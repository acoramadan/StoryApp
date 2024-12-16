package com.muflidevs.storyapp.data.remote.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.muflidevs.storyapp.data.local.RemoteKey
import com.muflidevs.storyapp.data.local.StoryDAO
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val apiService: ApiService,
    private val storyDAO: StoryDAO,
) : RemoteMediator<Int, Story>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                nextKey
            }
        }

        return try {
            val response = apiService.getAllStories(page = page, size = state.config.pageSize, 0)
            val stories = response.body()?.listStory ?: emptyList()

            if (loadType == LoadType.REFRESH) {
                storyDAO.clearAllStories()
            }
            storyDAO.insertAll(stories)

            val endOfPaginationReached = stories.isEmpty()

            if (loadType == LoadType.REFRESH) {
                storyDAO.insertRemoteKeys(
                    RemoteKey(
                        id = page.toString(),
                        nextKey = if (endOfPaginationReached) null else page + 1,
                        prevKey = null
                    )
                )
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKey? {
        val lastItem = state.lastItemOrNull() ?: return null
        return storyDAO.getRemoteKey(lastItem.id)
    }
}
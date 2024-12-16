package com.muflidevs.storyapp.data.remote.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muflidevs.storyapp.data.local.StoryDAO
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val storyDao: StoryDAO,private val location: Int) :
    PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1) ?: 1
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getAllStories(page = page, size = params.loadSize, location)
            val stories = response.body()?.listStory ?: emptyList()

            if (page == 1) {
                storyDao.clearAllStories()
            }
            storyDao.insertAll(stories)

            val endOfPaginationReached = stories.isEmpty()
            val nextKey = if (endOfPaginationReached) null else page + 1
            val prevKey = if (page == 1) null else page - 1

            LoadResult.Page(
                data = stories,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
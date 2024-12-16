package com.muflidevs.storyapp.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.muflidevs.storyapp.data.local.StoryDAO
import com.muflidevs.storyapp.data.remote.response.PostStoryResponse
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class StoryRepository(private val api: ApiService, private val storyDao: StoryDAO) {
    suspend fun getStories(location: Int = 0): List<Story>? {
        val response = api.getAllStories(location = location)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.listStory
        } else {
            throw Exception("Gagal mengambil data dari api {${response.code()}}")
        }
    }

    suspend fun getDetailStories(id: String): Story {
        val response = api.getDetailStories(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.story
        } else {
            throw Exception("ID tidak ditemukan ${response.code()} ${response.message()}")
        }
    }

    suspend fun postNewStories(description: String, filePhoto: File): Response<PostStoryResponse> {
        if (filePhoto.length() > 1024 * 1024) throw IllegalArgumentException("Maximum ukuran foto adalah 1mb!")

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val image = filePhoto.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            filePhoto.name,
            image
        )
        val response = api.postStories(descriptionBody, multipartBody)
        return response
    }

    suspend fun postNewStoriesWithLocation(description: String, filePhoto: File,lat: Double, lon: Double): Response<PostStoryResponse> {
        if (filePhoto.length() > 1024 * 1024) throw IllegalArgumentException("Maximum ukuran foto adalah 1mb!")

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        val latBody = lat.toString().toRequestBody("text/plain".toMediaType())
        val lonBody = lon.toString().toRequestBody("text/plain".toMediaType())

        val image = filePhoto.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            filePhoto.name,
            image
        )
        val response = api.postStories(descriptionBody, multipartBody,latBody,lonBody)
        return response
    }

    fun getPagedStories(location: Int): PagingSource<Int, Story> {
        return StoryPagingSource(api, storyDao,location)
    }

    fun getStory(): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(api,storyDao),
            pagingSourceFactory = {
                storyDao.getAllStory()
            }
        ).liveData
    }
}
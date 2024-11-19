package com.muflidevs.storyapp.data.remote.repository

import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiService

class StoryRepository(private val api: ApiService) {
    suspend fun getStories(token: String): List<Story>? {
        val response = api.getAllStories("Bearer $token")
        if(response.isSuccessful && response.body() != null) {
            return response.body()!!.listStory
        } else {
            throw Exception("Gagal mengambil data dari api {${response.code()}}")
        }
    }
}
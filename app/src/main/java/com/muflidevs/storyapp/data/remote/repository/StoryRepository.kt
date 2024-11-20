package com.muflidevs.storyapp.data.remote.repository

import android.util.Log
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.data.remote.retrofit.ApiService

class StoryRepository(private val api: ApiService) {
    suspend fun getStories(): List<Story>? {
        val response = api.getAllStories()
        if(response.isSuccessful && response.body() != null) {
            return response.body()!!.listStory
        } else {
            throw Exception("Gagal mengambil data dari api {${response.code()}}")
        }
    }
    suspend fun getDetailStories(id: String): Story {
        val response = api.getDetailStories(id)
        if(response.isSuccessful && response.body() != null) {
            return response.body()!!.story
        } else {
            throw Exception("ID tidak ditemukan ${response.code() } ${response.message()}")
        }
    }
}
package com.muflidevs.storyapp.data.remote.retrofit

import com.muflidevs.storyapp.data.remote.response.DetailStoryResponse
import com.muflidevs.storyapp.data.remote.response.LoginRequest
import com.muflidevs.storyapp.data.remote.response.LoginResponse
import com.muflidevs.storyapp.data.remote.response.PostStoryResponse
import com.muflidevs.storyapp.data.remote.response.RegisterRequest
import com.muflidevs.storyapp.data.remote.response.RegisterResponse
import com.muflidevs.storyapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    suspend fun postRegister(
        @Body register: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun postLogin(
        @Body login: LoginRequest
    ): Response<LoginResponse>

    @Multipart
    @POST("stories")
    suspend fun postStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): Response<PostStoryResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") storyId: String
    ): Response<DetailStoryResponse>


}
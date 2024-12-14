package com.muflidevs.storyapp.data.remote.retrofit

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL ="https://story-api.dicoding.dev/v1/"

        fun getApiService(token: String? = null): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    Log.d("ApiConfig", "Authorization: Bearer $token")
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                    chain.proceed(requestBuilder.build())
                }
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
package com.muflidevs.storyapp.data.remote.repository

import android.content.Context
import android.util.Log
import com.muflidevs.storyapp.data.remote.response.LoginRequest
import com.muflidevs.storyapp.data.remote.response.LoginResponse
import com.muflidevs.storyapp.data.remote.response.RegisterRequest
import com.muflidevs.storyapp.data.remote.response.RegisterResponse
import com.muflidevs.storyapp.data.remote.retrofit.ApiService


class AuthRepository(private val api: ApiService, private val context: Context) {

    suspend fun login(email: String, password: String): LoginResponse {
        val response = api.postLogin(LoginRequest(email,password))
        if(response.isSuccessful) {
            val loginResponse = response.body()
            saveToken(loginResponse!!.loginResult!!.token)
            return loginResponse
        } else {
            throw Exception(response.message())
        }
    }

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        val response = api.postRegister(RegisterRequest(username,email,password))
        if(response.isSuccessful && response.body() != null) {
            val registerResponse = response.body()
            return registerResponse!!
        } else {
            throw Exception(response.body()!!.message)
        }
    }

    private fun saveToken(token: String?) {
        val sharedPref = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
        sharedPref.edit().putString("TOKEN_KEY",token).apply()
        Log.e("AuthRepo","$token")
    }
    fun clearToken() {
        val sharedPref = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
        sharedPref.edit().remove("TOKEN_KEY").apply()
    }

    fun getToken(): String? {
        val sharedPref = context.getSharedPreferences("AppPrefs",Context.MODE_PRIVATE)
        return sharedPref.getString("TOKEN_KEY",null)
    }

}
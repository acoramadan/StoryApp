package com.muflidevs.storyapp.data.remote.repository

import android.content.Context
import android.util.Log
import com.muflidevs.storyapp.data.remote.response.LoginRequest
import com.muflidevs.storyapp.data.remote.response.LoginResponse
import com.muflidevs.storyapp.data.remote.response.RegisterRequest
import com.muflidevs.storyapp.data.remote.response.RegisterResponse
import com.muflidevs.storyapp.data.remote.retrofit.ApiService
import org.json.JSONObject


class AuthRepository(private val api: ApiService, private val context: Context? = null) {

    suspend fun login(email: String, password: String): LoginResponse {
        val response = api.postLogin(LoginRequest(email, password))
        if (response.isSuccessful) {
            val loginResponse = response.body()
            val token = loginResponse?.loginResult?.token
                ?: throw Exception("Tidak ada token yang didapat!")
            saveToken(token)
            return loginResponse
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = if (!errorBody.isNullOrEmpty()) {
                try {
                    JSONObject(errorBody).optString("message", "error tidak diketahui")
                } catch (e: Exception) {
                    "error tidak diketahui"
                }
            } else {
                "error"
            }
            throw Exception(errorMessage)
        }
    }


    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        val response = api.postRegister(RegisterRequest(username, email, password))
        if (response.isSuccessful) {
            val registerResponse = response.body()
                ?: throw Exception("Error not found")

            return registerResponse
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = if (!errorBody.isNullOrEmpty()) {
                try {
                    JSONObject(errorBody).optString("message", "error tidak diketahui")
                } catch (e: Exception) {
                    "error tidak diketahui"
                }
            } else {
                "error"
            }
            throw Exception(errorMessage)
        }
    }

    private fun saveToken(token: String?) {
        val sharedPref = context!!.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("TOKEN_KEY", token).apply()
        Log.e("AuthRepo", "$token")
    }

    fun clearToken() {
        val sharedPref = context!!.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().remove("TOKEN_KEY").apply()
    }

    fun getToken(): String? {
        val sharedPref = context!!.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("TOKEN_KEY", null)
    }

}
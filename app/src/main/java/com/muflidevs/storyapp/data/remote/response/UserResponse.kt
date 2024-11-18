package com.muflidevs.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class UserResponse(
    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
    val loginResult: User? = null
)

@Parcelize
data class User(
    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("token")
    val token: String? = null
): Parcelable
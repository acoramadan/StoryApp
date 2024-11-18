package com.muflidevs.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class StoryResponse(
    @field:SerializedName("error")
    var error: String? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("listStory")
    var listStory: List<Story>? = null
)

@Parcelize
data class Story (
    @field:SerializedName("id")
    var id: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("photoUrl")
    var photoUrl: String? = null,

    @field:SerializedName("createdAt")
    var createdAt: String? = null,

    @field:SerializedName("lat")
    var lat:Float? = null,

    @field:SerializedName("lon")
    var lon: Float? = null,

): Parcelable

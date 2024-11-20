package com.muflidevs.storyapp.data.remote.response

import com.google.gson.annotations.SerializedName


data class StoryResponse(
    @field:SerializedName("error")
    var error: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("listStory")
    var listStory: List<Story>? = null
)
data class PostStoryResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

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

)

data class DetailStoryResponse(
    @field:SerializedName("error")
    var error: Boolean,

    @field:SerializedName("message")
    var message: String,

    @field:SerializedName("story")
    var story: Story
)


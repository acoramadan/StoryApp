package com.muflidevs.storyapp.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
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

@Entity(tableName = "stories")
data class Story(
    @field:SerializedName("id")
    @PrimaryKey var id: String,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("photoUrl")
    var photoUrl: String? = null,

    @field:SerializedName("createdAt")
    var createdAt: String? = null,

    @field:SerializedName("lat")
    var lat: Double? = null,

    @field:SerializedName("lon")
    var lon: Double? = null,

    )

data class DetailStoryResponse(
    @field:SerializedName("error")
    var error: Boolean,

    @field:SerializedName("message")
    var message: String,

    @field:SerializedName("story")
    var story: Story

)


package com.muflidevs.storyapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


data class StoryEntity(
     val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
)

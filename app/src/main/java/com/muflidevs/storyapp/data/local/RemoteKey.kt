package com.muflidevs.storyapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val prevKey: Int?
)

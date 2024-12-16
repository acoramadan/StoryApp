package com.muflidevs.storyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muflidevs.storyapp.data.remote.response.Story

@Database(entities = [Story::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDAO
}


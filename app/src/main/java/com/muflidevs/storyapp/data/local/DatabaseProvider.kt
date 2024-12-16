package com.muflidevs.storyapp.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var INSTANCE: StoryDatabase? = null

    fun getDatabase(context: Context): StoryDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java,
                "story_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
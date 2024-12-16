package com.muflidevs.storyapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muflidevs.storyapp.data.remote.response.Story

@Dao
interface StoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<Story>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(remoteKeys: RemoteKey)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int,Story>

    @Query("SELECT * FROM remote_keys WHERE id = :storyId")
    suspend fun getRemoteKey(storyId: String): RemoteKey?

    @Query("DELETE FROM stories")
    suspend fun clearAllStories()
}

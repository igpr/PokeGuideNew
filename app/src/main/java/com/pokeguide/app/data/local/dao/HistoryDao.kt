package com.pokeguide.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokeguide.app.data.local.entity.HistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history WHERE profileId = :profileId ORDER BY viewedAt DESC LIMIT :limit")
    fun observeRecent(profileId: Long, limit: Int = 100): Flow<List<HistoryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: HistoryEntryEntity)

    @Query("DELETE FROM history WHERE profileId = :profileId")
    suspend fun clearProfile(profileId: Long)

    @Query("DELETE FROM history WHERE viewedAt < :threshold")
    suspend fun deleteOlderThan(threshold: Long)
}
package com.pokeguide.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pokeguide.app.data.local.entity.TrainerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerProfileDao {

    @Query("SELECT * FROM trainer_profiles ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<TrainerProfileEntity>>

    @Query("SELECT * FROM trainer_profiles WHERE id = :id")
    suspend fun getById(id: Long): TrainerProfileEntity?

    @Query("SELECT COUNT(*) FROM trainer_profiles")
    suspend fun count(): Int

    @Insert
    suspend fun insert(profile: TrainerProfileEntity): Long

    @Query("UPDATE trainer_profiles SET name = :name WHERE id = :id")
    suspend fun rename(id: Long, name: String)

    @Query("DELETE FROM trainer_profiles WHERE id = :id")
    suspend fun deleteById(id: Long)
}
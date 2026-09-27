package com.pokeguide.app.repository

import com.pokeguide.app.model.TrainerProfile
import kotlinx.coroutines.flow.Flow

interface TrainerProfileRepository {
    fun observeProfiles(): Flow<List<TrainerProfile>>
    suspend fun createProfile(name: String, avatarKey: String): Long
    suspend fun renameProfile(id: Long, newName: String)
    suspend fun deleteProfile(id: Long)
    suspend fun ensureDefaultProfile(): Long
}
package com.pokeguide.app.repository

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeRecentIds(profileId: Long, limit: Int = 50): Flow<List<Int>>
    suspend fun record(profileId: Long, pokemonId: Int)
    suspend fun clear(profileId: Long)
    suspend fun deleteOlderThan(threshold: Long)
}
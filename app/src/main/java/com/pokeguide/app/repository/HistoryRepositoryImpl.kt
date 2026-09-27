package com.pokeguide.app.repository

import com.pokeguide.app.data.local.dao.HistoryDao
import com.pokeguide.app.data.local.entity.HistoryEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val dao: HistoryDao
) : HistoryRepository {

    override fun observeRecentIds(profileId: Long, limit: Int): Flow<List<Int>> =
        dao.observeRecent(profileId, limit).map { list -> list.map { it.pokemonId } }

    override suspend fun record(profileId: Long, pokemonId: Int) {
        dao.upsert(
            HistoryEntryEntity(
                profileId = profileId,
                pokemonId = pokemonId,
                viewedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clear(profileId: Long) = dao.clearProfile(profileId)

    override suspend fun deleteOlderThan(threshold: Long) = dao.deleteOlderThan(threshold)
}
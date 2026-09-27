package com.pokeguide.app.repository

import com.pokeguide.app.api.PokeService
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.mapper.PokemonMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeService,
    private val cacheDao: CachedPokemonDao
) : PokemonRepository {

    override fun observeCatalogue() =
        cacheDao.observeAll().map { list -> list.map(PokemonMapper::entityToSummary) }

    override fun observeDetails(id: Int) =
        cacheDao.observeById(id).map { it?.let(PokemonMapper::entityToDetails) }

    override suspend fun refreshCatalogue(): Result<Unit> = safeCall {
        val now = System.currentTimeMillis()
        val entities = api.fetchList().entries.map { PokemonMapper.entryToEntity(it, now) }
        cacheDao.insertIfAbsent(entities)
    }

    override suspend fun refreshDetails(id: Int): Result<Unit> = safeCall {
        val now = System.currentTimeMillis()
        cacheDao.upsert(PokemonMapper.responseToEntity(api.fetchById(id), now))
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (ce: CancellationException) {
        throw ce
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
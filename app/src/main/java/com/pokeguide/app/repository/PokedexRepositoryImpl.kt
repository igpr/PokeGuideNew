package com.pokeguide.app.repository

import com.pokeguide.app.data.local.dao.CaughtPokemonDao
import com.pokeguide.app.data.local.entity.CaughtPokemonEntity
import com.pokeguide.app.model.PokemonStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokedexRepositoryImpl @Inject constructor(
    private val dao: CaughtPokemonDao
) : PokedexRepository {

    override fun observeStatus(profileId: Long, pokemonId: Int): Flow<PokemonStatus?> =
        dao.observeOne(profileId, pokemonId).map { it?.status?.let { s ->
            runCatching { PokemonStatus.valueOf(s) }.getOrNull()
        } }

    override fun observeNotes(profileId: Long): Flow<Map<Int, String>> =
        dao.observeByProfile(profileId).map { list ->
            list.mapNotNull { e -> e.note?.let { e.pokemonId to it } }.toMap()
        }

    override fun observeStatuses(profileId: Long): Flow<Map<Int, PokemonStatus>> =
        dao.observeByProfile(profileId).map { list ->
            list.mapNotNull { e ->
                runCatching { PokemonStatus.valueOf(e.status) }.getOrNull()
                    ?.let { e.pokemonId to it }
            }.toMap()
        }

    override fun observeIdsByStatus(profileId: Long, status: PokemonStatus): Flow<Set<Int>> =
        dao.observeIdsByStatus(profileId, status.name).map { it.toSet() }

    override suspend fun setStatus(profileId: Long, pokemonId: Int, status: PokemonStatus?) {
        if (status == null) {
            dao.delete(profileId, pokemonId)
        } else {
            val current = dao.getOne(profileId, pokemonId)
            dao.upsert(
                CaughtPokemonEntity(
                    profileId = profileId,
                    pokemonId = pokemonId,
                    status = status.name,
                    note = current?.note,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun setNote(profileId: Long, pokemonId: Int, note: String?) {
        val current = dao.getOne(profileId, pokemonId)
        val trimmed = note?.trim()?.takeIf { it.isNotEmpty() }
        if (current == null && trimmed == null) return
        dao.upsert(
            CaughtPokemonEntity(
                profileId = profileId,
                pokemonId = pokemonId,
                status = current?.status ?: PokemonStatus.SEEN.name,
                note = trimmed,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}
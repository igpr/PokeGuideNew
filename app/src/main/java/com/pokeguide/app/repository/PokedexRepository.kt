package com.pokeguide.app.repository

import com.pokeguide.app.data.local.entity.CaughtPokemonEntity
import com.pokeguide.app.model.PokemonStatus
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    fun observeStatus(profileId: Long, pokemonId: Int): Flow<PokemonStatus?>
    fun observeNotes(profileId: Long): Flow<Map<Int, String>>
    fun observeStatuses(profileId: Long): Flow<Map<Int, PokemonStatus>>
    fun observeIdsByStatus(profileId: Long, status: PokemonStatus): Flow<Set<Int>>
    suspend fun setStatus(profileId: Long, pokemonId: Int, status: PokemonStatus?)
    suspend fun setNote(profileId: Long, pokemonId: Int, note: String?)
}
package com.pokeguide.app.repository

import com.pokeguide.app.model.PokemonDetails
import com.pokeguide.app.model.PokemonSummary
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /** Единственный источник правды — Room. UI подписывается на этот Flow. */
    fun observeCatalogue(): Flow<List<PokemonSummary>>

    fun observeDetails(id: Int): Flow<PokemonDetails?>

    /** Сеть → Room. Список обновляет только отсутствующие записи. */
    suspend fun refreshCatalogue(): Result<Unit>

    /** Сеть → Room. Полностью перезаписывает запись. */
    suspend fun refreshDetails(id: Int): Result<Unit>
}
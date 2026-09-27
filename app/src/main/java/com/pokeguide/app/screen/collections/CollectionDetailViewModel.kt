package com.pokeguide.app.screen.collections

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.model.Collection
import com.pokeguide.app.model.PokemonSummary
import com.pokeguide.app.repository.CollectionRepository
import com.pokeguide.app.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CollectionDetailUiState(
    val collection: Collection? = null,
    val items: List<PokemonSummary> = emptyList()
)

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    saved: SavedStateHandle,
    private val collections: CollectionRepository,
    private val pokemon: PokemonRepository
) : ViewModel() {

    private val collectionId: Long = saved["collectionId"] ?: 0

    val state: StateFlow<CollectionDetailUiState> = combine(
        collections.observeCollection(collectionId),
        collections.observeItemIds(collectionId),
        pokemon.observeCatalogue()
    ) { col, ids, catalogue ->
        CollectionDetailUiState(
            collection = col,
            items = catalogue.filter { it.id in ids }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CollectionDetailUiState())

    fun removePokemon(pokemonId: Int) {
        viewModelScope.launch { collections.removeItem(collectionId, pokemonId) }
    }
}
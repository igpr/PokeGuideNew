package com.pokeguide.app.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.Collection
import com.pokeguide.app.model.PokemonDetails
import com.pokeguide.app.model.PokemonStatus
import com.pokeguide.app.repository.CollectionRepository
import com.pokeguide.app.repository.HistoryRepository
import com.pokeguide.app.repository.PokedexRepository
import com.pokeguide.app.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProfileState {
    data object Loading : ProfileState
    data class Ready(val pokemon: PokemonDetails) : ProfileState
    data class Failed(val reason: String) : ProfileState
}

data class DetailsUiState(
    val profileState: ProfileState = ProfileState.Loading,
    val status: PokemonStatus? = null,
    val note: String = "",
    val collections: List<Collection> = emptyList(),
    val collectionsWithPokemon: Set<Long> = emptySet()
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailsViewModel @Inject constructor(
    saved: SavedStateHandle,
    private val repo: PokemonRepository,
    private val pokedex: PokedexRepository,
    private val collections: CollectionRepository,
    private val history: HistoryRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val pokemonId: Int = saved["pokemonId"] ?: 0
    private val error = MutableStateFlow<String?>(null)
    private val noteDraft = MutableStateFlow<String?>(null)

    private val profileStateFlow = combine(repo.observeDetails(pokemonId), error) { d, e ->
        when {
            d != null -> ProfileState.Ready(d)
            e != null -> ProfileState.Failed(e)
            else -> ProfileState.Loading
        }
    }

    private val statusFlow = settings.activeProfileId.flatMapLatest { pid ->
        if (pid == null) flowOf(null) else pokedex.observeStatus(pid, pokemonId)
    }

    private val noteFlow = settings.activeProfileId.flatMapLatest { pid ->
        if (pid == null) flowOf("")
        else pokedex.observeNotes(pid).map { it[pokemonId].orEmpty() }
    }

    private val collectionListFlow = settings.activeProfileId.flatMapLatest { pid ->
        if (pid == null) flowOf(emptyList()) else collections.observeCollections(pid)
    }

    private val collectionMembershipFlow = settings.activeProfileId.flatMapLatest { pid ->
        if (pid == null) flowOf(emptySet<Long>())
        else collections.observeCollectionsContaining(pid, pokemonId)
    }

    val state: StateFlow<DetailsUiState> = combine(
        profileStateFlow,
        statusFlow,
        noteFlow,
        collectionListFlow,
        collectionMembershipFlow,
        noteDraft
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        DetailsUiState(
            profileState = values[0] as ProfileState,
            status = values[1] as PokemonStatus?,
            note = (values[5] as String?) ?: (values[2] as String),
            collections = values[3] as List<Collection>,
            collectionsWithPokemon = values[4] as Set<Long>
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailsUiState())

    init {
        load()
        recordHistory()
    }

    fun load() {
        viewModelScope.launch {
            error.value = null
            repo.refreshDetails(pokemonId)
                .onFailure { error.value = it.localizedMessage ?: "Ошибка" }
        }
    }

    private fun recordHistory() {
        viewModelScope.launch {
            val pid = settings.activeProfileId.first() ?: return@launch
            history.record(pid, pokemonId)
        }
    }

    fun setStatus(status: PokemonStatus) {
        viewModelScope.launch {
            val pid = settings.activeProfileId.first() ?: return@launch
            val current = state.value.status
            pokedex.setStatus(pid, pokemonId, if (current == status) null else status)
        }
    }

    fun onNoteChanged(value: String) {
        noteDraft.value = value
    }

    fun saveNote() {
        viewModelScope.launch {
            val pid = settings.activeProfileId.first() ?: return@launch
            pokedex.setNote(pid, pokemonId, noteDraft.value)
            noteDraft.value = null
        }
    }

    fun toggleInCollection(collectionId: Long) {
        viewModelScope.launch {
            collections.toggleItem(collectionId, pokemonId)
        }
    }
}
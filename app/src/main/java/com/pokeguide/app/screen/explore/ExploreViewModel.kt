package com.pokeguide.app.screen.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.PokemonStatus
import com.pokeguide.app.model.PokemonSummary
import com.pokeguide.app.repository.PokedexRepository
import com.pokeguide.app.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private val POPULAR_IDS = setOf(1, 6, 9, 25, 37, 39)

sealed interface ExploreState {
    data object Loading : ExploreState
    data class Content(
        val pokemon: List<PokemonSummary>,
        val searchText: String,
        val statuses: Map<Int, PokemonStatus>,
        val onlyCaught: Boolean
    ) : ExploreState
    data class Failed(val reason: String) : ExploreState
}

private enum class Mode { ALL, POPULAR }

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repo: PokemonRepository,
    private val pokedex: PokedexRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val mode = MutableStateFlow(Mode.ALL)
    private val isLoading = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)
    private val onlyCaught = MutableStateFlow(false)

    val state: StateFlow<ExploreState> = combine(
        repo.observeCatalogue(),
        query, mode, isLoading, error, onlyCaught, settings.activeProfileId
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val catalogue = values[0] as List<PokemonSummary>
        val q = values[1] as String
        val m = values[2] as Mode
        val loading = values[3] as Boolean
        val err = values[4] as String?
        val only = values[5] as Boolean

        if (err != null && catalogue.isEmpty()) return@combine ExploreState.Failed(err)
        if (loading && catalogue.isEmpty()) return@combine ExploreState.Loading

        // Статусы подгрузим отдельно; здесь просто пустая карта, заполнится ниже
        val base = if (m == Mode.POPULAR) catalogue.filter { it.id in POPULAR_IDS } else catalogue
        val searched = if (q.isBlank()) base
        else base.filter { it.name.contains(q, ignoreCase = true) }
        ExploreState.Content(searched, q, emptyMap(), only)
    }.combine(
        settings.activeProfileId.flatMapLatest { pid ->
            if (pid == null) flowOf(emptyMap())
            else pokedex.observeStatuses(pid)
        }
    ) { content, statuses ->
        if (content is ExploreState.Content) {
            val filtered = if (content.onlyCaught)
                content.pokemon.filter { statuses[it.id] == PokemonStatus.CAUGHT }
            else content.pokemon
            content.copy(pokemon = filtered, statuses = statuses)
        } else content
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExploreState.Loading)

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            repo.refreshCatalogue()
                .onFailure { error.value = it.localizedMessage ?: "Ошибка" }
            isLoading.value = false
        }
    }

    fun onSearchChanged(q: String) {
        query.value = q
        mode.value = Mode.ALL
    }

    fun showPopular() {
        query.value = ""
        mode.value = Mode.POPULAR
    }

    fun resetSearch() {
        query.value = ""
        mode.value = Mode.ALL
    }

    fun toggleOnlyCaught() {
        onlyCaught.value = !onlyCaught.value
    }
}
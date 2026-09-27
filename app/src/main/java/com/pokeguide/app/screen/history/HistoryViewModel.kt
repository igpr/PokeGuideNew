package com.pokeguide.app.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.PokemonSummary
import com.pokeguide.app.repository.HistoryRepository
import com.pokeguide.app.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val history: HistoryRepository,
    private val pokemon: PokemonRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    val state: StateFlow<List<PokemonSummary>> = settings.activeProfileId
        .flatMapLatest { pid ->
            if (pid == null) flowOf(emptyList())
            else combine(
                history.observeRecentIds(pid),
                pokemon.observeCatalogue()
            ) { ids, catalogue ->
                val byId = catalogue.associateBy { it.id }
                ids.mapNotNull { byId[it] }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun clear() {
        viewModelScope.launch {
            val pid = settings.activeProfileId.first() ?: return@launch
            history.clear(pid)
        }
    }
}
package com.pokeguide.app.screen.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.Collection
import com.pokeguide.app.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val repo: CollectionRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    val state: StateFlow<List<Collection>> = settings.activeProfileId
        .flatMapLatest { pid ->
            if (pid == null) flowOf(emptyList()) else repo.observeCollections(pid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun create(name: String) {
        viewModelScope.launch {
            val activeId = settings.activeProfileId.first()
            if (activeId != null) repo.create(activeId, name)
        }
    }

    fun rename(id: Long, name: String) {
        viewModelScope.launch { repo.rename(id, name) }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repo.delete(id) }
    }
}
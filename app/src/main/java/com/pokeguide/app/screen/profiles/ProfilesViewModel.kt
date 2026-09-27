package com.pokeguide.app.screen.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.TrainerProfile
import com.pokeguide.app.repository.TrainerProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfilesUiState(
    val profiles: List<TrainerProfile> = emptyList(),
    val activeId: Long? = null
)

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val repo: TrainerProfileRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    val state: StateFlow<ProfilesUiState> = combine(
        repo.observeProfiles(),
        settings.activeProfileId,
        refreshTrigger
    ) { profiles, active, _ ->
        ProfilesUiState(profiles = profiles, activeId = active)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfilesUiState())

    init {
        viewModelScope.launch { repo.ensureDefaultProfile() }
    }

    fun create(name: String, avatarKey: String) {
        viewModelScope.launch { repo.createProfile(name, avatarKey) }
    }

    fun rename(id: Long, newName: String) {
        viewModelScope.launch { repo.renameProfile(id, newName) }
    }

    fun delete(id: Long) {
        viewModelScope.launch { repo.deleteProfile(id) }
    }

    fun setActive(id: Long) {
        viewModelScope.launch { settings.setActiveProfile(id) }
    }
}
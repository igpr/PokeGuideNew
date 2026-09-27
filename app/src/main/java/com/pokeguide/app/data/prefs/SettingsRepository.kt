package com.pokeguide.app.data.prefs

import com.pokeguide.app.model.SortOrder
import com.pokeguide.app.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    val activeProfileId: Flow<Long?>
    val cacheTtlHours: Flow<Int>
    val syncIntervalHours: Flow<Int>
    val defaultSort: Flow<SortOrder>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setActiveProfile(id: Long?)
    suspend fun setCacheTtlHours(hours: Int)
    suspend fun setSyncIntervalHours(hours: Int)
    suspend fun setDefaultSort(order: SortOrder)
}
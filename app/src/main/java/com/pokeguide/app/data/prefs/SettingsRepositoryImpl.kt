package com.pokeguide.app.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pokeguide.app.model.SortOrder
import com.pokeguide.app.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val ACTIVE_PROFILE = longPreferencesKey("active_profile_id")
        val TTL = intPreferencesKey("cache_ttl_hours")
        val SYNC = intPreferencesKey("sync_interval_hours")
        val SORT = stringPreferencesKey("default_sort")
    }

    override val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.THEME] ?: ThemeMode.SYSTEM.name
        runCatching { ThemeMode.valueOf(raw) }.getOrDefault(ThemeMode.SYSTEM)
    }

    override val activeProfileId: Flow<Long?> = context.dataStore.data.map { it[Keys.ACTIVE_PROFILE] }

    override val cacheTtlHours: Flow<Int> = context.dataStore.data.map { it[Keys.TTL] ?: DEFAULT_TTL }

    override val syncIntervalHours: Flow<Int> = context.dataStore.data.map { it[Keys.SYNC] ?: DEFAULT_SYNC }

    override val defaultSort: Flow<SortOrder> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.SORT] ?: SortOrder.BY_ID.name
        runCatching { SortOrder.valueOf(raw) }.getOrDefault(SortOrder.BY_ID)
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME] = mode.name }
    }

    override suspend fun setActiveProfile(id: Long?) {
        context.dataStore.edit { prefs ->
            if (id == null) prefs.remove(Keys.ACTIVE_PROFILE) else prefs[Keys.ACTIVE_PROFILE] = id
        }
    }

    override suspend fun setCacheTtlHours(hours: Int) {
        context.dataStore.edit { it[Keys.TTL] = hours }
    }

    override suspend fun setSyncIntervalHours(hours: Int) {
        context.dataStore.edit { it[Keys.SYNC] = hours }
    }

    override suspend fun setDefaultSort(order: SortOrder) {
        context.dataStore.edit { it[Keys.SORT] = order.name }
    }

    companion object {
        const val DEFAULT_TTL = 12
        const val DEFAULT_SYNC = 6
    }
}
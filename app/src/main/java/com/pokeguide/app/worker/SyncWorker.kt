package com.pokeguide.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.data.sync.CachePolicy
import com.pokeguide.app.repository.PokemonRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: PokemonRepository,
    private val cacheDao: CachedPokemonDao,
    private val settings: SettingsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val ttl = settings.cacheTtlHours.first()
        val latest = cacheDao.latestCachedAt()
        val stale = latest == null || CachePolicy.isStale(latest, ttl)

        return if (stale) {
            repo.refreshCatalogue().fold(
                onSuccess = { Result.success() },
                onFailure = { Result.retry() }
            )
        } else {
            Result.success()
        }
    }

    companion object {
        const val NAME = "SyncWorker"
    }
}
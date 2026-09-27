package com.pokeguide.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.data.sync.CachePolicy
import com.pokeguide.app.repository.HistoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cacheDao: CachedPokemonDao,
    private val history: HistoryRepository,
    private val settings: SettingsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val ttl = settings.cacheTtlHours.first()
        val historyDays = 30

        // удалить кэш старше TTL
        cacheDao.deleteStale(CachePolicy.staleThreshold(ttl))

        // удалить историю старше 30 дней
        history.deleteOlderThan(CachePolicy.historyThreshold(historyDays))

        return Result.success()
    }

    companion object {
        const val NAME = "CleanupWorker"
    }
}
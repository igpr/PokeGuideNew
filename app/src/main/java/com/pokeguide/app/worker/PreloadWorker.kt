package com.pokeguide.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pokeguide.app.data.local.dao.CachedPokemonDao
import com.pokeguide.app.data.local.dao.CollectionDao
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.repository.HistoryRepository
import com.pokeguide.app.repository.PokemonRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class PreloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: PokemonRepository,
    private val cacheDao: CachedPokemonDao,
    private val collectionDao: CollectionDao,
    private val history: HistoryRepository,
    private val settings: SettingsRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val profileId = settings.activeProfileId.first() ?: return Result.success()
        val ids = mutableSetOf<Int>()

        // все покемоны из истории
        ids += history.observeRecentIds(profileId, 50).first()

        // все покемоны из коллекций профиля
        val collections = collectionDao.observeByProfile(profileId).first()
        collections.forEach { col ->
            collectionDao.observeItems(col.id).first().forEach { ids += it.pokemonId }
        }

        // предзагрузка деталей для тех, у кого ещё нет полных данных
        ids.forEach { id ->
            val cached = cacheDao.getById(id)
            if (cached == null || cached.types.isEmpty()) {
                repo.refreshDetails(id)
            }
        }
        return Result.success()
    }

    companion object {
        const val NAME = "PreloadWorker"
    }
}
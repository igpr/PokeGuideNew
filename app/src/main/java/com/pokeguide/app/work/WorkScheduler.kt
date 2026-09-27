package com.pokeguide.app.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.pokeguide.app.worker.CleanupWorker
import com.pokeguide.app.worker.PreloadWorker
import com.pokeguide.app.worker.SyncWorker
import java.util.concurrent.TimeUnit

object WorkScheduler {

    fun schedulePreload(context: Context) {
        val request = OneTimeWorkRequestBuilder<PreloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(context)
            .enqueue(request)
    }

    fun schedulePeriodicSync(context: Context, intervalHours: Int) {
        val request = PeriodicWorkRequestBuilder<SyncWorker>(
            intervalHours.toLong(), TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncWorker.NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun schedulePeriodicCleanup(context: Context) {
        val request = PeriodicWorkRequestBuilder<CleanupWorker>(
            24, TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CleanupWorker.NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
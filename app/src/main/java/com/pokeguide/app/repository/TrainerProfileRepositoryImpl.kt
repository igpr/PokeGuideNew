package com.pokeguide.app.repository

import com.pokeguide.app.data.local.dao.TrainerProfileDao
import com.pokeguide.app.data.local.entity.TrainerProfileEntity
import com.pokeguide.app.data.prefs.SettingsRepository
import com.pokeguide.app.model.TrainerProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainerProfileRepositoryImpl @Inject constructor(
    private val dao: TrainerProfileDao,
    private val settings: SettingsRepository
) : TrainerProfileRepository {

    override fun observeProfiles(): Flow<List<TrainerProfile>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun createProfile(name: String, avatarKey: String): Long {
        val id = dao.insert(
            TrainerProfileEntity(
                name = name.trim().ifBlank { "Тренер" },
                avatarKey = avatarKey,
                createdAt = System.currentTimeMillis()
            )
        )
        // если это первый профиль — делаем его активным
        if (settings.activeProfileId.first() == null) {
            settings.setActiveProfile(id)
        }
        return id
    }

    override suspend fun renameProfile(id: Long, newName: String) {
        dao.rename(id, newName.trim().ifBlank { "Тренер" })
    }

    override suspend fun deleteProfile(id: Long) {
        val wasActive = settings.activeProfileId.first() == id
        dao.deleteById(id)
        if (wasActive) {
            val remaining = dao.observeAll().first()
            settings.setActiveProfile(remaining.firstOrNull()?.id)
        }
    }

    override suspend fun ensureDefaultProfile(): Long {
        val existing = dao.observeAll().first()
        if (existing.isNotEmpty()) {
            val active = settings.activeProfileId.first()
            if (active == null || existing.none { it.id == active }) {
                settings.setActiveProfile(existing.first().id)
            }
            return existing.first().id
        }
        return createProfile("Тренер", DEFAULT_AVATAR)
    }

    private fun TrainerProfileEntity.toDomain() =
        TrainerProfile(id = id, name = name, avatarKey = avatarKey, createdAt = createdAt)

    companion object {
        const val DEFAULT_AVATAR = "red"
    }
}
package com.pokeguide.app.repository

import com.pokeguide.app.data.local.dao.CollectionDao
import com.pokeguide.app.data.local.entity.CollectionEntity
import com.pokeguide.app.data.local.entity.CollectionItemEntity
import com.pokeguide.app.model.Collection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionRepositoryImpl @Inject constructor(
    private val dao: CollectionDao
) : CollectionRepository {

    override fun observeCollections(profileId: Long): Flow<List<Collection>> =
        dao.observeByProfile(profileId).map { list ->
            list.map { Collection(it.id, it.profileId, it.name, it.createdAt) }
        }

    override fun observeCollection(id: Long): Flow<Collection?> =
        dao.observeById(id).map { e ->
            e?.let { Collection(it.id, it.profileId, it.name, it.createdAt) }
        }

    override fun observeItemIds(collectionId: Long): Flow<Set<Int>> =
        dao.observeItems(collectionId).map { list -> list.map { it.pokemonId }.toSet() }

    override fun observeCollectionsContaining(profileId: Long, pokemonId: Int): Flow<Set<Long>> =
        dao.observeByProfile(profileId).map { collections ->
            collections.mapNotNull { col ->
                if (dao.contains(col.id, pokemonId) > 0) col.id else null
            }.toSet()
        }

    override suspend fun create(profileId: Long, name: String): Long =
        dao.insert(
            CollectionEntity(
                profileId = profileId,
                name = name.trim().ifBlank { "Коллекция" },
                createdAt = System.currentTimeMillis()
            )
        )

    override suspend fun rename(id: Long, name: String) {
        dao.rename(id, name.trim().ifBlank { "Коллекция" })
    }

    override suspend fun delete(id: Long) = dao.delete(id)

    override suspend fun addItem(collectionId: Long, pokemonId: Int) {
        dao.addItem(CollectionItemEntity(collectionId, pokemonId, System.currentTimeMillis()))
    }

    override suspend fun removeItem(collectionId: Long, pokemonId: Int) {
        dao.removeItem(collectionId, pokemonId)
    }

    override suspend fun toggleItem(collectionId: Long, pokemonId: Int) {
        if (dao.contains(collectionId, pokemonId) > 0) {
            dao.removeItem(collectionId, pokemonId)
        } else {
            addItem(collectionId, pokemonId)
        }
    }
}
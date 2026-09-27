package com.pokeguide.app.repository

import com.pokeguide.app.model.Collection
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {
    fun observeCollections(profileId: Long): Flow<List<Collection>>
    fun observeCollection(id: Long): Flow<Collection?>
    fun observeItemIds(collectionId: Long): Flow<Set<Int>>
    fun observeCollectionsContaining(profileId: Long, pokemonId: Int): Flow<Set<Long>>
    suspend fun create(profileId: Long, name: String): Long
    suspend fun rename(id: Long, name: String)
    suspend fun delete(id: Long)
    suspend fun addItem(collectionId: Long, pokemonId: Int)
    suspend fun removeItem(collectionId: Long, pokemonId: Int)
    suspend fun toggleItem(collectionId: Long, pokemonId: Int)
}
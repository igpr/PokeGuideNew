package com.pokeguide.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokeguide.app.data.local.entity.CollectionEntity
import com.pokeguide.app.data.local.entity.CollectionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections WHERE profileId = :profileId ORDER BY createdAt DESC")
    fun observeByProfile(profileId: Long): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections WHERE id = :id")
    fun observeById(id: Long): Flow<CollectionEntity?>

    @Insert
    suspend fun insert(collection: CollectionEntity): Long

    @Query("UPDATE collections SET name = :name WHERE id = :id")
    suspend fun rename(id: Long, name: String)

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun delete(id: Long)

    // --- items ---

    @Query("SELECT * FROM collection_items WHERE collectionId = :collectionId ORDER BY addedAt DESC")
    fun observeItems(collectionId: Long): Flow<List<CollectionItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(item: CollectionItemEntity)

    @Query("DELETE FROM collection_items WHERE collectionId = :collectionId AND pokemonId = :pokemonId")
    suspend fun removeItem(collectionId: Long, pokemonId: Int)

    @Query("SELECT COUNT(*) FROM collection_items WHERE collectionId = :collectionId AND pokemonId = :pokemonId")
    suspend fun contains(collectionId: Long, pokemonId: Int): Int
}
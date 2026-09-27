package com.pokeguide.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokeguide.app.data.local.entity.CachedPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedPokemonDao {

    @Query("SELECT * FROM cached_pokemon ORDER BY id ASC")
    fun observeAll(): Flow<List<CachedPokemonEntity>>

    @Query("SELECT * FROM cached_pokemon WHERE id = :id")
    fun observeById(id: Int): Flow<CachedPokemonEntity?>

    @Query("SELECT * FROM cached_pokemon WHERE id = :id")
    suspend fun getById(id: Int): CachedPokemonEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIfAbsent(list: List<CachedPokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CachedPokemonEntity)

    @Query("SELECT MAX(cachedAt) FROM cached_pokemon")
    suspend fun latestCachedAt(): Long?

    @Query("SELECT COUNT(*) FROM cached_pokemon")
    suspend fun count(): Int

    @Query("DELETE FROM cached_pokemon WHERE cachedAt < :threshold")
    suspend fun deleteStale(threshold: Long)
}
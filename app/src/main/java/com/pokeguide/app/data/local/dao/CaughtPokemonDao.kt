package com.pokeguide.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pokeguide.app.data.local.entity.CaughtPokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaughtPokemonDao {

    @Query("SELECT * FROM caught_pokemon WHERE profileId = :profileId")
    fun observeByProfile(profileId: Long): Flow<List<CaughtPokemonEntity>>

    @Query("SELECT * FROM caught_pokemon WHERE profileId = :profileId AND pokemonId = :pokemonId")
    fun observeOne(profileId: Long, pokemonId: Int): Flow<CaughtPokemonEntity?>

    @Query("SELECT pokemonId FROM caught_pokemon WHERE profileId = :profileId AND status = :status")
    fun observeIdsByStatus(profileId: Long, status: String): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CaughtPokemonEntity)

    @Query("DELETE FROM caught_pokemon WHERE profileId = :profileId AND pokemonId = :pokemonId")
    suspend fun delete(profileId: Long, pokemonId: Int)

    @Query("DELETE FROM caught_pokemon WHERE profileId = :profileId")
    suspend fun clearProfile(profileId: Long)

    @Query("SELECT * FROM caught_pokemon WHERE profileId = :profileId AND pokemonId = :pokemonId LIMIT 1")
    suspend fun getOne(profileId: Long, pokemonId: Int): CaughtPokemonEntity?
}
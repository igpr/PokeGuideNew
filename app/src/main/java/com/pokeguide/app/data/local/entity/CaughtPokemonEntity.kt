package com.pokeguide.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "caught_pokemon",
    primaryKeys = ["profileId", "pokemonId"],
    foreignKeys = [
        ForeignKey(
            entity = TrainerProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId"), Index("pokemonId")]
)
data class CaughtPokemonEntity(
    val profileId: Long,
    val pokemonId: Int,
    val status: String,
    val note: String?,
    val updatedAt: Long
)
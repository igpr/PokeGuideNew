package com.pokeguide.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pokeguide.app.model.Ability
import com.pokeguide.app.model.StatValue

@Entity(tableName = "cached_pokemon")
data class CachedPokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val heightDm: Int,
    val weightHg: Int,
    val baseXp: Int,
    val types: List<String>,
    val stats: List<StatValue>,
    val abilities: List<Ability>,
    val cachedAt: Long
)
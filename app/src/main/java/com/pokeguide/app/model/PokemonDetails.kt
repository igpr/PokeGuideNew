package com.pokeguide.app.model

/** Доменная модель покемона для экрана деталей */
data class PokemonDetails(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val heightDm: Int,
    val weightHg: Int,
    val baseXp: Int,
    val types: List<String>,
    val stats: List<StatValue>,
    val abilities: List<Ability>
)

/** Одна характеристика (HP, Атака, и т.д.) */
data class StatValue(val label: String, val base: Int)

/** Способность покемона */
data class Ability(val name: String, val hidden: Boolean)

package com.pokeguide.app.model

/** Доменная модель покемона для списка (без сырых DTO) */
data class PokemonSummary(
    val id: Int,
    val name: String,
    val imageUrl: String
)

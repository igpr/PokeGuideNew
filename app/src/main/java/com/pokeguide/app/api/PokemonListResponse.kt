package com.pokeguide.app.api

import com.google.gson.annotations.SerializedName

/** DTO ответа списка покемонов */
data class PokemonListResponse(
    @SerializedName("count") val total: Int,
    @SerializedName("results") val entries: List<PokemonEntry>
)

/** Элемент списка — имя и ссылка на детали */
data class PokemonEntry(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

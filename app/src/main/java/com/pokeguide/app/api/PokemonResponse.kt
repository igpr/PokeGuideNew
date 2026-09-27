package com.pokeguide.app.api

import com.google.gson.annotations.SerializedName

/** DTO полного ответа по одному покемону */
data class PokemonResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("base_experience") val baseExperience: Int?,
    @SerializedName("types") val types: List<TypeSlot>,
    @SerializedName("stats") val stats: List<StatSlot>,
    @SerializedName("abilities") val abilities: List<AbilitySlot>,
    @SerializedName("sprites") val sprites: Sprites
)

data class TypeSlot(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: NamedRef
)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: NamedRef
)

data class AbilitySlot(
    @SerializedName("ability") val ability: NamedRef,
    @SerializedName("is_hidden") val isHidden: Boolean
)

/** Общий тип для ссылки на именованный ресурс API */
data class NamedRef(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: OtherSprites?
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: ArtworkSprites?
)

data class ArtworkSprites(
    @SerializedName("front_default") val frontDefault: String?
)

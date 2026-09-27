package com.pokeguide.app.data.mapper

import com.pokeguide.app.api.PokemonEntry
import com.pokeguide.app.api.PokemonResponse
import com.pokeguide.app.data.local.entity.CachedPokemonEntity
import com.pokeguide.app.model.Ability
import com.pokeguide.app.model.PokemonDetails
import com.pokeguide.app.model.PokemonSummary
import com.pokeguide.app.model.StatValue

private const val ARTWORK_TEMPLATE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%d.png"

object PokemonMapper {

    /** Список из API → сущность для кэша (без полных данных). */
    fun entryToEntity(entry: PokemonEntry, now: Long): CachedPokemonEntity {
        val id = entry.url.trimEnd('/').substringAfterLast('/').toInt()
        return CachedPokemonEntity(
            id = id,
            name = entry.name.replaceFirstChar { it.uppercaseChar() },
            imageUrl = ARTWORK_TEMPLATE.format(id),
            heightDm = 0,
            weightHg = 0,
            baseXp = 0,
            types = emptyList(),
            stats = emptyList(),
            abilities = emptyList(),
            cachedAt = now
        )
    }

    /** Полный ответ API → сущность для кэша. */
    fun responseToEntity(response: PokemonResponse, now: Long): CachedPokemonEntity =
        CachedPokemonEntity(
            id = response.id,
            name = response.name.replaceFirstChar { it.uppercaseChar() },
            imageUrl = response.sprites.other?.officialArtwork?.frontDefault
                ?: response.sprites.frontDefault
                ?: ARTWORK_TEMPLATE.format(response.id),
            heightDm = response.height,
            weightHg = response.weight,
            baseXp = response.baseExperience ?: 0,
            types = response.types.sortedBy { it.slot }.map {
                it.type.name.replaceFirstChar { c -> c.uppercaseChar() }
            },
            stats = response.stats.map { s ->
                StatValue(label = formatStatName(s.stat.name), base = s.baseStat)
            },
            abilities = response.abilities.map { a ->
                Ability(
                    name = a.ability.name.replace('-', ' ')
                        .split(' ').joinToString(" ") { w -> w.replaceFirstChar { c -> c.uppercaseChar() } },
                    hidden = a.isHidden
                )
            },
            cachedAt = now
        )

    fun entityToSummary(e: CachedPokemonEntity): PokemonSummary =
        PokemonSummary(id = e.id, name = e.name, imageUrl = e.imageUrl)

    fun entityToDetails(e: CachedPokemonEntity): PokemonDetails =
        PokemonDetails(
            id = e.id,
            name = e.name,
            imageUrl = e.imageUrl,
            heightDm = e.heightDm,
            weightHg = e.weightHg,
            baseXp = e.baseXp,
            types = e.types,
            stats = e.stats,
            abilities = e.abilities
        )

    private fun formatStatName(raw: String): String = when (raw) {
        "hp" -> "HP"
        "attack" -> "Атака"
        "defense" -> "Защита"
        "special-attack" -> "Сп. Атк"
        "special-defense" -> "Сп. Защ"
        "speed" -> "Скорость"
        else -> raw.replaceFirstChar { it.uppercaseChar() }
    }
}
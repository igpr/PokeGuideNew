package com.pokeguide.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.pokeguide.app.api.AbilitySlot
import com.pokeguide.app.api.ArtworkSprites
import com.pokeguide.app.api.NamedRef
import com.pokeguide.app.api.OtherSprites
import com.pokeguide.app.api.PokemonEntry
import com.pokeguide.app.api.PokemonResponse
import com.pokeguide.app.api.Sprites
import com.pokeguide.app.api.StatSlot
import com.pokeguide.app.api.TypeSlot
import org.junit.Test

class PokemonMapperTest {

    private val now = 1_700_000_000_000L

    @Test
    fun `entry maps to entity with correct id and name`() {
        val entry = PokemonEntry(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/"
        )
        val entity = PokemonMapper.entryToEntity(entry, now)

        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.name).isEqualTo("Bulbasaur")
        assertThat(entity.cachedAt).isEqualTo(now)
        assertThat(entity.types).isEmpty()
    }

    @Test
    fun `response maps stats and abilities`() {
        val response = PokemonResponse(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            baseExperience = 112,
            types = listOf(
                TypeSlot(slot = 1, type = NamedRef("electric", ""))
            ),
            stats = listOf(
                StatSlot(baseStat = 35, effort = 0, stat = NamedRef("hp", "")),
                StatSlot(baseStat = 55, effort = 0, stat = NamedRef("attack", ""))
            ),
            abilities = listOf(
                AbilitySlot(ability = NamedRef("static", ""), isHidden = false),
                AbilitySlot(ability = NamedRef("lightning-rod", ""), isHidden = true)
            ),
            sprites = Sprites(
                frontDefault = null,
                other = OtherSprites(ArtworkSprites(frontDefault = "art.png"))
            )
        )
        val entity = PokemonMapper.responseToEntity(response, now)

        assertThat(entity.id).isEqualTo(25)
        assertThat(entity.name).isEqualTo("Pikachu")
        assertThat(entity.imageUrl).isEqualTo("art.png")
        assertThat(entity.types).containsExactly("Electric")
        assertThat(entity.stats.map { it.label }).containsExactly("HP", "Атака").inOrder()
        assertThat(entity.abilities).hasSize(2)
        assertThat(entity.abilities[0].hidden).isFalse()
        assertThat(entity.abilities[1].hidden).isTrue()
    }
}
package com.pokeguide.app.theme

import androidx.compose.ui.graphics.Color

// Основные цвета тёмной темы
val Scarlet = Color(0xFFE94560)
val ScarletDark = Color(0xFFB71C3A)
val Goldenrod = Color(0xFFFFD740)
val MintGreen = Color(0xFF69F0AE)
val DarkNavy = Color(0xFF1A1A2E)
val DarkSurface = Color(0xFF16213E)
val LightText = Color(0xFFF5F5F5)
val SubtleGrey = Color(0xFF8E99A4)

/** Цвета типов покемонов для бейджиков и фонов */
object TypeColor {
    private val map = mapOf(
        "Normal" to Color(0xFFA8A878),
        "Fire" to Color(0xFFF08030),
        "Water" to Color(0xFF6890F0),
        "Grass" to Color(0xFF78C850),
        "Electric" to Color(0xFFF8D030),
        "Ice" to Color(0xFF98D8D8),
        "Fighting" to Color(0xFFC03028),
        "Poison" to Color(0xFFA040A0),
        "Ground" to Color(0xFFE0C068),
        "Flying" to Color(0xFFA890F0),
        "Psychic" to Color(0xFFF85888),
        "Bug" to Color(0xFFA8B820),
        "Rock" to Color(0xFFB8A038),
        "Ghost" to Color(0xFF705898),
        "Dragon" to Color(0xFF7038F8),
        "Dark" to Color(0xFF705848),
        "Steel" to Color(0xFFB8B8D0),
        "Fairy" to Color(0xFFEE99AC)
    )

    fun of(type: String): Color = map[type] ?: Color(0xFF68A090)
}

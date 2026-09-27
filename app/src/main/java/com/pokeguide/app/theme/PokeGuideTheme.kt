package com.pokeguide.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Тёмная цветовая схема в стиле Покедекса */
private val PokeDarkScheme = darkColorScheme(
    primary = Scarlet,
    onPrimary = LightText,
    secondary = Goldenrod,
    tertiary = MintGreen,
    background = DarkNavy,
    surface = DarkSurface,
    onBackground = LightText,
    onSurface = LightText,
    error = Color(0xFFCF6679),
    outline = SubtleGrey
)

/** Тема приложения PokéGuide */
@Composable
fun PokeGuideTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PokeDarkScheme,
        typography = PokeTypography,
        content = content
    )
}

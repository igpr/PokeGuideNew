package com.pokeguide.app.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Экран пустого состояния (MOD_A3).
 * Показывается, когда по поисковому запросу ничего не нашлось.
 * Предлагает 3 действия: популярные, случайный, сброс.
 */
@Composable
fun EmptyView(
    query: String,
    onPopular: () -> Unit,
    onSurprise: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "По запросу «$query» ничего не найдено",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Попробуй одно из действий ниже",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))

        // Действие 1: показать популярных покемонов
        ActionButton(
            label = "Популярные",
            icon = Icons.Outlined.Star,
            onClick = onPopular
        )
        Spacer(Modifier.height(10.dp))

        // Действие 2: перейти к случайному покемону
        ActionButton(
            label = "Мне повезёт!",
            icon = Icons.Outlined.Casino,
            onClick = onSurprise
        )
        Spacer(Modifier.height(10.dp))

        // Действие 3: сбросить поиск и показать всё
        ActionButton(
            label = "Сбросить поиск",
            icon = Icons.AutoMirrored.Outlined.Undo,
            onClick = onReset
        )
    }
}

@Composable
private fun ActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(8.dp))
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

package com.pokeguide.app.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pokeguide.app.theme.TypeColor

/** Бейджик типа покемона (Fire, Water и т.д.) с соответствующим цветом */
@Composable
fun TypeChip(type: String, modifier: Modifier = Modifier) {
    val bg = TypeColor.of(type)
    Text(
        text = type,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

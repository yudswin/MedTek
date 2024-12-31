package com.medtek.main.survey.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

private val gradientColors = listOf(
    Color(0xFFD8E1E8),
    Color(0xFFC6D3E3),
    Color(0xFFB2CBDE),
    Color(0xFF98BAD5),
    Color(0xFF304674)
)

@Composable
fun GradientBackgroundBrush(
    isVertical: Boolean = true,
    colors: List<Color> = gradientColors
): Brush {
    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}
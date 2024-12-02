package com.medtek.main.core.presentation.home.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.medtek.main.ui.theme.AppTheme
import java.time.LocalDate
import kotlin.random.Random
import kotlin.random.nextInt

// Empty Item
@Composable
fun CalendarItemEmpty(
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Box(
        modifier = Modifier
            .height(size)
            .width(size)
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) { /* TODOS */ }
}

@Composable
fun CalendarTitle(
    day: String
) {
    Box(
        modifier = Modifier
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CalendarItem(
    size: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    day: Int = (0..30).random(),
    progress: Float = (0..100).random().toFloat() / 100,
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .height(size)
            .width(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (progress >= 100) {
            Icons.Default.BeachAccess
        } else {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { progress },
                strokeWidth = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = "${day + 1}",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@Preview
@Composable
fun PreviewCalendarItemEmpty() {
    AppTheme {
        CalendarItemEmpty()
    }
}

@Preview
@Composable
fun PreviewCalendarItem() {
    AppTheme {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalendarItem()
            CalendarItem(progress = 0.2f)
            CalendarItem(progress = 0.5f)
            CalendarItem(progress = 0.7f)
            CalendarItem(progress = 1f)
        }
    }
}


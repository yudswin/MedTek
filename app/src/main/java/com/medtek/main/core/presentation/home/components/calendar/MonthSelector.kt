package com.medtek.main.core.presentation.home.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MonthSelector(
    currentDate: LocalDate,
    onMonthChange: (LocalDate) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val formattedDate = currentDate.format(formatter)
    val today = LocalDate.now()
    val minDate = today.minusMonths(2)
    val maxDate = today.plusMonths(2)

    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    val newDate = currentDate.minusMonths(1)
                    if (newDate.isAfter(minDate) || newDate.isEqual(minDate)) {
                        onMonthChange(newDate)
                    }
                },
                enabled = currentDate.isAfter(minDate)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowLeft,
                    contentDescription = "Previous Month",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = formattedDate,
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            IconButton(
                onClick = {
                    val newDate = currentDate.plusMonths(1)
                    if (newDate.isBefore(maxDate) || newDate.isEqual(maxDate)) {
                        onMonthChange(newDate)
                    }
                },
                enabled = currentDate.isBefore(maxDate)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowRight,
                    contentDescription = "Next Month",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
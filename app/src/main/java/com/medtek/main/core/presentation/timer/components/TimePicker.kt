package com.medtek.main.core.presentation.timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun TimePicker(
    minutes: Int = 120,
    onMinutesChange: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .padding(18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = MaterialTheme.shapes.large
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = minutes.toString(),
                    style = MaterialTheme.typography.headlineMedium, // Large text
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "mins",
                    style = MaterialTheme.typography.bodySmall, // Smaller text
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            IconButton(
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large
                ),
                onClick = { onMinutesChange(minutes + 30) }) {
                Icon(
                    Icons.Default.ArrowUpward, contentDescription = "Increase minutes",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.large
                ),
                onClick = { if (minutes > 0) onMinutesChange(minutes - 30) }) {
                Icon(
                    Icons.Default.ArrowDownward, contentDescription = "Decrease minutes",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
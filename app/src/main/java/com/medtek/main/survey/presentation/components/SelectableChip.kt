package com.medtek.main.survey.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.ui.theme.AppTheme

@Composable
fun SelectableChip(
    label: String,
    icon: String,
    isSelected: Boolean = false,
    onSelectedChange: (Boolean) -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onSelectedChange(!isSelected) }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    maxLines = 1
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    maxLines = 1
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewChip() {
    AppTheme {
        SelectableChip(
            label = "Test",
            icon = "👁️",
            isSelected = true,
            onSelectedChange = {}
        )
    }
}

@Preview
@Composable
fun PreviewChipDisable() {
    AppTheme {
        SelectableChip(
            label = "Test",
            icon = "👁️",
            isSelected = false,
            onSelectedChange = {}
        )
    }
}
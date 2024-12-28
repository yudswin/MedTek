package com.medtek.main.survey.presentation.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopSection() {
    var worksCategory = listOf<String>(
        "Marketing", "Engineering", "Reading", "Traveling"
    )
    FlowRow(
        modifier = Modifier.padding(8.dp)
    ) {
        for (work in worksCategory) {
            ChipItem(work)
        }
    }
}

@Composable
fun ChipItem(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(4.dp)
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

data class WorkItem(
    val icon: ImageVector,
    val title: String,
    val category: String
)


@Preview
@Composable
fun PreviewTopSection() {
    AppTheme {
        TopSection()
    }
}
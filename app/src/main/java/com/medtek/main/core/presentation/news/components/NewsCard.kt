package com.medtek.main.core.presentation.news.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsCard() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "News Card",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(64.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
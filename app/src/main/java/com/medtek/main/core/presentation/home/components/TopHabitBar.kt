package com.medtek.main.core.presentation.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopHabitBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "Habit Screen",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = Icons.Default.Mail,
                contentDescription = "Account",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = "Account",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    )
}
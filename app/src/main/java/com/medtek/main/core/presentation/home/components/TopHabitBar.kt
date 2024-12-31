package com.medtek.main.core.presentation.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHabitBar(navController: NavController) {
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
            IconButton(
                onClick = {
                    navController.navigate("greeting")
                }
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Filled.WbSunny,
                    contentDescription = "Account",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
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
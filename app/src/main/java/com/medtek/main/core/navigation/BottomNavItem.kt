package com.medtek.main.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Habit : BottomNavItem(
        route = "home",
        title = "habit",
        icon = Icons.Default.Home
    )

    object News : BottomNavItem(
        route = "news",
        title = "News",
        icon = Icons.Default.Newspaper
    )

    object Timer : BottomNavItem(
        route = "timer",
        title = "Timer",
        icon = Icons.Default.Timer
    )

    object Music : BottomNavItem(
        route = "music",
        title = "Music",
        icon = Icons.Default.LibraryMusic
    )
}
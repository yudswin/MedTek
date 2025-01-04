package com.medtek.main.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.medtek.main.core.navigation.BottomNavBar
import com.medtek.main.core.navigation.BottomNavGraph
import com.medtek.main.core.presentation.music.service.MusicService
import com.medtek.main.core.presentation.timer.service.FocusSessionService

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    focusSessionService: FocusSessionService,
    musicService: MusicService,
    initPage: Int = 0,
) {
    val innerNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = { BottomNavBar(navController = innerNavController) }
    ) { innerPadding ->
        BottomNavGraph(
            outterNavController = navController,
            navController = innerNavController,
            paddingValues = innerPadding,
            focusSessionService = focusSessionService,
            musicService = musicService,
            initPage = initPage
        )
    }
}



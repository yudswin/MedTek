package com.medtek.main.core.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medtek.main.core.presentation.home.HabitScreen
import com.medtek.main.core.presentation.home.viewmodel.HabitViewModel
import com.medtek.main.core.presentation.music.MusicScreen
import com.medtek.main.core.presentation.music.service.MusicService
import com.medtek.main.core.presentation.news.NewsScreen
import com.medtek.main.core.presentation.news.NewsViewModel
import com.medtek.main.core.presentation.timer.TimerScreen
import com.medtek.main.core.presentation.timer.service.FocusSessionService


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNavGraph(
    focusSessionService: FocusSessionService,
    musicService: MusicService,
    outterNavController: NavController,
    navController: NavHostController,
    paddingValues: PaddingValues,
    initPage: Int
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = BottomNavItem.Habit.route
    ) {
        composable(route = BottomNavItem.Habit.route) {
            val habitViewModel: HabitViewModel = hiltViewModel()
            HabitScreen(
                initialPage = initPage,
                viewModel = habitViewModel,
                navController = navController,
                outterNavController = outterNavController
            )
        }
        composable(route = BottomNavItem.News.route) {
            val viewModel: NewsViewModel = hiltViewModel()
            NewsScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = BottomNavItem.Timer.route) {
            TimerScreen(focusSessionService)
        }
        composable(route = BottomNavItem.Music.route) {
            MusicScreen(musicService)
        }
    }
}
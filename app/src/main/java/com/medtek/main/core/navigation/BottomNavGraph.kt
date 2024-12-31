package com.medtek.main.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medtek.main.core.presentation.calendar.CalendarScreen
import com.medtek.main.core.presentation.home.HabitScreen
import com.medtek.main.core.presentation.home.HabitViewModel
import com.medtek.main.core.presentation.music.MusicScreen
import com.medtek.main.core.presentation.news.NewsScreen
import com.medtek.main.core.presentation.news.WeatherViewModel

@Composable
fun BottomNavGraph(
    outterNavController: NavController,
    navController: NavHostController,
    paddingValues: PaddingValues,
    isEmptyHabit: Boolean = false,
    initPage: Int
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = BottomNavItem.Habit.route
    ) {
        composable(route = BottomNavItem.Habit.route) {
            val habitViewModel: HabitViewModel = viewModel()
            HabitScreen(
                isEmpty = isEmptyHabit,
                initialPage = initPage,
                viewModel = habitViewModel,
                navController = navController,
                outterNavController = outterNavController
            )
        }
        composable(route = BottomNavItem.Calendar.route) {
            CalendarScreen()
        }
        composable(route = BottomNavItem.News.route) { backStackEntry ->
            val viewModel: WeatherViewModel = hiltViewModel(backStackEntry)
            NewsScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = BottomNavItem.Music.route) {
            MusicScreen()
        }
    }
}
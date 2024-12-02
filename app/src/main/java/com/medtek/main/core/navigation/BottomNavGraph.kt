package com.medtek.main.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medtek.main.core.presentation.calendar.CalendarScreen
import com.medtek.main.core.presentation.home.HabitScreen
import com.medtek.main.core.presentation.music.MusicScreen
import com.medtek.main.core.presentation.news.NewsScreen

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues, isEmptyHabit: Boolean = false, initPage: Int) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = BottomNavItem.Habit.route
    ) {
        composable(route = BottomNavItem.Habit.route) {
            HabitScreen(isEmpty = isEmptyHabit, initialPage = initPage)
        }
        composable(route = BottomNavItem.Calendar.route) {
            CalendarScreen()
        }
        composable(route = BottomNavItem.News.route) {
            NewsScreen()
        }
        composable(route = BottomNavItem.Music.route) {
            MusicScreen()
        }
    }
}
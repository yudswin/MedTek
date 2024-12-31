package com.medtek.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.core.HomeScreen
import com.medtek.main.survey.EntryScreen
import com.medtek.main.survey.viewmodel.EntryViewModel
import com.medtek.main.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainNavHost()
            }
        }
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {
        composable("entry") {
            val entryViewModel: EntryViewModel = hiltViewModel()
            EntryScreen(
                viewModel = entryViewModel,
                navController = navController
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}





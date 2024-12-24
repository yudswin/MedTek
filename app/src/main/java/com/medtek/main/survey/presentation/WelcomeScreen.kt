package com.medtek.main.survey.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.pages.AuthPage
import com.medtek.main.survey.presentation.pages.WelcomePage
import com.medtek.main.survey.viewmodel.WelcomeViewModel

@Composable
fun WelcomeScreen(parentNavController: NavController) {

    val navController: NavHostController = rememberNavController()
    val welcomeViewModel: WelcomeViewModel = hiltViewModel()


    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomePage(
                navController = navController,
                viewModel = welcomeViewModel
            )
        }

        composable("auth") {
            AuthPage(
                navController = parentNavController,
                viewModel = welcomeViewModel
            )
        }
    }
}



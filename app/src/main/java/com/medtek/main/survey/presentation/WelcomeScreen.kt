package com.medtek.main.survey.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.pages.AuthPage
import com.medtek.main.survey.presentation.pages.WelcomePage
import com.medtek.main.survey.viewmodel.WelcomeViewModel
import com.medtek.main.utilties.sharedViewModel

@Composable
fun WelcomeScreen(
    navController: NavHostController,
) {
    val nestedNavController = rememberNavController()
    NavHost(
        navController = nestedNavController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            val sharedViewModel = it.sharedViewModel<WelcomeViewModel>(navController)
            WelcomePage(
                navController = nestedNavController,
                viewModel = sharedViewModel
            )
        }

        composable("auth") {
            val sharedViewModel = it.sharedViewModel<WelcomeViewModel>(navController)
            AuthPage(
                navController = nestedNavController,
                viewModel = sharedViewModel,
                onSuccess = {
                    navController.navigate("survey") {
                        popUpTo("welcome")
                    }
                }
            )
        }
    }
}



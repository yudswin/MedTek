package com.medtek.main.survey

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.SurveyScreen
import com.medtek.main.survey.presentation.WelcomeScreen
import com.medtek.main.survey.viewmodel.SurveyViewModel

@Composable
fun EntryScreen(
    parentNavController: NavHostController
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "welcome_screen"
    ) {
        composable("welcome_screen") {
            WelcomeScreen()
        }
        composable("survey_screen") { backStackEntry ->
            val surveyViewModel: SurveyViewModel = hiltViewModel(backStackEntry)
            SurveyScreen(
                viewModel = surveyViewModel,
                parentNavController = navController
            )
        }
    }
}

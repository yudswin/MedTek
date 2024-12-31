package com.medtek.main.survey

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.SurveyScreen
import com.medtek.main.survey.presentation.WelcomeScreen
import com.medtek.main.survey.viewmodel.EntryViewModel
import com.medtek.main.survey.viewmodel.SurveyViewModel
import com.medtek.main.utilties.sharedViewModel

@Composable
fun EntryScreen(
    navController: NavHostController,
    viewModel: EntryViewModel = hiltViewModel(),
) {
    val loadingState = viewModel.loadingState
    val isAuthenticated = viewModel.isAuth.collectAsState()
    val isNewUser = viewModel.isNewUser.collectAsState()

    when {
        loadingState.value -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        isNewUser.value != true -> {
            LaunchedEffect(Unit) {
                navController.navigate("home") {
                    popUpTo("entry") { inclusive = true }
                }
            }
        }

        else -> {
            val nestedNavController = rememberNavController()
            NavHost(
                navController = nestedNavController,
                startDestination = if (isAuthenticated.value) "survey" else "welcome"
            ) {
                composable("welcome") {
                    viewModel.setAuth(false)
                    WelcomeScreen(
                        navController = nestedNavController
                    )
                }
                composable("survey") {
                    viewModel.setNewUser(true)
                    val surveyViewModel = it.sharedViewModel<SurveyViewModel>(navController)
                    SurveyScreen(
                        navController = navController,
                        viewModel = surveyViewModel
                    )
                }
            }
        }
    }
}


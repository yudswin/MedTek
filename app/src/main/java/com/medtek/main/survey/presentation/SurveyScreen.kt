package com.medtek.main.survey.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.pages.SurveyPage
import com.medtek.main.survey.presentation.pages.TimeExercise
import com.medtek.main.survey.presentation.pages.TimeUsagePage
import com.medtek.main.survey.viewmodel.SurveyViewModel
import com.medtek.main.utilties.sharedViewModel

@Composable
fun SurveyScreen(
    navController: NavHostController,
    viewModel: SurveyViewModel = hiltViewModel(),
) {
    val fieldState = viewModel.fieldState
    val loadingState = viewModel.loadingState
    val pages = fieldState.value.keys.toList()

    if (loadingState.value && pages.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (pages.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.getAllField()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        val nestedNavController = rememberNavController()
        NavHost(
            navController = nestedNavController,
            startDestination = pages.firstOrNull() ?: "empty",
        ) {
            composable("empty") {
                viewModel.getAllField()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            pages.forEachIndexed { i, page ->
                composable(page) {
                    SurveyPage(
                        title = page,
                        navController = nestedNavController,
                        viewModel = viewModel,
                        options = viewModel.getConfigValues(page),
                        nextPage = pages.getOrNull(i + 1) ?: "phoneUsage"
                    )
                }
            }
            composable("phoneUsage") {
                TimeUsagePage(
                    navController = nestedNavController,
                    viewModel = viewModel,
                    nextPage = "exercise"
                )
            }
            composable("exercise") {
                TimeExercise(
                    navController = nestedNavController,
                    viewModel = viewModel,
                    onComplete = {
                        navController.navigate("home") {
                            popUpTo("entry") { inclusive = true }
                        }
                    }

                )
            }
        }
    }
}



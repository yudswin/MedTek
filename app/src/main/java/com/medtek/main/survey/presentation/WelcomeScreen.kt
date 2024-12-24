package com.medtek.main.survey.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medtek.main.survey.presentation.pages.AuthPage
import com.medtek.main.survey.presentation.pages.WelcomePage

@SuppressLint("UnrememberedMutableState")
@Composable
fun WelcomeScreen() {
    var isWelcomePageCompleted = mutableStateOf(false)
    var isAuthPageCompleted = mutableStateOf(false)

    val navController: NavHostController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomePage(
                onComplete = { isWelcomePageCompleted.value = true },
                canMoveToNext = isWelcomePageCompleted,
                onMoveToNext = { TODO() },
                navController = navController
            )
        }

        composable("auth") {
            AuthPage(
                onComplete = { isAuthPageCompleted.value = true },
                canFinish = isAuthPageCompleted,
                onFinish = { TODO() },
                navController = navController
            )
        }
    }
}



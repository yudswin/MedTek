package com.medtek.main.survey.presentation.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun WelcomePage(
    onComplete: () -> Unit,
    canMoveToNext: MutableState<Boolean>,
    onMoveToNext: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to the App!")
        Button(onClick = onComplete) {
            Text("Mark as Done")
        }
        if (canMoveToNext.value) {
            Button(onClick = onMoveToNext) {
                Text("Next")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { navController.navigate("auth") }) {
                Text("Go to Auth")
            }
        }
    }
}
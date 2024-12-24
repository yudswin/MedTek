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
fun AuthPage(
    onComplete: () -> Unit,
    canFinish: MutableState<Boolean>,
    onFinish: () -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Authentication Page")
        Button(onClick = onComplete) {
            Text("Complete Authentication")
        }
        if (canFinish.value) {
            Button(onClick = onFinish) {
                Text("Finish")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { navController.navigate("details") }) {
                Text("Go to Details")
            }
        }
    }
}
package com.medtek.main.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.medtek.main.survey.presentation.components.SurveyBody
import com.medtek.main.survey.presentation.components.TopSection

@Composable
fun SurveyScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopSection()
        SurveyBody()
    }
}

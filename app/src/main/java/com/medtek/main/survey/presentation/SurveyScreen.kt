package com.medtek.main.survey.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.survey.presentation.components.SurveyBody
import com.medtek.main.survey.presentation.components.TopSection
import com.medtek.main.survey.viewmodel.SurveyViewModel

@Composable
fun SurveyScreen(
    parentNavController: NavController,
    viewModel: SurveyViewModel = hiltViewModel(),
) {
    val fieldState = viewModel.fieldState.value
    val loadingState = viewModel.loadingState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopSection()
        SurveyBody()
    }
}

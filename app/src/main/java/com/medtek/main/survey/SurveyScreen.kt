package com.medtek.main.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.survey.presentation.components.SurveyBody
import com.medtek.main.survey.presentation.components.TopSection
import com.medtek.main.ui.theme.AppTheme

@Composable
fun SurveyScreen(
    navController: NavController? = null,
    viewModel: SurveyViewModel = hiltViewModel()
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

@Preview
@Composable
fun PreviewSurveyScreen() {
    AppTheme {
        SurveyScreen()
    }
}
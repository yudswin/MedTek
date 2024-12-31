package com.medtek.main.greeting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import com.medtek.main.greeting.components.Action
import com.medtek.main.greeting.components.GreetingBottomBar
import com.medtek.main.greeting.components.GreetingContent
import com.medtek.main.greeting.components.GreetingTopBar

@Composable
fun GreetingScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onClose: () -> Unit,
    onActionClick: (Action) -> Unit
) {

    val weatherState = viewModel.weatherState.value
    val weatherLoadingState = viewModel.weatherLoadingState
    val weatherLoadError = viewModel.weatherLoadError.value
    val weatherErrorState = viewModel.weatherErrorState.value

    val quoteState = viewModel.quoteState.value
    val quoteLoadingState = viewModel.quoteLoadingState
    val quoteLoadError = viewModel.quoteLoadError.value
    val quoteErrorState = viewModel.quoteErrorState.value

    val loadingState = weatherLoadingState.value && quoteLoadingState.value

    Scaffold(
        topBar = { GreetingTopBar(onClose = onClose) },
        bottomBar = { GreetingBottomBar(onActionClick = onActionClick) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                loadingState -> LoadingView()
                weatherLoadError.isNotEmpty() || quoteLoadError.isNotEmpty() -> ErrorView(
                    weatherError = weatherErrorState ?: "Unknown weather error",
                    quoteError = quoteErrorState ?: "Unknown quote error"
                )
                weatherState != null && quoteState != null -> GreetingContent(
                    weather = weatherState,
                    quote = quoteState
                )
                else -> ErrorView("Unexpected error occurred.")
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(weatherError: String, quoteError: String? = null) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Error occurred:", style = MaterialTheme.typography.titleLarge, color = Color.Red)
            Text("Weather: $weatherError", style = MaterialTheme.typography.bodyLarge)
            quoteError?.let {
                Text("Quote: $it", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
















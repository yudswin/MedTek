package com.medtek.main.core.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.core.presentation.news.components.NewsCard
import com.medtek.main.data.local.entities.Weather

@Composable
fun NewsScreen(
    navController: NavController,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherState = viewModel.weatherState.value
    val loadingState = viewModel.loadingState
    val loadError = viewModel.loadError.value
    val errorState = viewModel.errorState.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("News Screen", style = MaterialTheme.typography.bodySmall)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loadingState.value) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    } else {
                        weatherState?.let { WeatherCard(it) }
                    }

                    if (loadError.isNotEmpty()) {
                        viewModel.loadWeather()
                    }
                }

                LazyColumn {
                    items(10) {
                        NewsCard()
                    }
                }
            }
        }
    }
}


@Composable
fun WeatherCard(weather: Weather) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Today's Weather")
            Text("Temperature: ${weather.temp}°C")
            Text("Condition: ${weather.condition}")
            Text("Humidity: ${weather.humidity}%")
            Text("Wind Speed: ${weather.windSpeed} km/h")
        }
    }
}


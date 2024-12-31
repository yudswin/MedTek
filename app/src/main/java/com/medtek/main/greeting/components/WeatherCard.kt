package com.medtek.main.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.medtek.main.data.local.entities.Weather

@Composable
fun WeatherCard(weather: Weather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Today's Weather", style = MaterialTheme.typography.titleLarge)
            Text("Temperature: ${weather.temp}°C", style = MaterialTheme.typography.bodyLarge)
            Text("Condition: ${weather.condition}", style = MaterialTheme.typography.bodyLarge)
            Text("Humidity: ${weather.humidity}%", style = MaterialTheme.typography.bodyLarge)
            Text(
                "Wind Speed: ${weather.windSpeed} km/h",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
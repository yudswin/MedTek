package com.medtek.main.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.local.entities.Weather


@Composable
fun GreetingContent(weather: Weather, quote: Quote) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherCard(weather)
        QuoteCard(quote)
    }
}

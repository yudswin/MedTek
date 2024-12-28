package com.medtek.main.core.presentation.greeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun GreetingContent(
    quote: String,
    author: String,
    temperature: Double,
    feelsLike: Double,
    location: String
) {
    Box(
        modifier = Modifier
            .size(width = 350.dp, height = 600.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFDF7F2)) // A soft, warm background
    ) {
        // Decorative quote icons
        Icon(
            imageVector = Icons.Default.FormatQuote,
            contentDescription = "Quote start",
            modifier = Modifier
                .size(80.dp)
                .offset(x = 10.dp, y = 10.dp),
            tint = Color.Black.copy(alpha = 0.6f)
        )
        Icon(
            imageVector = Icons.Default.FormatQuote,
            contentDescription = "Quote end",
            modifier = Modifier
                .size(80.dp)
                .offset(x = 260.dp, y = 500.dp)
                .alpha(0.6f),
            tint = Color.Black.copy(alpha = 0.6f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            QuoteArea(quote = quote, author = author)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = Color.Black.copy(alpha = 0.1f),
                thickness = 1.dp
            )

            WeatherArea(
                temperature = temperature,
                feelsLike = feelsLike,
                location = location
            )
        }
    }
}
package com.medtek.main.core.presentation.greeting

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import com.medtek.main.core.presentation.greeting.components.ActionBar
import com.medtek.main.core.presentation.greeting.components.CloseBar
import com.medtek.main.core.presentation.greeting.components.GreetingBottomBar
import com.medtek.main.core.presentation.greeting.components.GreetingContent
import com.medtek.main.core.presentation.greeting.components.GreetingTopBar



@Preview(showBackground = true)
@Composable
fun GreetingScreen(
    quote: String = "Success is not what you do when you are on top. Success is how high you bounce when you hit the bottom.",
    author: String = "Sonia Ricotti",
    temperature: Double = 72.0,
    feelsLike: Double = 69.0,
    location: String = "Hanoi, Vietnam",
    onClose: () -> Unit = {},
    onShare: () -> Unit = {},
    onFavorite: () -> Unit = {}
) {
    Scaffold(
        topBar = { GreetingTopBar(onClose = onClose) },
        bottomBar = { GreetingBottomBar(onShare = onShare, onFavorite = onFavorite) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            GreetingContent(
                quote = quote,
                author = author,
                temperature = temperature,
                feelsLike = feelsLike,
                location = location
            )
        }
    }
}














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
import com.medtek.main.core.presentation.greeting.components.WeatherCard
import com.medtek.main.core.presentation.greeting.components.locationText
import com.medtek.main.core.presentation.greeting.components.quoteCard


@Preview
@Composable
fun GreetingDisplay() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 400.dp, height = 50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                CloseBar()
            }

            Box(
                modifier = Modifier
                    .size(width = 400.dp, height = 750.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 10.dp, vertical = 30.dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawRect(
                        color = Color.LightGray,
                        size = size
                    )
                }
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .offset(x = 30.dp)
                )
                Column(
                    modifier = Modifier.offset(y = 100.dp)
                ) {
                    quoteCard()
                }
                Column(
                    modifier = Modifier.offset(y = 350.dp),
                ) {
                    WeatherCard()
                    locationText()
                }
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 100.dp, height = 100.dp)
                        .offset(x = 240.dp, y = 600.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(width = 400.dp, height = 50.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                ActionBar()
            }
        }
    }
}














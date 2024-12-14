package com.medtek.main.greeting

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.greeting.Presentation.Components.ActionBar
import com.medtek.main.greeting.Presentation.Components.CloseBar
import com.medtek.main.greeting.Presentation.Components.WeatherCard
import com.medtek.main.greeting.Presentation.Components.locationText
import com.medtek.main.greeting.Presentation.Components.quoteCard
import java.io.File
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.medtek.main.ui.theme.AppTheme
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun WeatherDisplay() {
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














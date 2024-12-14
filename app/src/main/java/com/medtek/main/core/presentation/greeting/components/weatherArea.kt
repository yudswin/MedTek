package com.medtek.main.core.presentation.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun WeatherCard(){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp),
            contentAlignment = Alignment.CenterStart
        ) {

        }
        Column(
            modifier = Modifier.size(120.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
            ) {
                Text(
                    modifier = Modifier.size(60.dp).padding(horizontal = 0.dp, vertical = 15.dp),
                    text = "72F",
                    fontSize = 30.sp
                )
            }
            Box(
                modifier = Modifier.size(120.dp)
            ) {
                Text(
                    modifier = Modifier.size(120.dp),
                    text = "Feel 69F",
                    fontSize = 30.sp
                )
            }
        }
    }
}

fun DrawScope.drawSun() {
    drawCircle(
        color = Color.Yellow,
        radius = size.minDimension / 4,
        center = center
    )
    for (i in 0 until 360 step 30) {
        val angleRad = Math.toRadians(i.toDouble())
        val lineLength = size.minDimension / 2
        val startX = center.x + (size.minDimension / 4) * Math.cos(angleRad).toFloat()
        val startY = center.y + (size.minDimension / 4) * Math.sin(angleRad).toFloat()
        val endX = center.x + lineLength * Math.cos(angleRad).toFloat()
        val endY = center.y + lineLength * Math.sin(angleRad).toFloat()
        drawLine(
            color = Color.Yellow,
            start = androidx.compose.ui.geometry.Offset(startX, startY),
            end = androidx.compose.ui.geometry.Offset(endX, endY),
            strokeWidth = 4f
        )
    }
}
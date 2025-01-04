package com.medtek.main.core.presentation.home.components.streak

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.ui.theme.AppTheme

@Composable
fun InfoGraphic(
    currentStreak: Int = 36,
    longestStreak: Int = 7,
    totalHabits: Int = 108,
    currentProgress: Float = 0.8f
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f) // Ensures equal width
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = totalHabits.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                DisplayText("Habits Done")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f) // Ensures equal width
            ) {
                StreakCanvas(
                    progress = currentProgress,
                    total = currentStreak
                )
                DisplayText("Current Streak")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f) // Ensures equal width
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    longestStreak.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                DisplayText("Longest Streak")
            }
        }
    }
}


@Composable
fun DisplayText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}

@Composable
fun StreakCanvas(
    progress: Float = 0.1f,
    total: Int = 36,
) {
    val iconSize =
        with(LocalDensity.current) { MaterialTheme.typography.headlineMedium.fontSize.toDp() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
    ) {
        CircularProgressIndicator(
            progress = { progress },
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxSize()
                .padding(iconSize / 2)
        )
        Text(
            total.toString(),
            style = if (total < 100) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Fire Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(iconSize)

            )
        }
    }
}


@Preview
@Composable
fun PreviewInfoGraphic() {
    AppTheme {
        InfoGraphic()
    }
}

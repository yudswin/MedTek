package com.medtek.main.core.presentation.home.components.progresscard

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.medtek.main.data.local.entities.Habit

@Composable
fun HabitCard(
    habit: Habit,
    onUpdate: (trackingId: String, amount: Int) -> Unit
) {
    var localProgress by remember { mutableIntStateOf(habit.progress) }
    val progress = habit.progress
    val goal = habit.goal
    val percentage = localProgress.toFloat() / goal

    Card(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val changeAmount = (dragAmount / 10).toInt() // Adjust sensitivity
                        localProgress = (localProgress + changeAmount).coerceIn(0, goal)
                    },
                    onDragEnd = {
                        onUpdate(habit.trackingId, localProgress - habit.progress)
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    habit.icon,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                        .padding(10.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = habit.habitName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${habit.progress} / ${habit.goal} ${habit.targetUnit}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                val displayProgress = progress * 100 / goal
                Text(
                    text = "${displayProgress}%",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
                        .padding(16.dp),
                )
            }
            LinearProgressIndicator(
                progress = { percentage },
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}


//@Preview
//@Composable
//fun PreviewCards() {
//    AppTheme {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//        ) {
//            val mockHabit = Habit(
//                trackingId = "1",
//                dayPlanId = 1,
//                habitName = "Read a Book",
//                habitType = "DAILY",
//                defaultScore = 10,
//                description = "Read at least 30 minutes",
//                targetUnit = "MINUTES",
//                progress = 15,
//                goal = 30,
//                icon = "📚"
//            )
//            items(10) { index ->
//                HabitCard(mockHabit, onUpdate = {})
//            }
//        }
//    }
//}
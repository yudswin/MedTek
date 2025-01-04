package com.medtek.main.core.presentation.timer

import androidx.compose.animation.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.medtek.main.core.presentation.timer.components.TimePicker
import com.medtek.main.core.presentation.timer.service.FocusSessionService
import com.medtek.main.core.presentation.timer.service.FocusSessionState
import com.medtek.main.core.presentation.timer.service.ServiceHelper
import com.medtek.main.utilties.Constants.ACTION_SERVICE_PAUSE
import com.medtek.main.utilties.Constants.ACTION_SERVICE_RESUME
import com.medtek.main.utilties.Constants.ACTION_SERVICE_START
import com.medtek.main.utilties.Constants.ACTION_SERVICE_STOP

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(
    focusSessionService: FocusSessionService
) {
    val context = LocalContext.current

    // Observe states from the service
    val hours by focusSessionService.hours
    val minutes by focusSessionService.minutes
    val seconds by focusSessionService.seconds
    val currentState by focusSessionService.currentState
    val remainingSessions by focusSessionService.remainingSessions

    // Local state for user input
    val inputMinutes = remember { mutableStateOf("30") }
    val inputSessions = remember { mutableStateOf("") }
    val isTimerRunning = currentState == FocusSessionState.Running
    val isTimerPaused = currentState == FocusSessionState.Paused
    val isTimerIdle = currentState == FocusSessionState.Idle

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Timer Input and Start Button
        if (isTimerIdle) {
            Column(
                modifier = Modifier.weight(9f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(minutes = inputMinutes.value.toIntOrNull() ?: 0,
                    onMinutesChange = { input ->
                        inputMinutes.value = input.toString()
                    })

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val totalMinutes = inputMinutes.value.toIntOrNull()
                        if (totalMinutes == null || totalMinutes <= 0) {
                            // Provide user feedback for invalid input (e.g., a Toast message)
                            return@Button
                        }
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = ACTION_SERVICE_START,
                            focusMinutes = totalMinutes
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = inputMinutes.value.isNotBlank() && isTimerIdle
                ) {
                    Text("Start Focus Session")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Timer Display with Animation
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = "$hours:$minutes:$seconds",
                transitionSpec = { addAnimation() },
                label = "Timer Animation"
            ) { targetState ->
                Text(
                    text = targetState, style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Text(
                text = "Sessions Left: $remainingSessions",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Pause/Resume Button
            Button(
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (isTimerRunning) ACTION_SERVICE_PAUSE else ACTION_SERVICE_RESUME
                    )
                }, enabled = !isTimerIdle
            ) {
                Text(if (isTimerRunning) "Pause" else "Resume")
            }

            // Stop Button
            Button(
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_STOP
                    )
                }, enabled = !isTimerIdle
            ) {
                Text("Stop")
            }

            // Restart Button
            Button(onClick = {
                focusSessionService.restartServiceSessions()
            }) {
                Text("Restart")
            }
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 600): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}
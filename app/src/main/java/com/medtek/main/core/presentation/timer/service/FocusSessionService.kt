package com.medtek.main.core.presentation.timer.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.medtek.main.utilties.Constants.ACTION_SERVICE_CANCEL
import com.medtek.main.utilties.Constants.ACTION_SERVICE_CONFIRM
import com.medtek.main.utilties.Constants.ACTION_SERVICE_PAUSE
import com.medtek.main.utilties.Constants.ACTION_SERVICE_RESUME
import com.medtek.main.utilties.Constants.ACTION_SERVICE_START
import com.medtek.main.utilties.Constants.NOTIFICATION_CHANNEL_ID
import com.medtek.main.utilties.Constants.NOTIFICATION_CHANNEL_NAME
import com.medtek.main.utilties.Constants.NOTIFICATION_ID
import com.medtek.main.utilties.formatTime
import com.medtek.main.utilties.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@ExperimentalAnimationApi
@AndroidEntryPoint
class FocusSessionService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = FocusSessionBinder()

    private var totalFocusDuration: Duration = Duration.ZERO
    private var remainingFocusTime: Duration = Duration.ZERO
    private var breakTime: Duration = 5.minutes
    private var isBreakSession = false
    private var timer: Timer? = null

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(FocusSessionState.Idle)
        private set

    var remainingSessions = mutableStateOf(0)
        private set

    override fun onBind(intent: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SERVICE_START -> {
                val focusMinutes = intent.getIntExtra("FOCUS_MINUTES", 0)
                startFocusSessions(focusMinutes)
            }

            ACTION_SERVICE_CONFIRM -> continueNextSession()
            ACTION_SERVICE_CANCEL -> stopServiceSessions()
            ACTION_SERVICE_PAUSE -> pauseTimer()
            ACTION_SERVICE_RESUME -> resumeTimer()
        }
        return START_NOT_STICKY
    }

    fun startFocusSessions(focusMinutes: Int) {
        totalFocusDuration = focusMinutes.minutes
        remainingFocusTime = minOf(totalFocusDuration, 20.minutes)
        isBreakSession = false
        remainingSessions.value = (focusMinutes / 20)
        startForegroundService()
        startTimer()
    }

    private fun startTimer() {
        currentState.value = FocusSessionState.Running
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            remainingFocusTime -= 1.seconds
            updateTimeUnits()
            updateNotification()

            if (remainingFocusTime <= Duration.ZERO) {
                cancel()
                notifyUserForConfirmation()
            }
        }
    }

    private fun stopServiceSessions() {
        timer?.cancel()
        currentState.value = FocusSessionState.Idle
        stopForegroundService()
    }

    private fun notifyUserForConfirmation() {
        currentState.value = FocusSessionState.Confirming
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder
                .setContentTitle("Focus Session")
                .setContentText(
                    if (isBreakSession) "Break session ended. Ready to focus?"
                    else "Focus session ended. Ready for a break?"
                )
                .addAction(
                    NotificationCompat.Action(
                        0, "Confirm", ServiceHelper.confirmPendingIntent(this)
                    )
                )
                .addAction(
                    NotificationCompat.Action(0, "Cancel", ServiceHelper.cancelPendingIntent(this))
                )
                .build()
        )
    }

    private fun updateTimeUnits() {
        remainingFocusTime.toComponents { hours, minutes, seconds, _ ->
            this@FocusSessionService.hours.value = hours.toInt().pad()
            this@FocusSessionService.minutes.value = minutes.pad()
            this@FocusSessionService.seconds.value = seconds.pad()
        }
    }

    fun continueNextSession() {
        if (isBreakSession) {
            remainingFocusTime = minOf(totalFocusDuration, 20.minutes)
            isBreakSession = false
        } else {
            totalFocusDuration -= remainingFocusTime
            remainingFocusTime = breakTime
            isBreakSession = true
            remainingSessions.value -= 1 // Decrement remaining sessions
        }

        if (remainingSessions.value == 0) {
            stopServiceSessions()
            return
        }

        startTimer()
    }

    fun restartServiceSessions() {
        timer?.cancel()
        totalFocusDuration = Duration.ZERO
        remainingFocusTime = Duration.ZERO
        isBreakSession = false
        currentState.value = FocusSessionState.Idle
        updateTimeUnits()
        stopForegroundService()
    }

    private fun updateNotification() {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(hours = hours.value, minutes = minutes.value, seconds = seconds.value)
            ).build()
        )
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun pauseTimer() {
        timer?.cancel()
        currentState.value = FocusSessionState.Paused
    }

    private fun resumeTimer() {
        if (currentState.value == FocusSessionState.Paused) {
            startTimer()
        }
    }

    inner class FocusSessionBinder : Binder() {
        fun getService(): FocusSessionService = this@FocusSessionService
    }
}

enum class FocusSessionState {
    Idle, Running, Confirming, Started, Stopped, Canceled, Paused
}
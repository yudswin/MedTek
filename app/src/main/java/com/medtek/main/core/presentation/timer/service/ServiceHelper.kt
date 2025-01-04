package com.medtek.main.core.presentation.timer.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import com.medtek.main.MainActivity
import com.medtek.main.utilties.Constants.ACTION_SERVICE_CANCEL
import com.medtek.main.utilties.Constants.ACTION_SERVICE_CONFIRM
import com.medtek.main.utilties.Constants.ACTION_SERVICE_PAUSE
import com.medtek.main.utilties.Constants.ACTION_SERVICE_RESUME
import com.medtek.main.utilties.Constants.ACTION_SERVICE_STOP
import com.medtek.main.utilties.Constants.CANCEL_REQUEST_CODE
import com.medtek.main.utilties.Constants.CLICK_REQUEST_CODE
import com.medtek.main.utilties.Constants.FOCUS_SESSION_STATE
import com.medtek.main.utilties.Constants.PAUSE_REQUEST_CODE
import com.medtek.main.utilties.Constants.RESUME_REQUEST_CODE
import com.medtek.main.utilties.Constants.STOP_REQUEST_CODE

@ExperimentalAnimationApi
object ServiceHelper {

    private val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }

    private fun createServicePendingIntent(
        context: Context,
        action: String,
        requestCode: Int,
        extraState: FocusSessionState? = null
    ): PendingIntent {
        val intent = Intent(context, FocusSessionService::class.java).apply {
            this.action = action
            extraState?.let { putExtra(FOCUS_SESSION_STATE, it.name) }
        }
        return PendingIntent.getService(context, requestCode, intent, flag)
    }

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(FOCUS_SESSION_STATE, FocusSessionState.Started.name)
        }
        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, clickIntent, flag)
    }

    fun stopPendingIntent(context: Context): PendingIntent =
        createServicePendingIntent(
            context,
            ACTION_SERVICE_STOP,
            STOP_REQUEST_CODE,
            FocusSessionState.Stopped
        )

    fun resumePendingIntent(context: Context): PendingIntent =
        createServicePendingIntent(
            context,
            ACTION_SERVICE_RESUME,
            RESUME_REQUEST_CODE,
            FocusSessionState.Started
        )

    fun pausePendingIntent(context: Context): PendingIntent =
        createServicePendingIntent(
            context,
            ACTION_SERVICE_PAUSE,
            PAUSE_REQUEST_CODE,
            FocusSessionState.Paused
        )

    fun cancelPendingIntent(context: Context): PendingIntent =
        createServicePendingIntent(
            context,
            ACTION_SERVICE_CANCEL,
            CANCEL_REQUEST_CODE,
            FocusSessionState.Canceled
        )

    fun confirmPendingIntent(context: Context): PendingIntent =
        createServicePendingIntent(
            context,
            ACTION_SERVICE_CONFIRM,
            CLICK_REQUEST_CODE,
            FocusSessionState.Confirming
        )

    fun triggerForegroundService(context: Context, action: String, focusMinutes: Int = 0) {
        val intent = Intent(context, FocusSessionService::class.java).apply {
            this.action = action
            if (focusMinutes > 0) {
                putExtra("FOCUS_MINUTES", focusMinutes)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
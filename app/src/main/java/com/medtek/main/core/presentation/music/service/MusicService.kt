package com.medtek.main.core.presentation.music.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service() {
    @Inject
    lateinit var exoPlayer: ExoPlayer

    private val binder = MusicBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        val resourceId = intent?.getIntExtra("RESOURCE_ID", -1)

        when (action) {
            ACTION_PLAY -> if (resourceId != null && resourceId != -1) play(resourceId)
            ACTION_STOP -> stop()
        }

        return START_NOT_STICKY
    }

    private fun play(resourceId: Int) {
        val uri = "android.resource://${packageName}/${resourceId}"
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    private fun stop() {
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
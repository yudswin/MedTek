package com.medtek.main.core.presentation.music

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.medtek.main.core.presentation.music.service.MusicService
import com.medtek.main.core.presentation.music.service.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    musicService: MusicService
) {
    val tracks = listOf(
        Track.track_one,
        Track.track_two,
        Track.track_three,
        Track.track_four,
        Track.track_five
    )

    val currentTrackIndex = remember { mutableStateOf(0) }
    val isPlaying = remember { mutableStateOf(false) }

    fun playTrack(index: Int) {
        val intent = Intent(musicService, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra("RESOURCE_ID", tracks[index].id)
        }
        musicService.startService(intent)
        isPlaying.value = true
    }

    fun stopTrack() {
        val intent = Intent(musicService, MusicService::class.java).apply {
            action = MusicService.ACTION_STOP
        }
        musicService.startService(intent)
        isPlaying.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box()
        Text(
            text = if (isPlaying.value) "Now Playing: Track ${currentTrackIndex.value + 1}" else "Stopped",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = {
                currentTrackIndex.value = (currentTrackIndex.value - 1 + tracks.size) % tracks.size
                playTrack(currentTrackIndex.value)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowLeft,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Back"
                )
            }
            Button(onClick = {
                if (isPlaying.value) {
                    stopTrack()
                } else {
                    playTrack(currentTrackIndex.value)
                }
            }) {
                Icon(
                    imageVector = if (isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = if (isPlaying.value) "Pause" else "Play"
                )
            }
            Button(onClick = {
                currentTrackIndex.value = (currentTrackIndex.value + 1) % tracks.size
                playTrack(currentTrackIndex.value)
            }) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowRight,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Next"
                )
            }
        }


    }
}
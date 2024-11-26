package com.medtek.main.core.presentation.music.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MusicDropdownBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Music",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Image(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "dropdown"
            )
        }
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Account",
            modifier = Modifier.size(50.dp)
        )
    }
}
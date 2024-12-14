package com.medtek.main.core.presentation.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ActionBar(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ){
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Account",
                modifier = Modifier
                    .size(50.dp)
            )
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Account",
                modifier = Modifier
                    .size(50.dp)
                    .offset(x = 340.dp)
            )
        }
    }
}
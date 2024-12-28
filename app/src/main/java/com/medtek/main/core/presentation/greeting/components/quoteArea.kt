package com.medtek.main.core.presentation.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun QuoteArea(quote: String = "This is a sample quote.",
              author: String = "Author Name") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quote,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Medium),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp),
            maxLines = 4
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "- $author",
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Light),
            color = Color.Black.copy(alpha = 0.7f)
        )
    }
}
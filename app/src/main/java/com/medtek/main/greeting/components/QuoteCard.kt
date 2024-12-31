package com.medtek.main.greeting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.medtek.main.data.local.entities.Quote


@Composable
fun QuoteCard(quote: Quote) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Quote of the Day", style = MaterialTheme.typography.titleLarge)
            Text("Quote: ${quote.quote}", style = MaterialTheme.typography.bodyLarge)
            Text("Author: ${quote.author}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

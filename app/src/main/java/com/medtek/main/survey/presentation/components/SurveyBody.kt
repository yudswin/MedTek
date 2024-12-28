package com.medtek.main.survey.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import com.medtek.main.ui.theme.AppTheme

@Composable
fun SurveyBody() {
    var options = listOf<String>(
        "Option 1", "Option 2", "Option 3", "Option 4"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(options) { opt ->
            OptionItem(opt)
        }
    }
}

@Composable
fun OptionItem(option: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { /* Handle Selection */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = false, onClick = { /* Handle Selection */ })
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = option, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Preview
@Composable
fun PreviewSurveyBody() {
    AppTheme {
        SurveyBody()
    }
}

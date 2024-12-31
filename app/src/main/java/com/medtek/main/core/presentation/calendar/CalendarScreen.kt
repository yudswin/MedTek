package com.medtek.main.core.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.core.presentation.home.components.calendar.CalendarView
import com.medtek.main.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//@Preview
@Composable
fun CalendarScreen() {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMMM")
    val formattedDate = currentDate.format(formatter)
    val selectedDate = remember { mutableStateOf(currentDate) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Calendar Screen")
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Graph / Infographic",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$formattedDate",
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                CalendarView(currentDate, selectedDate.value) { date ->
                    selectedDate.value = date
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewCalendarView() {
    AppTheme {
        CalendarScreen()
    }
}
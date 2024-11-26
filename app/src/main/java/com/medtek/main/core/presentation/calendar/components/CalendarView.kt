package com.medtek.main.core.presentation.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun CalendarView(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val yearMonth = YearMonth.from(currentDate)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek.value % 7

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(firstDayOfMonth) {
            Box(modifier = Modifier.height(40.dp))
        }
        items(daysInMonth) { day ->
            val date = yearMonth.atDay(day + 1)
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (date == selectedDate) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.primary
                    )
                    .clickable { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${day + 1}",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
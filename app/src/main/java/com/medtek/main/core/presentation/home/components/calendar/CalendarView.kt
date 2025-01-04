package com.medtek.main.core.presentation.home.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun CalendarView(
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    userProgress: Map<LocalDate, Float>
) {
    val yearMonth: YearMonth = YearMonth.from(currentDate)
    val daysInMonth: Int = yearMonth.lengthOfMonth()
    val firstDayOfMonth: Int = yearMonth.atDay(1).dayOfWeek.value % 7
    val dayTitle = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Box(
        modifier = Modifier
            .shadow(20.dp)
            .height(300.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(dayTitle) { title ->
                CalendarTitle(title)
            }
            items(firstDayOfMonth) {
                CalendarItemEmpty(backgroundColor = MaterialTheme.colorScheme.primary)
            }
            items(daysInMonth) { day ->
                val date = yearMonth.atDay(day + 1)
                val progress = userProgress[date] ?: 0f
                val isCurrentDay = date == currentDate
                Box(
                    modifier = Modifier.clickable { onDateSelected(date) },
                    contentAlignment = Alignment.Center
                ) {
                    CalendarItem(day = day, progress = progress, isCurrentDay = isCurrentDay)
                }
            }
        }
    }
}



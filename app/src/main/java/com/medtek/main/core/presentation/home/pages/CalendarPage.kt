package com.medtek.main.core.presentation.home.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medtek.main.core.presentation.home.components.calendar.CalendarView
import com.medtek.main.core.presentation.home.components.calendar.HabitCard
import com.medtek.main.core.presentation.home.components.calendar.MonthSelector
import com.medtek.main.core.presentation.home.components.streak.InfoGraphic
import com.medtek.main.core.presentation.home.viewmodel.CalendarViewModel
import com.medtek.main.data.local.entities.Habit
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarPage(
    viewModel: CalendarViewModel = hiltViewModel()
) {

    // Calendar Variable
    val currentDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedDate = remember { mutableStateOf(currentDate.value) }
    val userProgress by viewModel.userProgress
    val isLoading by viewModel.loading
    val errorMessage by viewModel.error

    val habits by viewModel.habits.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()


    // Infographic Variable
    val currentStreak by viewModel.currentStreak
    val longestStreak by viewModel.longestStreak
    val totalHabits by viewModel.totalHabitsDone
    val currentProgress by viewModel.currentDayProcess


    // Load user progress for the current month
    LaunchedEffect(currentDate.value) {
        viewModel.getUserProgressForCurrentMonth()
    }

    LaunchedEffect(userProgress) {
        viewModel.fetchUserMetrics()
    }

    // Fetch habits for the current date initially
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getHabitsForDate(currentDate.value)
            viewModel.loadCurrentDayProgress()
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header content (e.g., loading or error messages)
        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        item {
            InfoGraphic(
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                totalHabits = totalHabits,
                currentProgress = currentProgress
            )
        }
        item {
            MonthSelector(currentDate.value) { newDate ->
                currentDate.value = newDate
            }
        }
        item {
            CalendarView(
                currentDate = currentDate.value,
                selectedDate = selectedDate.value,
                onDateSelected = { date ->
                    selectedDate.value = date
                    coroutineScope.launch {
                        viewModel.getHabitsForDate(date)
                        viewModel.loadCurrentDayProgress()
                    }
                },
                userProgress = userProgress
            )
        }
        habitList(habits, selectedDate.value)
    }
}

fun LazyListScope.habitList(
    habits: List<Habit>,
    selectedDate: LocalDate
) {
    if (habits.isEmpty()) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No habits for $selectedDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    } else {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Journey: $selectedDate",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        items(habits.size) { index ->
            HabitCard(habits[index])
        }
    }
}






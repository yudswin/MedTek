package com.medtek.main.core.presentation.home.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medtek.main.core.presentation.home.components.progresscard.HabitCard
import com.medtek.main.core.presentation.home.components.weeklybar.WeeklyBar
import com.medtek.main.core.presentation.home.viewmodel.DailyPageViewModel
import java.time.LocalDate

@Composable
fun DailyPage(
    viewModel: DailyPageViewModel = hiltViewModel()
) {
    val loadingState by viewModel.loadingState
    val errorState by viewModel.errorState
    val weekScheduled by viewModel.weekScheduled
    val weekProgress by viewModel.weekProgress
    val habitsLoadingState by viewModel.habitsLoadingState

    val dates = listOf(
        LocalDate.now().minusDays(3),
        LocalDate.now().minusDays(2),
        LocalDate.now().minusDays(1),
        LocalDate.now(),
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(2),
        LocalDate.now().plusDays(3),
    )

    val pagerState = rememberPagerState(initialPage = 3, pageCount = { dates.size })

    LaunchedEffect(viewModel) {
        viewModel.loadWeekPlan()
        viewModel.userId.value?.let {
            viewModel.loadScheduled()
        }
    }

    Scaffold(
        topBar = {
            WeeklyBar(
                pageState = pagerState,
                dates = dates,
                progressMap = weekProgress
            )
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState
        ) { pageIndex ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (loadingState) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (errorState != null) {
                    Text(
                        text = errorState ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    val date = dates[pageIndex]
                    val habits = weekScheduled[date] ?: emptyList()
                    val isLoading = habitsLoadingState[date] != false

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (habits.isEmpty()) {
                        Text(
                            text = "No habits to display for $date",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 8.dp, horizontal = 6.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 6.dp)
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(habits.count()) { index ->
                                HabitCard(habits[index]) { trackingId, amount ->
                                    viewModel.updateHabitProgress(trackingId, amount)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



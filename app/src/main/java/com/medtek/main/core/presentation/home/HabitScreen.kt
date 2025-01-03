package com.medtek.main.core.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.core.presentation.home.components.TopHabitBar
import com.medtek.main.core.presentation.home.pages.CalendarPage
import com.medtek.main.core.presentation.home.pages.DailyPage
import com.medtek.main.core.presentation.home.viewmodel.DailyPageViewModel
import com.medtek.main.core.presentation.home.viewmodel.HabitViewModel
import kotlinx.coroutines.launch

@Composable
fun HabitScreen(
    initialPage: Int? = null,
    viewModel: HabitViewModel = hiltViewModel(),
    navController: NavController,
    outterNavController: NavController
) {
    val userIdState = viewModel.userId.value
    val loadingState = viewModel.loadingState.value

    LaunchedEffect(userIdState) {
        viewModel.getUserId()
    }

    if (loadingState) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (userIdState == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Failed to fetch user ID. Please try again.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    } else {
        Scaffold(
            topBar = { TopHabitBar(outterNavController) },
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pagerState = rememberPagerState(
                        initialPage = initialPage ?: 0,
                        pageCount = { 2 }
                    )
                    val coroutineScope = rememberCoroutineScope()

                    TabRow(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = MaterialTheme.colorScheme.onPrimary,
                                height = 6.dp
                            )
                        }
                    ) {
                        Tab(
                            selected = pagerState.currentPage == 0,
                            text = { Text("Home") },
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        )
                        Tab(
                            selected = pagerState.currentPage == 1,
                            text = { Text("Calendar") },
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        )
                    }
                    val dailyViewModel: DailyPageViewModel = hiltViewModel()
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> DailyPage(viewModel = dailyViewModel)
                            1 -> CalendarPage()
                        }
                    }
                }
            }
        }
    }
}





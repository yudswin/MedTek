package com.medtek.main.core.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.medtek.main.core.presentation.home.components.TopHabitBar
import com.medtek.main.core.presentation.home.pages.CalendarPage
import com.medtek.main.core.presentation.home.pages.DailyPage
import kotlinx.coroutines.launch

@Composable
fun HabitScreen(
    initialPage: Int? = null,
    isEmpty: Boolean = false,
    viewModel: HabitViewModel
) {
    Scaffold(
        topBar = { TopHabitBar() },
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

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> DailyPage(isEmpty = isEmpty, viewModel = viewModel)
                        1 -> CalendarPage()
                    }
                }
            }
        }
    }
}



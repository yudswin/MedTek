package com.medtek.main.core.presentation.home.components.weeklybar

import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun WeeklyBar(
    pageState: PagerState,
    dates: List<LocalDate>,
    progressMap: Map<LocalDate, Float>
) {
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pageState.currentPage
    ) {
        dates.forEachIndexed { index, date ->
            val progress = progressMap[date] ?: 0f
            Tab(
                selected = pageState.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        pageState.animateScrollToPage(index)
                    }
                }
            ) {
                DateTab(
                    date = date,
                    progress = progress,
                    isSelected = pageState.currentPage == index
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewFixedTabBar() {
//    AppTheme {
//        WeeklyBar()
//    }
//}

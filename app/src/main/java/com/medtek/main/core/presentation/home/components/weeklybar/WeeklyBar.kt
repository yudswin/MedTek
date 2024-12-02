package com.medtek.main.core.presentation.home.components.weeklybar

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.medtek.main.ui.theme.AppTheme
import java.time.LocalDate

@Composable
fun WeeklyBar() {
    val pageState = rememberPagerState(
        pageCount = { 7 },
        initialPage = 3
    )

    val dates = listOf(
        LocalDate.now().minusDays(3),
        LocalDate.now().minusDays(2),
        LocalDate.now().minusDays(1),
        LocalDate.now(),
        LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(2),
        LocalDate.now().plusDays(3),
    )


    TabRow(
        selectedTabIndex = pageState.currentPage
    ) {
        dates.forEachIndexed { index, date ->
            Tab(
                selected = pageState.currentPage == index,
                onClick = { }
            ) {
                DateTab(
                    date = date,
                    progress = (index + 1) * 0.2f, // Example progress values
                    isSelected = pageState.currentPage == index
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFixedTabBar() {
    AppTheme() {
        WeeklyBar()
    }
}

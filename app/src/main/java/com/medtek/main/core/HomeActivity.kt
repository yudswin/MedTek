package com.medtek.main.core

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.medtek.main.core.navigation.BottomNavBar
import com.medtek.main.core.navigation.BottomNavGraph
import com.medtek.main.ui.theme.AppTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(isEmpty: Boolean = false, initPage: Int = 0) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        BottomNavGraph(navController = navController, paddingValues = innerPadding, isEmpty, initPage)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen()
    }
}

@Preview
@Composable
fun PreviewHomeScreenCalendar() {
    AppTheme {
        HomeScreen(initPage = 1)
    }
}

@Preview
@Composable
fun PreviewHomeScreenEmpty() {
    AppTheme {
        HomeScreen(isEmpty = true)
    }
}



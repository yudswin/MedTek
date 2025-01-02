package com.medtek.main.core

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.medtek.main.core.navigation.BottomNavBar
import com.medtek.main.core.navigation.BottomNavGraph

@Composable
fun HomeScreen(
    navController: NavHostController,
    initPage: Int = 0,
) {
    val innerNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        bottomBar = { BottomNavBar(navController = innerNavController) }
    ) { innerPadding ->
        BottomNavGraph(
            outterNavController = navController,
            navController = innerNavController,
            paddingValues = innerPadding,
            initPage
        )
    }
}



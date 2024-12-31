package com.medtek.main.survey.presentation.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.medtek.main.R
import com.medtek.main.survey.viewmodel.SurveyViewModel
import com.medtek.main.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeUsagePage(
    navController: NavController,
    title: String = "How much time do you spend on your phone per day",
    nextPage: String? = null,
    viewModel: SurveyViewModel? = hiltViewModel(),
) {
    var sliderPosition by remember { mutableIntStateOf(0) }
    var selectedValue = listOf(sliderPosition.toString())
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    nextPage?.let { navController.navigate(it) }
                    viewModel?.addSurveyRequest(label = "timeUsingPhone", values = selectedValue)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    Text("Continue")
                    Icon(Icons.Default.PlayArrow, contentDescription = "Next")
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .height(12.dp)
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Image(
                painter = painterResource(id = R.drawable.time_usingphone),
                contentDescription = "My Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Row {
                Text(
                    text = "$sliderPosition Hour(s)",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Slider(
                    value = sliderPosition.toFloat(),
                    onValueChange = { sliderPosition = it.toInt() },
                    valueRange = 0f..24f,
                    steps = 24,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewThisTime() {
    AppTheme {
        TimeUsagePage(
            navController = rememberNavController(),
            viewModel = null
        )
    }
}

package com.medtek.main.survey.presentation.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.medtek.main.data.remote.models.survey.ConfigValue
import com.medtek.main.survey.presentation.components.ChipContainer
import com.medtek.main.survey.viewmodel.SurveyViewModel
import com.medtek.main.ui.theme.AppTheme

private fun convertToReadableString(input: String): String {
    return input.replace(Regex("([A-Z])"), " $1")
        .trim() // Remove leading/trailing spaces
        .lowercase() // Convert to lowercase
}


@Composable
fun SurveyPage(
    title: String = "SportFields",
    options: List<ConfigValue>? = null,
    navController: NavController,
    viewModel: SurveyViewModel? = hiltViewModel(),
    nextPage: String? = null
) {
    val selectedItems = remember { mutableListOf<String>() }
    val pageTitle = "What is your ${convertToReadableString(title)}?"

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedItems.size >= 2) {
                        nextPage?.let { navController.navigate(it) }
                        viewModel?.addSurveyRequest(label = title, values = selectedItems)
                    } else {
                        Toast.makeText(
                            context,
                            "Please select at least 2 items.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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
                text = pageTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            ChipContainer(altFields = options) { newSelection ->
                selectedItems.clear()
                selectedItems.addAll(newSelection)
                Log.d("ChipContainer", "Selected items updated: $selectedItems")
            }
        }
    }
}

@Preview
@Composable
fun PreviewSurvey() {
    AppTheme {
        SurveyPage(
            navController = rememberNavController(),
            viewModel = null
        )
    }
}
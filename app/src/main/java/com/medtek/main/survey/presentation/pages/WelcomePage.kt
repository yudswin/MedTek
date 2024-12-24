package com.medtek.main.survey.presentation.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.survey.viewmodel.WelcomeViewModel
import com.medtek.main.ui.theme.AppTheme


@Composable
fun WelcomePage(
    navController: NavController?,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    var email by remember {
        mutableStateOf("")
    }
    val loadingState = viewModel.loadingState
    val signInState by viewModel.signInState
    val errorState by viewModel.errorState
    val loadError = viewModel.loadError
    val context = LocalContext.current


    val charLimit = 10
    fun validate(text: CharSequence): Boolean {
        return text.length > charLimit
    }

    LaunchedEffect(signInState) {
        if (signInState != null) {
            navController?.navigate("auth")
        }
    }

    LaunchedEffect(errorState) {
        errorState?.let {
            Toast.makeText(context, loadError.value, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Welcome to MedTek",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Enter your email",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("YourEmail@mail.com") },
                shape = MaterialTheme.shapes.small,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { viewModel.signIn(email) },
                shape = MaterialTheme.shapes.small,
                enabled = !loadingState.value,
            ) {
                if (loadingState.value) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    Text(
                        text = "Enter",
                        modifier = Modifier.padding(horizontal = 64.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewWelcomePage() {
    AppTheme {
        WelcomePage(navController = null)
    }
}
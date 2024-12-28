package com.medtek.main.survey.presentation.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.medtek.main.survey.viewmodel.WelcomeViewModel
import com.medtek.main.ui.theme.AppTheme


@Composable
fun AuthPage(
    navController: NavController?,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    // ViewModel Values
    val loadingState = viewModel.loadingState
    val authState by viewModel.authState
    val errorState by viewModel.errorState
    val loadError = viewModel.loadError
    val context = LocalContext.current

    // Input Values
    val otpLength = 6
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isCompleted by remember { mutableStateOf(false) }
    var otpString by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState != null) {
            navController?.navigate("survey_screen")
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
                text = "Check your email for OTP",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (i in 0 until otpLength) {
                    OutlinedTextField(
                        value = otpValues[i],
                        onValueChange = { newValue ->
                            when {
                                // Add a new digit
                                newValue.length == 1 && newValue.all { it.isDigit() } -> {
                                    otpValues[i] = newValue
                                    if (i < otpLength - 1) {
                                        focusRequesters[i + 1].requestFocus()
                                    } else {
                                        // Hide the keyboard if all fields are filled
                                        if (otpValues.all { it.isNotEmpty() }) {
                                            otpString = otpValues.joinToString("")
                                            isCompleted = true
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    }
                                }

                                // Remove digit (backspace)
                                newValue.isEmpty() -> {
                                    otpValues[i] = ""
                                    if (i > 0) {
                                        focusRequesters[i - 1].requestFocus()
                                    }
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize
                        ),
                        shape = MaterialTheme.shapes.small,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .width(40.dp)
                            .height(50.dp)
                            .focusRequester(focusRequesters[i])
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.auth(code = otpString) },
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
fun PreviewAuthPage() {
    AppTheme {
        AuthPage(navController = null)
    }
}
package com.medtek.main.survey.presentation.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medtek.main.ui.theme.AppTheme

@Composable
fun TestPage() {
    val otpLength = 6
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (i in 0 until otpLength) {
                    OutlinedTextField(
                        value = otpValues[i],
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                otpValues[i] = newValue
                                if (newValue.isNotEmpty() && i < otpLength - 1) {
                                    focusRequesters[i + 1].requestFocus()
                                }
                            } else if (newValue.isEmpty() && i > 0) {
                                focusRequesters[i - 1].requestFocus()
                            }
                        },
                        placeholder = { Text("0") },
                        shape = MaterialTheme.shapes.small,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .width(40.dp)
                            .focusRequester(focusRequesters[i])
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewPage() {
    AppTheme {
        TestPage()
    }
}
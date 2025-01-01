package com.medtek.main.survey.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.medtek.main.data.remote.models.survey.ConfigValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipContainer(
    altFields: List<ConfigValue>? = null,
    onSelectionChanged: (List<String>) -> Unit
) {
    val selectedItems = remember { mutableStateListOf<String>() }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        altFields?.forEach { field ->
            SelectableChip(
                label = field.optionName,
                icon = field.icon,
                isSelected = selectedItems.contains(field.optionName),
                onSelectedChange = { isSelected ->
                    if (isSelected) {
                        selectedItems.add(field.optionName)
                    } else {
                        selectedItems.remove(field.optionName)
                    }
                    Log.d("ChipContainer", "Selected items: $selectedItems")
                    onSelectionChanged(selectedItems)
                }
            )
        }
    }
}




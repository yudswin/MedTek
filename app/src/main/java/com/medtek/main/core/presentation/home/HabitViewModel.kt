package com.medtek.main.core.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HabitViewModel : ViewModel() {
    private val _isEmptyHabit = MutableStateFlow(false)
    val isEmptyHabit: StateFlow<Boolean> = _isEmptyHabit

    private val _initialPage = MutableStateFlow(0)
    val initialPage: StateFlow<Int> = _initialPage

    fun updateHabitState(isEmpty: Boolean) {
        _isEmptyHabit.value = isEmpty
    }
}
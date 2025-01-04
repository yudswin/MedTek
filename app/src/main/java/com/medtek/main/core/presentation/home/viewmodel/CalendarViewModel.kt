package com.medtek.main.core.presentation.home.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.data.repository.core.HabitRepository
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userProgress = mutableStateOf<Map<LocalDate, Float>>(emptyMap())
    val userProgress: State<Map<LocalDate, Float>> = _userProgress

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    // InfoGraphic - Streak

    private val _currentStreak = mutableIntStateOf(0)
    val currentStreak: State<Int> = _currentStreak

    private val _longestStreak = mutableIntStateOf(0)
    val longestStreak: State<Int> = _longestStreak

    private val _totalHabitsDone = mutableIntStateOf(0)
    val totalHabitsDone: State<Int> = _totalHabitsDone

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _currentDayProgress = mutableFloatStateOf(0f)
    val currentDayProcess: State<Float> = _currentDayProgress

    private val currentDay = LocalDate.now()
    private val _errorState = mutableStateOf<String?>(null)


    private val _userId = mutableStateOf<String?>(null)
    val userId: State<String?> = _userId

    init {
        getUserId()
    }

    fun getUserId() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("HabitViewModel", "Getting user ID")
            try {
                userRepository.getUserId().also {
                    if (it is Resource.Success) {
                        _userId.value = it.data
                        Log.d("HabitViewModel", "User ID fetched successfully: ${it.data}")
                    } else if (it is Resource.Error) {
                        _errorState.value = it.message
                        Log.e("HabitViewModel", "Error fetching user ID: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Exception fetching user ID", e)
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
                Log.d("HabitViewModel", "Loading state set to false")
            }
        }
    }

    // Calendar
    fun getUserProgressForCurrentMonth() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val progressMap = mutableMapOf<LocalDate, Float>()
                val currentDate = LocalDate.now()
                val startDate = currentDate.withDayOfMonth(1)
                val endDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth())

                var current = startDate

                while (!current.isAfter(endDate)) {
                    when (val result = habitRepository.getHabitsForDate(current)) {
                        is Resource.Success -> {
                            val habits = result.data ?: emptyList()
                            if (habits.isNotEmpty()) {
                                val totalProgress = habits.sumOf { habit ->
                                    val progress = habit.progress.toFloat()
                                    val goal = habit.goal.toFloat()
                                    if (goal > 0) (progress / goal).toDouble() else 0.0
                                }
                                val dayProgress = (totalProgress / habits.size).toFloat()
                                progressMap[current] = dayProgress
                            } else {
                                progressMap[current] = 0f
                            }
                        }

                        is Resource.Error -> {
                            _error.value = result.message
                        }
                    }
                    current = current.plusDays(1)
                }

                _userProgress.value = progressMap
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCurrentDayProgress() {
        val date = currentDay
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                when (val result = habitRepository.getProgressForDate(date)) {
                    is Resource.Success -> {
                        _currentDayProgress.floatValue = result.data ?: 0f
                    }

                    is Resource.Error -> {
                        _error.value = result.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun getHabitsForDate(date: LocalDate) {
        viewModelScope.launch {
            // Fetch habits for the given date from the repository
            val result = habitRepository.getHabitsForDate(date)
            if (result is Resource.Success) {
                _habits.value = result.data ?: emptyList()
            } else {
                // Handle error
            }
        }
    }

    // Infographic
    fun fetchUserMetrics() {
        if (_userId.value != null) {
            val id = _userId.value.toString()
            fetchCurrentStreak(id)
            fetchLongestStreak(id)
            fetchTotalHabitsDone(id)
            loadCurrentDayProgress()
        }
    }

    private fun fetchCurrentStreak(userId: String) {
        viewModelScope.launch {
            try {
                when (val result = habitRepository.getCurrentStreak(userId)) {
                    is Resource.Success -> {
                        val currentStreak = result.data ?: 0
                        _currentStreak.intValue = currentStreak
                        updateCurrentStreak(userId, currentStreak)
                    }

                    is Resource.Error -> _errorState.value = result.message
                }
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }


    private fun updateCurrentStreak(userId: String, currentStreak: Int) {
        viewModelScope.launch {
            try {
                when (val result = userRepository.updateCurrentStreak(userId, currentStreak)) {
                    is Resource.Success -> {
                        // Handle success
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message
                    }
                }
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

    private fun fetchTotalHabitsDone(userId: String) {
        viewModelScope.launch {
            try {
                when (val result = habitRepository.getTotalHabitsDone(userId)) {
                    is Resource.Success -> _totalHabitsDone.intValue = result.data ?: 0
                    is Resource.Error -> _errorState.value = result.message
                }
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

    private fun fetchLongestStreak(userId: String) {
        viewModelScope.launch {
            try {
                val longestStreakResource = userRepository.getUserLongestStreak(userId)
                if (longestStreakResource is Resource.Success) {
                    val fetchedLongestStreak = longestStreakResource.data ?: 0
                    Log.d(
                        "ViewModel",
                        "Fetched longest streak: $fetchedLongestStreak for user $userId"
                    )
                    _longestStreak.intValue = fetchedLongestStreak
                } else if (longestStreakResource is Resource.Error) {
                    Log.e(
                        "ViewModel",
                        "Error fetching longest streak: ${longestStreakResource.message}"
                    )
                    _errorState.value = longestStreakResource.message
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Exception fetching longest streak", e)
                _errorState.value = e.message
            }
        }
    }
}

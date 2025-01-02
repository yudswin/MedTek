package com.medtek.main.core.presentation.home.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.DayPlan
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.data.repository.core.HabitRepository
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.Int
import kotlin.collections.List
import kotlin.collections.Map

@HiltViewModel
class DailyPageViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _userId = mutableStateOf<String?>(null)
    val userId: State<String?> = _userId

    private val _dayPlans = MutableStateFlow<List<DayPlan>>(emptyList())
    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState

    private val _updateProgressState = mutableStateOf<Resource<Unit>?>(null)
    val updateProgressState: State<Resource<Unit>?> = _updateProgressState

    init {
        getUserId()
    }

    // Init
    private val _weekScheduled = mutableStateOf<Map<LocalDate, List<Habit>>>(
        mapOf(
            LocalDate.now().minusDays(3) to emptyList<Habit>(),
            LocalDate.now().minusDays(2) to emptyList<Habit>(),
            LocalDate.now().minusDays(1) to emptyList<Habit>(),
            LocalDate.now() to emptyList<Habit>(),
            LocalDate.now().plusDays(1) to emptyList<Habit>(),
            LocalDate.now().plusDays(2) to emptyList<Habit>(),
            LocalDate.now().plusDays(3) to emptyList<Habit>()
        )
    )
    val weekScheduled: State<Map<LocalDate, List<Habit>>> = _weekScheduled

    private val _weekPlan = mutableStateOf<Map<LocalDate, Int?>>(
        mapOf(
            LocalDate.now().minusDays(3) to null,
            LocalDate.now().minusDays(2) to null,
            LocalDate.now().minusDays(1) to null,
            LocalDate.now() to null,
            LocalDate.now().plusDays(1) to null,
            LocalDate.now().plusDays(2) to null,
            LocalDate.now().plusDays(3) to null
        )
    )

    private val _weekProgress = mutableStateOf<Map<LocalDate, Float>>(
        mapOf(
            LocalDate.now().minusDays(3) to 0f,
            LocalDate.now().minusDays(2) to 0f,
            LocalDate.now().minusDays(1) to 0f,
            LocalDate.now() to 0f,
            LocalDate.now().plusDays(1) to 0f,
            LocalDate.now().plusDays(2) to 0f,
            LocalDate.now().plusDays(3) to 0f
        )
    )
    val weekProgress: State<Map<LocalDate, Float>> = _weekProgress

    fun calculateProgressForWeek() {
        val progressMap = mutableMapOf<LocalDate, Float>()
        _weekScheduled.value.forEach { (date, habits) ->
            if (habits.isNotEmpty()) {
                val totalProgress = habits.sumOf { habit ->
                    val progress = habit.progress.toFloat()
                    val goal = habit.goal.toFloat()
                    if (goal > 0) (progress / goal).toDouble() else 0.0
                }
                val dayProgress = (totalProgress / habits.size).toFloat()
                progressMap[date] = dayProgress
            } else {
                progressMap[date] = 0f
            }
        }
        _weekProgress.value = progressMap
    }

    private val _habitsLoadingState = mutableStateOf<Map<LocalDate, Boolean>>(
        mapOf(
            LocalDate.now().minusDays(3) to true,
            LocalDate.now().minusDays(2) to true,
            LocalDate.now().minusDays(1) to true,
            LocalDate.now() to true,
            LocalDate.now().plusDays(1) to true,
            LocalDate.now().plusDays(2) to true,
            LocalDate.now().plusDays(3) to true
        )
    )
    val habitsLoadingState: State<Map<LocalDate, Boolean>> = _habitsLoadingState


    fun loadScheduled() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                val weekScheduledMap = mutableMapOf<LocalDate, List<Habit>>()
                val loadingMap = _habitsLoadingState.value.toMutableMap()

                _weekPlan.value.forEach { (date, dayPlanId) ->
                    if (dayPlanId != null) {
                        try {
                            val habits = habitRepository.getHabitsByDayPlanId(dayPlanId)
                            weekScheduledMap[date] = habits.data as List<Habit>
                        } catch (e: Exception) {
                            Log.e("HabitViewModel", "Error fetching habits for day $date", e)
                            weekScheduledMap[date] = emptyList()
                        }
                    } else {
                        weekScheduledMap[date] = emptyList()
                    }
                    loadingMap[date] = false
                }

                _weekScheduled.value = weekScheduledMap
                _habitsLoadingState.value = loadingMap

                calculateProgressForWeek()
            } catch (e: Exception) {
                _errorState.value = e.message
                Log.e("HabitViewModel", "Error loading scheduled habits", e)
            } finally {
                _loadingState.value = false
            }
        }
    }


    fun loadWeekPlan() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                val userId = _userId.value.toString()
                when (val result = habitRepository.getAllDayPlansByUserId(userId)) {
                    is Resource.Success -> {
                        val dayPlans = result.data
                        val weekPlanMap = dayPlans?.associate { dayPlan ->
                            LocalDate.parse(dayPlan.date) to dayPlan.id
                        }
                        if (weekPlanMap != null) {
                            _weekPlan.value = weekPlanMap
                        }
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message
                        Log.e("HabitViewModel", "Error fetching day plans: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                _errorState.value = e.message
                Log.e("HabitViewModel", "Error loading week plan", e)
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun getUserId() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("DailyPageViewModel", "Getting user ID")
            try {
                userRepo.getUserId().also {
                    if (it is Resource.Success) {
                        _userId.value = it.data
                        loadDayPlans()
                        Log.d("DailyPageViewModel", "User ID fetched successfully: ${it.data}")
                    } else if (it is Resource.Error) {
                        _errorState.value = it.message
                        Log.e("DailyPageViewModel", "Error fetching user ID: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("DailyPageViewModel", "Exception fetching user ID", e)
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
                Log.d("DailyPageViewModel", "Loading state set to false")
            }
        }
    }

    fun loadDayPlans() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("DailyPageViewModel", "Loading day plans")
            val response = try {
                if (_userId.value == null) {
                    userRepo.getUserId().also {
                        if (it is Resource.Success) {
                            _userId.value = it.data
                            Log.d("DailyPageViewModel", "User ID fetched successfully: ${it.data}")
                        } else if (it is Resource.Error) {
                            _errorState.value = it.message
                            Log.e("DailyPageViewModel", "Error fetching user ID: ${it.message}")
                        }
                    }
                }

                habitRepository.getAllDayPlansByUserId(_userId.value ?: "")
            } catch (e: Exception) {
                Log.e("DailyPageViewModel", "Exception loading day plans", e)
                _errorState.value = e.message
                Resource.Error(e.message ?: "An unexpected error occurred")
            } finally {
                _loadingState.value = false
                Log.d("DailyPageViewModel", "Loading state set to false")
            }

            if (response is Resource.Success) {
                _dayPlans.value = response.data ?: emptyList()
                Log.d("DailyPageViewModel", "Day plans loaded successfully")
            } else if (response is Resource.Error) {
                _errorState.value = response.message
                Log.e("DailyPageViewModel", "Error loading day plans: ${response.message}")
            }
        }
    }

    fun getHabitsForDate(date: String): List<Habit> {
        var habits = emptyList<Habit>()
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("DailyPageViewModel", "Loading habits for date: $date")
            val response = try {
                habitRepository.getDayPlansByDate(date)
            } catch (e: Exception) {
                Log.e("DailyPageViewModel", "Exception loading habits", e)
                _errorState.value = e.message
                Resource.Error(e.message ?: "An unexpected error occurred")
            } finally {
                _loadingState.value = false
                Log.d("DailyPageViewModel", "Loading state set to false")
            }

            if (response is Resource.Success) {
                habits = response.data?.flatMap { dayPlan ->
                    habitRepository.getHabitsByDayPlanId(dayPlan.id).data ?: emptyList()
                } ?: emptyList()
                Log.d("DailyPageViewModel", "Habits loaded successfully")
            } else if (response is Resource.Error) {
                _errorState.value = response.message
                Log.e("DailyPageViewModel", "Error loading habits: ${response.message}")
            }
        }
        return habits
    }

    fun updateHabitProgress(trackingId: String, progressAmount: Int) {
        viewModelScope.launch {
            _loadingState.value = true
            _updateProgressState.value = null
            try {
                val response = habitRepository.updateProgressByHabitId(trackingId, progressAmount)
                _updateProgressState.value = response

                if (response is Resource.Success) {
                    Log.d("DailyPageViewModel", "Habit progress updated successfully")
                    loadScheduled()
                } else if (response is Resource.Error) {
                    Log.e(
                        "DailyPageViewModel",
                        "Error updating habit progress: ${response.message}"
                    )
                }
            } catch (e: Exception) {
                _updateProgressState.value =
                    Resource.Error(e.message ?: "An unexpected error occurred")
                Log.e("DailyPageViewModel", "Exception updating habit progress", e)
            } finally {
                _loadingState.value = false
            }
        }
    }

}
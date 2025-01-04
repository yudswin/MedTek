package com.medtek.main.core.presentation.home.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val userRepo: UserRepository,
) : ViewModel() {

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
//    val errorState: State<String?> = _errorState
//    val loadError = mutableStateOf("")

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
                userRepo.getUserId().also {
                    if (it is Resource.Success) {
                        _userId.value = it.data
                        Log.d("HabitViewModel", "User ID fetched successfully: ${it.data}")
                        checkAndFetchUserPlan(it.data.toString())
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

    fun checkAndFetchUserPlan(userId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d(
                "HabitViewModel",
                "Checking if user has a plan for the current week for userId: $userId"
            )
            try {
                userRepo.hasCurrentWeekPlan(userId).also {
                    if (it is Resource.Success && it.data == false) {
                        Log.d(
                            "HabitViewModel",
                            "User does not have a plan for the current week, fetching new plan"
                        )
                        fetchUserPlan(_userId.value.toString())
                    } else if (it is Resource.Error) {
                        _errorState.value = it.message
                        Log.e("HabitViewModel", "Error checking current week plan: ${it.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Exception checking current week plan", e)
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
                Log.d("HabitViewModel", "Loading state set to false")
            }
        }
    }

    fun fetchUserPlan(userId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("HabitViewModel", "Fetching user rituals for userId: $userId")
            try {
                userRepo.savePlanResponse(userId).also {
                    Log.d("HabitViewModel", "Fetched user rituals successfully")
                }
            } catch (e: Exception) {
                Log.e("HabitViewModel", "Error fetching user rituals", e)
                _errorState.value = e.message
            } finally {
                _loadingState.value = false
                Log.d("HabitViewModel", "Loading state set to false")
            }
        }
    }


}
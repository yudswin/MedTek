package com.medtek.main.core.presentation.home

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
    val errorState: State<String?> = _errorState
    val loadError = mutableStateOf("")

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
            val response = try {
                userRepo.getUserId().also {
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
            fetchUserPlan(_userId.value.toString())
        }
    }

    fun fetchUserPlan(userId: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("HabitViewModel", "Fetching user rituals for userId: $userId")
            val response = try {
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
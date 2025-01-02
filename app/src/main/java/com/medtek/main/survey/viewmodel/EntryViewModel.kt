package com.medtek.main.survey.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.data.repository.survey.AuthRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
//    val errorState: State<String?> = _errorState

    private val _isAuth = MutableStateFlow(false)
    val isAuth: StateFlow<Boolean> = _isAuth

    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser

    init {
        _loadingState.value = true
        checkUserSignIn()
    }

//    val loadError = mutableStateOf("")

    fun checkUserSignIn() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                val result = repository.getUserId()
                when (result) {
                    is Resource.Success -> {
                        if (result.data != null) {
                            _isAuth.value = true
                            val surveyResult = userRepo.isUserDoSurvey(result.data)
                            _isNewUser.value = when (surveyResult) {
                                is Resource.Success -> !surveyResult.data!!
                                is Resource.Error -> true
                            }
                            Log.d(
                                "WelcomeViewModel",
                                "User is already signed in with ID: ${result.data}"
                            )
                        } else {
                            _isAuth.value = false
                            _isNewUser.value = true
                            Log.d("WelcomeViewModel", "No user is signed in")
                        }
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message ?: "Unknown error"
                        _isAuth.value = false
                        _isNewUser.value = true
                        Log.e("WelcomeViewModel", "getUserId: Error - ${result.message}")
                    }
                }
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                _isAuth.value = false
                _isNewUser.value = true
                Log.e("WelcomeViewModel", "getUserId: Exception - ${e.message}")
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun setAuth(status: Boolean) {
        _isAuth.value = status
    }

    fun setNewUser(status: Boolean) {
        _isNewUser.value = status
    }
}

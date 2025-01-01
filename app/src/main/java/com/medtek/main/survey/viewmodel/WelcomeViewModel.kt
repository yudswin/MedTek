package com.medtek.main.survey.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.remote.models.auth.AuthResponse
import com.medtek.main.data.remote.models.auth.LoginResponse
import com.medtek.main.data.remote.services.AuthRequest
import com.medtek.main.data.remote.services.SignInRequest
import com.medtek.main.data.repository.survey.AuthRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signInState = mutableStateOf<LoginResponse?>(null)
    val signInState: State<LoginResponse?> = _signInState

    private val _authState = mutableStateOf<AuthResponse?>(null)
    val authState: State<AuthResponse?> = _authState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState
    val loadError = mutableStateOf("")


    fun signIn(email: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                if (email.isEmpty()) {
                    _errorState.value = "Email can't be empty"
                    _loadingState.value = false
                    return@launch
                }

                val body: SignInRequest = SignInRequest(email = email)
                val result = repository.signIn(body)
                when (result) {
                    is Resource.Success -> {
                        loadError.value = ""
                        _signInState.value = result.data
                        Log.d("WelcomeViewModel", "signIn: Success")
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message ?: "Unknown error"
                        loadError.value = "fetchFields() fetching failed"
                        Log.e("WelcomeViewModel", "signIn: Error - ${result.message}")
                    }
                }

            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
            } finally {
                _loadingState.value = false
                Log.d("WelcomeViewModel", "signIn: finished")
            }
        }
    }

    fun auth(code: String) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                if (code.isEmpty()) {
                    _errorState.value = "Code can't be empty"
                    _loadingState.value = false
                    return@launch
                }

                val userId = _signInState.value?.userId ?: run {
                    val result = repository.getUserId()
                    if (result is Resource.Success) {
                        result.data
                    } else {
                        _errorState.value = "Failed to get user ID"
                        _loadingState.value = false
                        return@launch
                    }
                }

                val body: AuthRequest = AuthRequest(
                    code = code,
                    userId = userId!!
                )
                val result = repository.auth(body)
                when (result) {
                    is Resource.Success -> {
                        loadError.value = ""
                        _authState.value = result.data
                        Log.d("WelcomeViewModel", "auth: Success")
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message ?: "Unknown error"
                        loadError.value = "fetchFields() fetching failed"
                        Log.e("WelcomeViewModel", "auth: Error - ${result.message}")
                    }
                }

            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
            } finally {
                _loadingState.value = false
                Log.d("WelcomeViewModel", "auth: finished")
            }
        }
    }
}
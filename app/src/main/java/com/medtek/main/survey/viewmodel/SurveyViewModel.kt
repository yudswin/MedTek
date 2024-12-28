package com.medtek.main.survey.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.repository.survey.FieldRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: FieldRepository
) : ViewModel() {

    private val _fieldState = mutableStateOf<List<Field?>>(listOf())
    val fieldState: State<List<Field?>> = _fieldState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState
    val loadError = mutableStateOf("")

    init {
        fetchFields()
    }

    fun getAllField() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("SurveyViewModel", "getAllField: Loading started")
            try {
                val result = repository.getAllConfig()
                when (result) {
                    is Resource.Success -> {
                        _fieldState.value = result.data ?: emptyList()
                        loadError.value = ""
                        Log.d("SurveyViewModel", "getAllField: Success")
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message ?: "Unknown error"
                        loadError.value = "getAllFields() fetching failed"
                        Log.e("SurveyViewModel", "getAllField: Error - ${result.message}")
                    }
                }
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
                Log.e("SurveyViewModel", "getAllField: Exception - ${e.message}")
            } finally {
                _loadingState.value = false
                Log.d("SurveyViewModel", "getAllField: Loading finished")
            }
        }
    }

    fun fetchFields() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("SurveyViewModel", "fetchFields: Loading started")
            try {
                val result = repository.fetchConfigs()
                Log.d("SurveyViewModel", "fetchFields: Fetching configs")
                when (result) {
                    is Resource.Success -> {
                        loadError.value = ""
                        getAllField()
                        Log.d("SurveyViewModel", "fetchFields: Success")
                    }

                    is Resource.Error -> {
                        _errorState.value = result.message ?: "Unknown error"
                        loadError.value = "fetchFields() fetching failed"
                        Log.e("SurveyViewModel", "fetchFields: Error - ${result.message}")
                    }
                }
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
                Log.e("SurveyViewModel", "fetchFields: Exception - ${e.message}")
            } finally {
                _loadingState.value = false
                Log.d("SurveyViewModel", "fetchFields: Loading finished")
            }
        }
    }
}

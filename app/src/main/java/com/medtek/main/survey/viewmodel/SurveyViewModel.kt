package com.medtek.main.survey.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.local.entities.Survey
import com.medtek.main.data.remote.models.survey.ConfigValue
import com.medtek.main.data.remote.services.SurveyRequest
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.data.repository.survey.AuthRepository
import com.medtek.main.data.repository.survey.FieldRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: FieldRepository,
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    // Field variables
    private val _fieldState = mutableStateOf<Map<String, Set<ConfigValue>>>(mapOf())
    val fieldState: State<Map<String, Set<ConfigValue>>> = _fieldState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState
    val loadError = mutableStateOf("")

    // Stack to store pages
    private val pageStack: Stack<String> = Stack()

    // Survey variables
    private val _surveyRequests = mutableStateOf<List<FormField?>>(emptyList())
    val surveyRequests: State<List<FormField?>> = _surveyRequests

    private val _totalStep = 5
    private val _isCompleteSurvey = MutableStateFlow(false)
    val isCompleteSurvey: StateFlow<Boolean> = _isCompleteSurvey

    private val _isAuthRepoComplete = mutableStateOf(false)
    val isAuthRepoComplete: State<Boolean> = _isAuthRepoComplete

    private val _isUserRepoComplete = mutableStateOf(false)
    val isUserRepoComplete: State<Boolean> = _isUserRepoComplete


    init {
        viewModelScope.launch {
            getAllField()
            if (_fieldState.value.isEmpty()) fetchFields()
        }
    }

    fun addSurveyRequest(label: String, values: List<String>) {
        Log.d(
            "SurveyViewModel",
            "addSurveyRequest: Adding survey request for $label with values $values"
        )
        val existingRequest = _surveyRequests.value.find { it?.label == label }
        if (existingRequest != null) {
            updateSurveyRequest(label, values)
        } else {
            val newRequest = FormField(label, values)
            _surveyRequests.value = _surveyRequests.value + newRequest
        }
    }

    fun updateSurveyRequest(label: String, newValues: List<String>) {
        Log.d(
            "SurveyViewModel",
            "updateSurveyRequest: Updating survey request for $label with new values $newValues"
        )
        _surveyRequests.value = _surveyRequests.value.map { formField ->
            if (formField?.label == label) {
                formField.copy(values = newValues)
            } else {
                formField
            }
        }
    }


    fun sendSurveyToAuthRepo() {
        Log.d("SurveyViewModel", "sendSurveyToAuthRepo: Sending survey to authRepo")
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                val request = convertToSurveyRequest()
                authRepo.sendSurvey(request)
                _isAuthRepoComplete.value = true
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
                Log.e("SurveyViewModel", "sendSurveyToAuthRepo: Exception - ${e.message}")
            } finally {
                _loadingState.value = false
                Log.d("SurveyViewModel", "sendSurveyToAuthRepo: Survey sent to authRepo")
            }
        }
    }

    fun sendSurveyToUserRepo() {
        Log.d("SurveyViewModel", "sendSurveyToUserRepo: Sending survey to userRepo")
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            try {
                val request = convertToSurveyRequest()
                userRepo.addSurveyCurrentUser(convertToSurvey(request))
                _isUserRepoComplete.value = true
            } catch (e: Exception) {
                _errorState.value = e.localizedMessage ?: "Exception occurred"
                loadError.value = "Exception: ${e.message}"
                Log.e("SurveyViewModel", "sendSurveyToUserRepo: Exception - ${e.message}")
            } finally {
                _loadingState.value = false
                Log.d("SurveyViewModel", "sendSurveyToUserRepo: Survey sent to userRepo")
            }
        }
    }

    private fun convertToSurveyRequest(): SurveyRequest {
        Log.d(
            "SurveyViewModel",
            "convertToSurveyRequest: Converting survey requests to SurveyRequest"
        )
        val workFields =
            _surveyRequests.value.find { it?.label == "WorkFields" }?.values ?: emptyList()
        val sportFields =
            _surveyRequests.value.find { it?.label == "SportFields" }?.values ?: emptyList()
        val hobbies = _surveyRequests.value.find { it?.label == "Hobbies" }?.values ?: emptyList()
        val timeUsingPhone =
            _surveyRequests.value.find { it?.label == "timeUsingPhone" }?.values?.firstOrNull()
                ?.toIntOrNull() ?: 0
        val exerciseTimePerWeek =
            _surveyRequests.value.find { it?.label == "exerciseTimePerWeek" }?.values?.firstOrNull()
                ?.toIntOrNull() ?: 0

        return SurveyRequest(
            WorkFields = workFields,
            SportFields = sportFields,
            Hobbies = hobbies,
            timeUsingPhone = timeUsingPhone,
            exerciseTimePerWeek = exerciseTimePerWeek
        )
    }

    private fun convertToSurvey(surveyRequest: SurveyRequest): Survey {
        return Survey(
            workFields = surveyRequest.WorkFields,
            sportFields = surveyRequest.SportFields,
            hobbies = surveyRequest.Hobbies,
            timeUsingPhone = surveyRequest.timeUsingPhone,
            exerciseTimePerWeek = surveyRequest.exerciseTimePerWeek
        )
    }

    fun isCompleteAllStep() {
        Log.d("SurveyViewModel", "isCompleteAllStep: Checking if all steps are complete")
        _loadingState.value = true
        val counter = _surveyRequests.value.count()
        if (counter == _totalStep) {
            Log.d("SurveyViewModel", "isCompleteAllStep: All steps are complete.")
            _isCompleteSurvey.value = true
        } else {
            _isCompleteSurvey.value = false
            Log.d(
                "SurveyViewModel",
                "isCompleteAllStep: Steps completed: $counter out of $_totalStep."
            )
        }
        Log.d(
            "SurveyViewModel",
            "isCompleteAllStep: Current survey requests: ${_surveyRequests.value}"
        )
        _loadingState.value = false
    }

    private fun mapFieldState(fields: List<Field?>): Map<String, Set<ConfigValue>> {
        Log.d("SurveyViewModel", "mapFieldState: Mapping field state")
        val result = mutableMapOf<String, Set<ConfigValue>>()
        fields.forEach { field ->
            field?.let {
                result[it.configName] = it.configValues.toSet()
            }
        }
        Log.d("SurveyViewModel", "mapFieldState: Mapped field state - $result")
        return result
    }

    fun getAllField() {
        Log.d("SurveyViewModel", "getAllField: Fetching all fields")
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("SurveyViewModel", "getAllField: Loading started")
            try {
                val result = repository.getAllConfig()
                when (result) {
                    is Resource.Success -> {
                        _fieldState.value = mapFieldState(result.data ?: emptyList())
                        loadError.value = ""
                        Log.d("SurveyViewModel", "getAllField: Success - ${_fieldState.value}")
                        pageStack.addAll(_fieldState.value.keys)
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
        Log.d("SurveyViewModel", "fetchFields: Fetching fields")
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            Log.d("SurveyViewModel", "fetchFields: Loading started")
            try {
                val result = repository.fetchFields()
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

    fun getConfigValues(configName: String): List<ConfigValue>? {
        Log.d("SurveyViewModel", "getConfigValues: Fetching config values for $configName")
        val configValues = _fieldState.value[configName]
        Log.d("SurveyViewModel", "getConfigValues: Config values for $configName - $configValues")
        return configValues?.toList()
    }

    fun getAllConfigNames(): List<String> {
        Log.d("SurveyViewModel", "getAllConfigNames: Fetching all config names")
        val configNames = _fieldState.value.keys.toList()
        Log.d("SurveyViewModel", "getAllConfigNames: All config names - $configNames")
        return configNames
    }

    fun getNextPage(): String? {
        Log.d("SurveyViewModel", "getNextPage: Fetching next page")
        return if (pageStack.isNotEmpty()) {
            pageStack.pop()
        } else {
            null
        }
    }

    data class FormField(val label: String, val values: List<String>)
}


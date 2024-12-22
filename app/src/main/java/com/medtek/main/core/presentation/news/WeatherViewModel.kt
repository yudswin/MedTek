package com.medtek.main.core.presentation.news

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.data.repository.greeting.WeatherRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val country = "Vietnam"

    private var _weatherState = mutableStateOf<Weather?>(null)
    val weatherState: State<Weather?> = _weatherState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState

    var loadError = mutableStateOf("")

    init {
        loadWeather()

    }


    fun getWeather() {
        viewModelScope.launch {
            Log.d("WeatherViewModel", "Fetching weather data...")
            _loadingState.value = true
            _errorState.value = null
            val result = repository.getWeather()
            when (result) {
                is Resource.Success -> {
                    Log.d("WeatherViewModel", "Weather data fetched successfully")
                    _weatherState.value = result.data
                    _loadingState.value = false
                }

                is Resource.Error -> {
                    Log.e("WeatherViewModel", "Error fetching weather data: ${result.message}")
                    _errorState.value = result.message
                    _loadingState.value = false
                }
            }
        }
    }

    fun loadWeather() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null
            val result = repository.fetchWeather(country)
            when (result) {
                is Resource.Success -> {
                    loadError.value = ""
                    _loadingState.value = false
                    getWeather()
                }

                is Resource.Error -> {
                    _loadingState.value = false
                }
            }
        }
    }
}
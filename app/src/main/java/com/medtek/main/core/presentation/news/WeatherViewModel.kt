package com.medtek.main.core.presentation.news

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.data.repository.greeting.QuoteRepository
import com.medtek.main.data.repository.greeting.WeatherRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repositoryWeather: WeatherRepository,
    private val repositoryQuote: QuoteRepository
) : ViewModel() {

    val country = "Vietnam"

    // Weather Variables
    private var _weatherState = mutableStateOf<Weather?>(null)
    val weatherState: State<Weather?> = _weatherState

    private val _weatherLoadingState = mutableStateOf(false)
    val weatherLoadingState: State<Boolean> = _weatherLoadingState

    private val _weatherErrorState = mutableStateOf<String?>(null)
    val weatherErrorState: State<String?> = _weatherErrorState

    var weatherLoadError = mutableStateOf("")

    // Quote Variables
    private var _quoteState = mutableStateOf<Quote?>(null)
    val quoteState: State<Quote?> = _quoteState

    private val _quoteLoadingState = mutableStateOf(false)
    val quoteLoadingState: State<Boolean> = _quoteLoadingState

    private val _quoteErrorState = mutableStateOf<String?>(null)
    val quoteErrorState: State<String?> = _quoteErrorState

    var quoteLoadError = mutableStateOf("")


    init {
        loadWeather()
        loadQuote()
    }


    fun getWeather() {
        viewModelScope.launch {
            Log.i("WeatherViewModel", "Fetching weather data...")
            _weatherLoadingState.value = true
            _weatherErrorState.value = null
            val result = repositoryWeather.getWeather()
            when (result) {
                is Resource.Success -> {
                    Log.i("WeatherViewModel", "Weather data fetched successfully")
                    _weatherState.value = result.data
                    _weatherLoadingState.value = false
                }

                is Resource.Error -> {
                    Log.e("WeatherViewModel", "Error fetching weather data: ${result.message}")
                    _weatherErrorState.value = result.message
                    _weatherLoadingState.value = false
                }
            }
        }
    }

    fun getQuote() {
        viewModelScope.launch {
            Log.i("WeatherViewModel", "Fetching quote data...")
            _quoteLoadingState.value = true
            _quoteErrorState.value = null
            val result = repositoryQuote.getNextQuote()
            when (result) {
                is Resource.Success -> {
                    Log.i("WeatherViewModel", "Quote data fetched successfully")
                    _quoteState.value = result.data
                    _quoteLoadingState.value = false
                    repositoryQuote.markQuoteAsUsed(result.data!!)
                }

                is Resource.Error -> {
                    Log.e("WeatherViewModel", "Error fetching quote data: ${result.message}")
                    _quoteErrorState.value = result.message
                    _quoteLoadingState.value = false
                }
            }
        }
    }

    fun loadQuote() {
        viewModelScope.launch {
            _quoteLoadingState.value = true
            _quoteErrorState.value = null
            val result = repositoryQuote.fetchAndStoreQuotes(7)
            when (result) {
                is Resource.Success -> {
                    Log.i("WeatherViewModel", "Quote data fetched successfully")
                    _quoteLoadingState.value = false
                    getQuote()
                }

                is Resource.Error -> {
                    Log.e("WeatherViewModel", "Error fetching quote data: ${result.message}")
                    _quoteErrorState.value = result.message
                    _quoteLoadingState.value = false
                }
            }
        }
    }

    fun loadWeather() {
        viewModelScope.launch {
            _weatherLoadingState.value = true
            _weatherErrorState.value = null
            val result = repositoryWeather.fetchWeather(country)
            when (result) {
                is Resource.Success -> {
                    weatherLoadError.value = ""
                    _weatherLoadingState.value = false
                    getWeather()
                }

                is Resource.Error -> {
                    _weatherLoadingState.value = false
                }
            }
        }
    }
}
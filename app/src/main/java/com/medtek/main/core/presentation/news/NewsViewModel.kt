package com.medtek.main.core.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.News
import com.medtek.main.data.repository.news.NewsRepository
import com.medtek.main.utilties.Resource
import com.medtek.main.utilties.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<UIState<List<News>>>(UIState.Loading)
    val newsState = _newsState.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val isLoading = _newsState.map { it is UIState.Loading }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )

    init {
        val currentDate = LocalDate.now()
        val cleanupThresholdDate = currentDate.minusDays(30).toString()

        viewModelScope.launch {
            // Sequentially perform all tasks during initialization
            loadLocalNews()
            cleanupOldNews(cleanupThresholdDate)
            refreshRemoteNews()
            checkNewsStatus(currentDate.toString())
        }
    }

    private fun handleError(message: String?, httpCode: Int? = null) {
        val errorMsg = "Error: ${message ?: "Unknown Error"}${if (httpCode != null) " (Code: $httpCode)" else ""}"
        _error.value = errorMsg
        Log.e("NewsViewModel", errorMsg)
    }


    fun loadLocalNews() {
        Log.d("NewsViewModel", "loadLocalNews called")
        viewModelScope.launch {
            _newsState.value = UIState.Loading
            when (val result = repository.getLocalNews()) {
                is Resource.Success -> {
                    _newsState.value = UIState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _newsState.value = UIState.Error(result.message ?: "Failed to load local news")
                    handleError(result.message)
                }
            }
        }
    }

    fun refreshRemoteNews() {
        Log.d("NewsViewModel", "refreshRemoteNews called")
        viewModelScope.launch {
            _newsState.value = UIState.Loading
            val refreshResult = repository.refreshNews()
            if (refreshResult is Resource.Error) {
                _newsState.value = UIState.Error(refreshResult.message ?: "Failed to refresh news")
                handleError(refreshResult.message)
            } else {
                fetchAndUpdateNews()
            }
        }
    }

    private suspend fun fetchAndUpdateNews() {
        when (val fetchResult = repository.fetchAndStoreNewsList()) {
            is Resource.Success -> {
                _newsState.value = UIState.Success(fetchResult.data ?: emptyList())
            }
            is Resource.Error -> {
                _newsState.value = UIState.Error(fetchResult.message ?: "Failed to fetch news")
                handleError(fetchResult.message)
            }
        }
    }

    fun checkNewsStatus(dateString: String) {
        Log.d("NewsViewModel", "checkNewsStatus called with dateString: $dateString")
        viewModelScope.launch {
            val result = repository.checkNewsStatus(dateString)
            if (result is Resource.Error) {
                if (result.httpCode == 304) {
                    autoRefreshNews()
                } else {
                    handleError(result.message, result.httpCode)
                }
            }
        }
    }

    private fun autoRefreshNews() {
        Log.d("NewsViewModel", "autoRefreshNews called")
        viewModelScope.launch {
            refreshRemoteNews()
        }
    }

    fun cleanupOldNews(beforeDate: String) {
        Log.d("NewsViewModel", "cleanupOldNews called with beforeDate: $beforeDate")
        viewModelScope.launch {
            val result = repository.cleanupOldNews(beforeDate)
            if (result is Resource.Error) {
                handleError(result.message)
            } else {
                loadLocalNews()
            }
        }
    }
}
package com.medtek.main.core.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medtek.main.data.local.entities.News
import com.medtek.main.data.repository.news.NewsRepository
import com.medtek.main.utilties.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<List<News>>(emptyList())
    val newsState = _newsState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        val currentDate = LocalDate.now()
        val cleanupThresholdDate = currentDate.minusDays(30).toString()

        viewModelScope.launch {
            // Load local news initially
            loadLocalNews()

            // Clean up old news
            cleanupOldNews(cleanupThresholdDate)

            // Refresh remote news
            refreshRemoteNews()

            // Check news status using today's date
            checkNewsStatus(currentDate.toString())
        }
    }


    fun loadLocalNews() {
        Log.d("NewsViewModel", "loadLocalNews called")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            when (val result = repository.getLocalNews()) {
                is Resource.Success -> {
                    val uniqueNews = clearDuplicatedNews(result.data ?: emptyList())
                    _newsState.value = uniqueNews
                }

                is Resource.Error -> {
                    _error.value = result.message
                    Log.e("NewsViewModel", "Error: ${result.message}")
                }
            }

            _loading.value = false
        }
    }

    fun refreshRemoteNews() {
        Log.d("NewsViewModel", "refreshRemoteNews called")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            when (val refreshResult = repository.refreshNews()) {
                is Resource.Success -> {
                    when (val fetchResult = repository.fetchAndStoreNewsList()) {
                        is Resource.Success -> {
                            _newsState.value = fetchResult.data ?: emptyList()
                        }

                        is Resource.Error -> {
                            _error.value = fetchResult.message
                        }
                    }
                }

                is Resource.Error -> {
                    _error.value = refreshResult.message
                }
            }

            _loading.value = false
        }
    }

    fun checkNewsStatus(dateString: String) {
        Log.d("NewsViewModel", "checkNewsStatus called with dateString: $dateString")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val result = repository.checkNewsStatus(dateString)
            when (result) {
                is Resource.Success -> {
                    // If successful (200 OK, etc.), maybe do nothing
                }

                is Resource.Error -> {
                    if (result.httpCode == 304) {
                        autoRefreshNews()
                    } else {
                        _error.value = result.message
                    }
                }
            }
            _loading.value = false
        }
    }

    private fun autoRefreshNews() {
        Log.d("NewsViewModel", "autoRefreshNews called")
        viewModelScope.launch {
            val refreshResult = repository.refreshNews()
            if (refreshResult is Resource.Error) {
                _error.value = refreshResult.message
                return@launch
            }

            val fetchResult = repository.fetchAndStoreNewsList()
            if (fetchResult is Resource.Error) {
                _error.value = fetchResult.message
                return@launch
            }

            val localResult = repository.getLocalNews()
            if (localResult is Resource.Success) {
                _newsState.value = localResult.data ?: emptyList()
            } else {
                _error.value = localResult.message
            }
        }
    }

    private fun clearDuplicatedNews(newsList: List<News>): List<News> {
        return newsList.distinctBy { it.title }
    }

    fun cleanupOldNews(beforeDate: String) {
        Log.d("NewsViewModel", "cleanupOldNews called with beforeDate: $beforeDate")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val result = repository.cleanupOldNews(beforeDate)
            if (result is Resource.Error) {
                _error.value = result.message
            } else {
                loadLocalNews()
            }

            _loading.value = false
        }
    }
}
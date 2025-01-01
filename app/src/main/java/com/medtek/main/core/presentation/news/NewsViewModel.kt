package com.example.app.presentation

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
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    // StateFlow or LiveData to track news
    private val _newsState = MutableStateFlow<List<News>>(emptyList())
    val newsState = _newsState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Example: fetch local DB news
    fun loadLocalNews() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            when (val result = repository.getLocalNews()) {
                is Resource.Success -> {
                    _newsState.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    _error.value = result.message
                    Log.e("NewsViewModel", "Error: ${result.message}")
                }
            }

            _loading.value = false
        }
    }

    // Example: refresh remote news
    fun refreshRemoteNews() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            when (val refreshResult = repository.refreshNews()) {
                is Resource.Success -> {
                    // Possibly after refreshing, call fetchAndStoreNewsList
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

    // Example: check news status
    fun checkNewsStatus(dateString: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val result = repository.checkNewsStatus(dateString)
            when (result) {
                is Resource.Success -> {
                    // If successful (200 OK, etc.), maybe do nothing
                }
                is Resource.Error -> {
                    // Check if the server responded 304
                    if (result.httpCode == 304) {
                        // If 304, we can auto-refresh or prompt the user, etc.
                        autoRefreshNews()
                    } else {
                        // Other errors: show them in the UI
                        _error.value = result.message
                    }
                }
            }
            _loading.value = false
        }
    }

    private fun autoRefreshNews() {
        viewModelScope.launch {
            // 1) Call refresh
            val refreshResult = repository.refreshNews()
            if (refreshResult is Resource.Error) {
                _error.value = refreshResult.message
                return@launch
            }

            // 2) After refreshing, fetch and store the new list
            val fetchResult = repository.fetchAndStoreNewsList()
            if (fetchResult is Resource.Error) {
                _error.value = fetchResult.message
                return@launch
            }

            // 3) Finally, update local data
            //    e.g., reload from DB or set _newsState.value directly
            val localResult = repository.getLocalNews()
            if (localResult is Resource.Success) {
                _newsState.value = localResult.data ?: emptyList()
            } else {
                _error.value = localResult.message
            }
        }
    }
    // Example: cleanup old news
    fun cleanupOldNews(beforeDate: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val result = repository.cleanupOldNews(beforeDate)
            if (result is Resource.Error) {
                _error.value = result.message
            } else {
                // Optionally reload local news
                loadLocalNews()
            }

            _loading.value = false
        }
    }
}

package com.medtek.main.data.repository.news

import com.medtek.main.data.local.entities.News
import com.medtek.main.utilties.Resource

interface NewsRepository {
    suspend fun checkNewsStatus(requestDate: String): Resource<Unit>

    suspend fun refreshNews(): Resource<Unit>

    suspend fun fetchAndStoreNewsList(): Resource<List<News>>

    suspend fun getLocalNews(): Resource<List<News>>

    suspend fun cleanupOldNews(olderThanDate: String): Resource<Unit>
}
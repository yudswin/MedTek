package com.medtek.main.data.repository.news

import android.util.Log
import com.medtek.main.data.local.dao.NewsDao
import com.medtek.main.data.local.entities.News
import com.medtek.main.data.remote.services.NewsRequest
import com.medtek.main.data.remote.services.NewsService
import com.medtek.main.utilties.Resource
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val dao: NewsDao,
    private val service: NewsService
) : NewsRepository {

    override suspend fun checkNewsStatus(requestDate: String): Resource<Unit> {
        Log.d("NewsRepositoryImpl", "checkNewsStatus called with requestDate: $requestDate")
        return try {
            val response = service.checkNewsStatus(NewsRequest(date = requestDate))
            if (response.isSuccessful) {
                Log.d("NewsRepositoryImpl", "checkNewsStatus successful")
                Resource.Success(Unit)
            } else {
                Log.e("NewsRepositoryImpl", "checkNewsStatus failed with HTTP ${response.code()}")
                Resource.Error(
                    message = "Server responded with HTTP ${response.code()}",
                    httpCode = response.code()
                )
            }
        } catch (e: Exception) {
            Log.e("NewsRepositoryImpl", "Error during checkNewsStatus: ${e.message}")
            Resource.Error("Error during checkNewsStatus: ${e.message}")
        }
    }

    override suspend fun refreshNews(): Resource<Unit> {
        Log.d("NewsRepositoryImpl", "refreshNews called")
        return try {
            val response = service.refreshNews()
            if (response.isSuccessful) {
                Log.d("NewsRepositoryImpl", "refreshNews successful")
                Resource.Success(Unit)
            } else {
                Log.e(
                    "NewsRepositoryImpl",
                    "refreshNews failed with HTTP ${response.code()}: ${response.message()}"
                )
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("NewsRepositoryImpl", "Error during refreshNews: ${e.message}")
            Resource.Error("Error during refreshNews: ${e.message}")
        }
    }

    override suspend fun fetchAndStoreNewsList(): Resource<List<News>> {
        Log.d("NewsRepositoryImpl", "fetchAndStoreNewsList called")
        return try {
            val remoteData = service.getListNews()
            val remoteArticles = remoteData.data
            val localArticles = remoteArticles.map { remoteNews ->
                News(
                    id = 0,
                    author = remoteNews.author,
                    content = remoteNews.content,
                    date = remoteNews.date,
                    title = remoteNews.title,
                    sourceName = remoteNews.sourceName
                )
            }
            dao.insertNews(localArticles)
            Log.d("NewsRepositoryImpl", "fetchAndStoreNewsList successful")
            Resource.Success(localArticles)
        } catch (e: Exception) {
            Log.e("NewsRepositoryImpl", "Error during fetchAndStoreNewsList: ${e.message}")
            Resource.Error("Error during fetchAndStoreNewsList: ${e.message}")
        }
    }

    override suspend fun getLocalNews(): Resource<List<News>> {
        Log.d("NewsRepositoryImpl", "getLocalNews called")
        return try {
            val newsList = dao.getNews()
            Log.d("NewsRepositoryImpl", "getLocalNews successful")
            Resource.Success(newsList)
        } catch (e: Exception) {
            Log.e("NewsRepositoryImpl", "Error reading local news: ${e.message}")
            Resource.Error("Error reading local news: ${e.message}")
        }
    }

    override suspend fun cleanupOldNews(olderThanDate: String): Resource<Unit> {
        Log.d("NewsRepositoryImpl", "cleanupOldNews called with olderThanDate: $olderThanDate")
        return try {
            dao.deleteOldNews(olderThanDate)
            Log.d("NewsRepositoryImpl", "cleanupOldNews successful")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("NewsRepositoryImpl", "Error cleaning old news: ${e.message}")
            Resource.Error("Error cleaning old news: ${e.message}")
        }
    }
}
package com.medtek.main.data.repository.news

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
        return try {
            val response = service.checkNewsStatus(NewsRequest(date = requestDate))

            return if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                // Use the HTTP code for more logic in the ViewModel
                Resource.Error(
                    message = "Server responded with HTTP ${response.code()}",
                    httpCode = response.code()
                )
            }
        } catch (e: Exception) {
            return Resource.Error("Error during checkNewsStatus: ${e.message}")
        }
    }

    override suspend fun refreshNews(): Resource<Unit> {
        return try {
            val response = service.refreshNews()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error during refreshNews: ${e.message}")
        }
    }

    override suspend fun fetchAndStoreNewsList(): Resource<List<News>> {
        return try {
            val remoteData = service.getListNews()  // NewsResponse
            val remoteArticles = remoteData.data     // List<NewsResponse.News>

            // Map remote articles to local News entity
            val localArticles = remoteArticles.map { remoteNews ->
                News(
                    // id is auto-gen, so 0
                    id = 0,
                    author = remoteNews.author,
                    content = remoteNews.content,
                    date = remoteNews.date,
                    title = remoteNews.title,
                    sourceName = remoteNews.sourceName
                )
            }

            // Store in DB
            dao.insertNews(localArticles)

            Resource.Success(localArticles)
        } catch (e: Exception) {
            Resource.Error("Error during fetchAndStoreNewsList: ${e.message}")
        }
    }

    override suspend fun getLocalNews(): Resource<List<News>> {
        return try {
            val newsList = dao.getNews()
            Resource.Success(newsList)
        } catch (e: Exception) {
            Resource.Error("Error reading local news: ${e.message}")
        }
    }

    override suspend fun cleanupOldNews(olderThanDate: String): Resource<Unit> {
        return try {
            dao.deleteOldNews(olderThanDate)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error cleaning old news: ${e.message}")
        }
    }

}
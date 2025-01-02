package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.news.NewsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class NewsRequest(
    val date: String
)

interface NewsService {
    @POST("news/check-news-status")
    suspend fun checkNewsStatus(
        @Body request: NewsRequest
    ): retrofit2.Response<Unit>

    @POST("news/refresh-news")
    suspend fun refreshNews(): retrofit2.Response<Unit>

    @GET("news/get-news-list")
    suspend fun getListNews(): NewsResponse
}
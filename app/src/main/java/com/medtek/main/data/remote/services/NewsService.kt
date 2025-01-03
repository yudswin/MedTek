package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.news.NewsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

data class NewsRequest(
    val date: String
)

interface NewsService {
    @POST("news/check-news-status")
    suspend fun checkNewsStatus(
        @Body request: NewsRequest
    ): Response<Unit>

    @POST("news/refresh-news")
    suspend fun refreshNews(): Response<Unit>

    @GET("news/get-news-list")
    suspend fun getListNews(): NewsResponse
}
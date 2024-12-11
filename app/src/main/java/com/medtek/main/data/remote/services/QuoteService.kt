package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.QuoteRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteService {
    @GET("quotes/latest")
    suspend fun getLatestQuotes(
        @Query("limit") limit: Int
    ): Response<List<QuoteRemote>>
}
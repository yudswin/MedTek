package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.survey.FieldResponse
import retrofit2.Response
import retrofit2.http.GET

interface FieldService {
    @GET("configs/allConfigs")
    suspend fun getAllField(): Response<FieldResponse>
}
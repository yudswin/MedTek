package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.plan.PlanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/habitPlan/{id}")
    suspend fun fetchUserPlan(
        @Path("id") userId: String,
    ): Response<PlanResponse>
}
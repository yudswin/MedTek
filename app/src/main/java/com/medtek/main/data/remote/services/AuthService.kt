package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.auth.AuthResponse
import com.medtek.main.data.remote.models.auth.LoginResponse
import com.medtek.main.data.remote.models.survey.SurveyResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class SignInRequest(
    val email: String,
    val name: String = "user"
)

data class AuthRequest(
    val code: String,
    val userId: String
)

data class SurveyRequest(
    val WorkFields: List<String>,
    val SportFields: List<String>,
    val Hobbies: List<String>,
    val timeUsingPhone: Int,
    val exerciseTimePerWeek: Int
)

interface AuthService {
    @POST("users")
    suspend fun signIn(
        @Body request: SignInRequest,
    ): LoginResponse?

    @POST("users/checkAccessCode")
    suspend fun auth(
        @Body request: AuthRequest
    ): AuthResponse

    @POST("users/entryForm/{id}")
    suspend fun submitUserPreferences(
        @Path("id") userId: String,
        @Body request: SurveyRequest
    ): SurveyResponse
}
package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.AuthResponse
import com.medtek.main.data.remote.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SignInRequest(
    val email: String
)

data class AuthRequest(
    val code: String,
    val userId: String
)

interface AuthService {
    @POST("users")
    suspend fun signIn(
        @Body request: SignInRequest
    ): LoginResponse?

    @POST("users/checkAccessCode")
    suspend fun auth(
        @Body request: AuthRequest
    ): AuthResponse
}
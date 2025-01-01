package com.medtek.main.data.repository.survey

import com.medtek.main.data.remote.models.auth.AuthResponse
import com.medtek.main.data.remote.models.auth.LoginResponse
import com.medtek.main.data.remote.services.AuthRequest
import com.medtek.main.data.remote.services.SignInRequest
import com.medtek.main.data.remote.services.SurveyRequest
import com.medtek.main.utilties.Resource

interface AuthRepository {
    suspend fun signIn(request: SignInRequest): Resource<LoginResponse?>

    suspend fun getUserId(): Resource<String>

    suspend fun auth(request: AuthRequest): Resource<AuthResponse?>

    suspend fun isNewUser(): Resource<Boolean>

    suspend fun sendSurvey(request: SurveyRequest): Resource<Unit>
}
package com.medtek.main.data.repository.survey

import android.util.Log
import com.medtek.main.data.local.dao.UserDao
import com.medtek.main.data.local.entities.User
import com.medtek.main.data.remote.models.AuthResponse
import com.medtek.main.data.remote.models.LoginResponse
import com.medtek.main.data.remote.services.AuthRequest
import com.medtek.main.data.remote.services.AuthService
import com.medtek.main.data.remote.services.SignInRequest
import com.medtek.main.data.remote.services.SurveyRequest
import com.medtek.main.utilties.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val dao: UserDao
) : AuthRepository {

    override suspend fun signIn(request: SignInRequest): Resource<LoginResponse?> {
        Log.d("AuthRepositoryImpl", "signIn: Signing in started")
        val response = try {
            api.signIn(request = request)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signIn: Error signing in - ${e.message}")
            return Resource.Error("Error signIn to server: ${e.message}")
        }

        val storedResponse = try {
            val user = User(
                id = response!!.userId,
                email = request.email,
            )
            dao.createUser(user)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "signIn: Error storing user - ${e.message}")
            return Resource.Error("signIn failed: $e")
        }

        Log.d("AuthRepositoryImpl", "signIn: Signing in successful")
        return Resource.Success(response)
    }

    override suspend fun getUserId(): Resource<String> {
        Log.d("AuthRepositoryImpl", "getUserId: Fetching user ID started")
        val response = try {
            dao.getUserID()
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "getUserId: Error fetching user ID - ${e.message}")
            return Resource.Error("getUserId failed: $e")
        }
        Log.d("AuthRepositoryImpl", "getUserId: Fetching user ID successful")
        return Resource.Success(response)
    }

    override suspend fun auth(request: AuthRequest): Resource<AuthResponse?> {
        Log.d("AuthRepositoryImpl", "auth: Authenticating user started")
        val response = try {
            api.auth(request = request)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "auth: Error authenticating user - ${e.message}")
            return Resource.Error("Error auth user: ${e.message}")
        }
        Log.d("AuthRepositoryImpl", "auth: Authenticating user successful")
        return Resource.Success(response)
    }

    override suspend fun isNewUser(): Resource<Boolean> {
        Log.d("AuthRepositoryImpl", "isNewUser: Checking if user is new started")
        return try {
            val isDoneSurvey = dao.isUserDoneSurvey()
            Log.d("AuthRepositoryImpl", "isNewUser: Checking if user is new successful")
            Resource.Success(!isDoneSurvey)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "isNewUser: Error checking if user is new - ${e.message}")
            Resource.Error("isNewUser check failed: ${e.message}")
        }
    }

    override suspend fun sendSurvey(request: SurveyRequest): Resource<Unit> {
        Log.d("AuthRepositoryImpl", "sendSurvey: Sending survey started")
        val userId = when (val result = getUserId()) {
            is Resource.Success -> result.data
            is Resource.Error -> {
                Log.e("AuthRepositoryImpl", "sendSurvey: Error getting user ID - ${result.message}")
                return Resource.Error("UserId get failed: ${result.message}")
            }
        }

        return try {
            api.submitUserPreferences(userId.toString(), request)
            Log.d("AuthRepositoryImpl", "sendSurvey: Sending survey successful")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "sendSurvey: Error sending survey - ${e.message}")
            Resource.Error("Failed to send survey: ${e.message}")
        }
    }
}
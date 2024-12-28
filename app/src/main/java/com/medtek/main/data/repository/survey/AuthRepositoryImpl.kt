package com.medtek.main.data.repository.survey

import com.medtek.main.data.local.dao.UserDao
import com.medtek.main.data.local.entities.User
import com.medtek.main.data.remote.models.AuthResponse
import com.medtek.main.data.remote.models.LoginResponse
import com.medtek.main.data.remote.services.AuthRequest
import com.medtek.main.data.remote.services.AuthService
import com.medtek.main.data.remote.services.SignInRequest
import com.medtek.main.utilties.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthService,
    private val dao: UserDao
) : AuthRepository {
    override suspend fun signIn(request: SignInRequest): Resource<LoginResponse?> {
        val response = try {
            api.signIn(request = request)
        } catch (e: Exception) {
            return Resource.Error("Error signIn to server: ${e.message}")
        }

        val storedResponse = try {
            val user = User(
                id = response!!.userId,
                email = request.email,
            )
            dao.createUser(user)
        } catch (e: Exception) {
            return Resource.Error("signIn failed: $e")
        }

        return Resource.Success(response)
    }

    override suspend fun getUserId(): Resource<String> {
        val response = try {
            dao.getUserID()
        } catch (e: Exception) {
            return Resource.Error("getUserId failed: $e")
        }
        return Resource.Success(response)
    }

    override suspend fun auth(request: AuthRequest): Resource<AuthResponse?> {
        val response = try {
            api.auth(request = request)
        } catch (e: Exception) {
            return Resource.Error("Error auth user: ${e.message}")
        }

        return Resource.Success(response)
    }
}
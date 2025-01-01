package com.medtek.main.data.remote.models.auth

data class AuthResponse(
    val isNewUser: Boolean,
    val userId: String
)
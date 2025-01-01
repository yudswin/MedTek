package com.medtek.main.data.remote.models.greeting

data class WeatherRemote(
    val _id: String,
    val name: String,
    val country: String,
    val temp_c: Double,
    val condition: String,
    val icon: String,
    val windSpeed: Double,
    val humidity: Int,
    val uvIndex: Double,
    val localTime: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)
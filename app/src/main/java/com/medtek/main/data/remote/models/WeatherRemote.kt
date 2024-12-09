package com.medtek.main.data.remote.models

data class WeatherRemote(
    val id: String,
    val name: String,
    val country: String,
    val temp_c: Int,
    val condition: String,
    val icon: String,
    val windSpeed: Int,
    val humidity: Int,
    val uvIndex: Double,
    val localTime: String,
    val createAt: String,      // Date stored as ISO-8601 String
    val updateAt: String       // Date stored as ISO-8601 String
)

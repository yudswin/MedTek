package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val temp: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val weatherIcon: String,
    val lastUpdated: String
)

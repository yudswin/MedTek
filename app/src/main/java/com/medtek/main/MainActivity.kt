package com.medtek.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.medtek.main.core.HomeActivity
import com.medtek.main.core.presentation.home.HabitScreen
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Weather
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    lateinit var database: AppDatabase
    private set
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "MedTekLocalDB",
        ).build()

        // Insert sample data and fetch all records
        lifecycleScope.launch {
            insertSampleData()
            fetchAndLogAllWeather()
        }

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    private suspend fun insertSampleData() {
        val weatherDao = database.weatherDao()

        val sampleWeather = Weather(
            id = 0,
            date = "2024-12-10",
            temp = 22.5,
            condition = "Sunny",
            humidity = 55,
            windSpeed = 3.2,
            weatherIcon = "sunny_icon.png",
            lastUpdated = "2024-12-10T12:00:00Z"
        )

        // Insert the sample weather data
        weatherDao.insertWeather(sampleWeather)
        Log.d("DatabaseLog", "Inserted Weather: $sampleWeather")
    }

    private suspend fun fetchAndLogAllWeather() {
        val weatherDao = database.weatherDao()

        // Fetch all weather entries
        val weatherList = weatherDao.getAllWeather()

        // Log each entry
        weatherList.forEach { weather ->
            Log.d("DatabaseLog", "Weather Entry: $weather")
        }
    }
}


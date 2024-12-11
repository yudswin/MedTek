package com.medtek.main.data.repository.weather

import android.util.Log
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.data.remote.network.RetrofitClient
import com.medtek.main.data.remote.services.WeatherService

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val weatherService: WeatherService
) {
    suspend fun fetchAndStoreWeather(country: String) {
        try {
            val weatherRemote = weatherService.getWeatherByCountry(country)
            if (weatherRemote == null) {
                throw Exception("Weather data is empty for the country: $country")
            }

            val weather = Weather(
                id = 0,
                date = weatherRemote.localTime,
                temp = weatherRemote.temp_c,
                condition = weatherRemote.condition,
                humidity = weatherRemote.humidity,
                windSpeed = weatherRemote.windSpeed,
                weatherIcon = "https:${weatherRemote.icon}",
                lastUpdated = weatherRemote.updatedAt
            )
            weatherDao.insertWeather(weather)
            weatherDao.deleteOldWeather(weatherRemote.localTime)
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error during API call: ${e.message}")
            throw Exception("Failed to fetch weather data: ${e.message}")
        }
    }


    suspend fun getTodayWeather(date: String): Weather? {
        return weatherDao.getTodayWeather(date)
    }

    suspend fun getAllWeather(): List<Weather> {
        return weatherDao.getAllWeather()
    }
}

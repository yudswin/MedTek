package com.medtek.main.data.repository.greeting

import android.util.Log
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.data.remote.services.WeatherService
import com.medtek.main.utilties.Resource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.text.format

class WeatherRepositoryImpl @Inject constructor(
    private val dao: WeatherDao,
    private val api: WeatherService
) : WeatherRepository {

    override suspend fun getWeather(): Resource<Weather?> {
//        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
        val response = try {
            dao.getFirstWeather()
        } catch (e: Exception) {
            Log.e("WeatherRepositoryImpl", "Error fetching weather: $e")
            return Resource.Error("getTodayWeather() failed!: $e")
        }
        Log.d("WeatherRepositoryImpl", "Weather fetched successfully: $response")
        return Resource.Success(response)
    }


    override suspend fun fetchWeather(country: String): Resource<Unit> {

        val fetchResponse = try {
            api.getWeatherByCountry(country)
        } catch (e: Exception) {
            return Resource.Error("api.getWeatherByCountry(country) failed: $e")
        }

        val weather = Weather(
            date = fetchResponse.localTime,
            temp = fetchResponse.temp_c,
            condition = fetchResponse.condition,
            humidity = fetchResponse.humidity,
            windSpeed = fetchResponse.windSpeed,
            weatherIcon = "https:${fetchResponse.icon}",
            lastUpdated = fetchResponse.updatedAt
        )

        val storedResponse = try {
            dao.insertWeather(weather)
        } catch (e: Exception) {
            return Resource.Error("api.getWeatherByCountry(country) failed: $e")
        }

        return return Resource.Success(storedResponse)
    }

}
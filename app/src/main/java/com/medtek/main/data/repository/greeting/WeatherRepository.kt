package com.medtek.main.data.repository.greeting

import com.medtek.main.data.local.entities.Weather
import com.medtek.main.utilties.Resource

interface WeatherRepository {
    suspend fun getWeather(): Resource<Weather?>

    suspend fun fetchWeather(country: String): Resource<Unit>
}

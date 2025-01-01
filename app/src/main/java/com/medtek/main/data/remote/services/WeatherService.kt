package com.medtek.main.data.remote.services

import com.medtek.main.data.remote.models.greeting.WeatherRemote
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("weather/{country}")
    suspend fun getWeatherByCountry(
        @Path("country") country: String
    ): WeatherRemote

}
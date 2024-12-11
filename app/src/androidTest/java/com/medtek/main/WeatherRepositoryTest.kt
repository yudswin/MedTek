package com.medtek.main

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.data.remote.models.WeatherRemote
import com.medtek.main.data.remote.services.WeatherService
import com.medtek.main.data.repository.weather.WeatherRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var weatherDao: WeatherDao
    private lateinit var weatherService: WeatherService
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        // Initialize an in-memory Room database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        weatherDao = database.weatherDao()

        // Mock the WeatherService using Mockito
        weatherService = Mockito.mock(WeatherService::class.java)

        // Inject the mocked service into the repository
        weatherRepository = WeatherRepository(weatherDao, weatherService)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun fetchAndStoreWeather() = runBlocking {
        // Define a mocked API response
        val weatherRemote = WeatherRemote(
            _id = "673ddbe988e470940b661382",
            name = "Hanoi",
            country = "Vietnam",
            temp_c = 27.2,
            condition = "Partly cloudy",
            icon = "//cdn.weatherapi.com/weather/64x64/day/116.png",
            windSpeed = 14.4,
            humidity = 58,
            uvIndex = 0.9,
            localTime = "2024-12-05 15:42",
            createdAt = "2024-11-20T12:54:01.885Z",
            updatedAt = "2024-12-05T09:00:03.131Z",
            __v = 0
        )

        // Mock the API call to return the mocked response
        `when`(weatherService.getWeatherByCountry("Vietnam")).thenReturn(weatherRemote)

        // Fetch weather data and store it in the database
        weatherRepository.fetchAndStoreWeather("Vietnam")

        // Validate the stored weather data
        val storedWeather = weatherDao.getTodayWeather("2024-12-05 15:42")

        // Assertions to verify the data stored in the database matches the API response
        assertEquals(weatherRemote.localTime, storedWeather?.date)
        assertEquals(weatherRemote.temp_c, storedWeather?.temp)
        assertEquals(weatherRemote.condition, storedWeather?.condition)
        assertEquals(weatherRemote.humidity, storedWeather?.humidity)
        assertEquals(weatherRemote.windSpeed, storedWeather?.windSpeed)
        assertEquals("https:${weatherRemote.icon}", storedWeather?.weatherIcon) // Ensure full URL for icon
        assertEquals(weatherRemote.updatedAt, storedWeather?.lastUpdated)
    }
}

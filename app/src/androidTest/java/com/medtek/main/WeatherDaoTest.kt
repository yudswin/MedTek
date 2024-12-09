package com.medtek.main

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Weather
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        weatherDao = database.weatherDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertWeatherAndGetAllWeather() = runBlocking {
        val sampleWeather = Weather(
            date = "2024-12-10",
            temp = 22.5,
            condition = "Sunny",
            humidity = 55,
            windSpeed = 3.2,
            weatherIcon = "sunny_icon.png",
            lastUpdated = "2024-12-10T12:00:00Z"
        )

        weatherDao.insertWeather(sampleWeather)
        val weatherList = weatherDao.getAllWeather()

        assertEquals(1, weatherList.size)
        assertEquals(sampleWeather, weatherList[0])
    }
}
package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.medtek.main.data.local.entities.Weather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE date = :currentDate LIMIT 1")
    suspend fun getTodayWeather(currentDate: String): Weather?

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: Weather): Long

    @Query("DELETE FROM weather WHERE date != :currentDate")
    suspend fun deleteOldWeather(currentDate: String): Int

    // Fetch all weather entries
    @Query("SELECT * FROM weather")
    suspend fun getAllWeather(): List<Weather>
}
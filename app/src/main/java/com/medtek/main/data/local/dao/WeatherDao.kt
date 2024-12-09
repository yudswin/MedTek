package com.medtek.main.data.local.dao

import com.medtek.main.data.local.entities.Weather
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE date = :currentDate LIMIT 1")
    suspend fun getTodayWeather(currentDate: String): Weather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: Weather)

    @Query("DELETE FROM weather WHERE date != :currentDate")
    suspend fun deleteOldWeather(currentDate: String)
}
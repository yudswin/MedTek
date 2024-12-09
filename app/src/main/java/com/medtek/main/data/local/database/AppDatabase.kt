package com.medtek.main.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.local.entities.Weather

//@Database(entities = [Weather::class, Quote::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun weatherDao(): WeatherDao
//    abstract fun quoteDao(): QuoteDao
//}
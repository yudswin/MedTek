package com.medtek.main.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medtek.main.data.local.dao.FieldDao
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.utilties.Converters

@Database(
    entities = [
        Weather::class,
        Quote::class,
        Field::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao
    abstract val quoteDao: QuoteDao
    abstract val fieldDao: FieldDao

    companion object {
        const val DATABASE_NAME = "MedTekLocalDB"
    }
}
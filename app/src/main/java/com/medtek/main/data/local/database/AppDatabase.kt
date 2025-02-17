package com.medtek.main.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medtek.main.data.local.dao.DayPlanDao
import com.medtek.main.data.local.dao.FieldDao
import com.medtek.main.data.local.dao.HabitDao
import com.medtek.main.data.local.dao.NewsDao
import com.medtek.main.data.local.dao.PlanDao
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.dao.UserDao
import com.medtek.main.data.local.dao.WeatherDao
import com.medtek.main.data.local.entities.DayPlan
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.data.local.entities.News
import com.medtek.main.data.local.entities.Plan
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.local.entities.User
import com.medtek.main.data.local.entities.Weather
import com.medtek.main.utilties.Converters

@Database(
    entities = [
        Weather::class,
        Quote::class,
        Field::class,
        User::class,
        Plan::class,
        DayPlan::class,
        Habit::class,
        News::class
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao
    abstract val quoteDao: QuoteDao
    abstract val fieldDao: FieldDao
    abstract val userDao: UserDao
    abstract val planDao: PlanDao
    abstract val dayPlanDao: DayPlanDao
    abstract val habitDao: HabitDao
    abstract val newsDao: NewsDao


    companion object {
        const val DATABASE_NAME = "MedTekLocalDB"
    }
}
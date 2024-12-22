package com.medtek.main.di

import android.app.Application
import androidx.room.Room
import com.medtek.main.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao

    @Provides
    fun provideQuoteDao(db: AppDatabase) = db.quoteDao

}
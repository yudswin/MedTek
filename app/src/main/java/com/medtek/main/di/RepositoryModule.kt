package com.medtek.main.di

import com.medtek.main.data.repository.core.HabitRepository
import com.medtek.main.data.repository.core.HabitRepositoryImpl
import com.medtek.main.data.repository.core.UserRepository
import com.medtek.main.data.repository.core.UserRepositoryImpl
import com.medtek.main.data.repository.greeting.QuoteRepository
import com.medtek.main.data.repository.greeting.QuoteRepositoryImpl
import com.medtek.main.data.repository.greeting.WeatherRepository
import com.medtek.main.data.repository.greeting.WeatherRepositoryImpl
import com.medtek.main.data.repository.survey.AuthRepository
import com.medtek.main.data.repository.survey.AuthRepositoryImpl
import com.medtek.main.data.repository.survey.FieldRepository
import com.medtek.main.data.repository.survey.FieldRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(
        impl: QuoteRepositoryImpl
    ): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindFieldRepository(
        impl: FieldRepositoryImpl
    ): FieldRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindHabitRepository(
        impl: HabitRepositoryImpl
    ): HabitRepository
}
package com.medtek.main.di

import com.medtek.main.data.remote.services.AuthService
import com.medtek.main.data.remote.services.FieldService
import com.medtek.main.data.remote.services.QuoteService
import com.medtek.main.data.remote.services.UserService
import com.medtek.main.data.remote.services.WeatherService
import com.medtek.main.utilties.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @MainRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(@MainRetrofit retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideQuoteService(@MainRetrofit retrofit: Retrofit): QuoteService {
        return retrofit.create(QuoteService::class.java)
    }

    @Provides
    @Singleton
    fun provideFieldService(@MainRetrofit retrofit: Retrofit): FieldService {
        return retrofit.create(FieldService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(@MainRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(@MainRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}
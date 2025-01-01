package com.medtek.main.di

import com.medtek.main.utilties.Constants.ALT_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AltNetworkModule {

    @Provides
    @Singleton
    fun provideAltRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ALT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

//    @Provides
//    @Singleton
//    fun provideNewsService(retrofit: Retrofit): NewsService {
//        return retrofit.create(NewsService::class.java)
//    }

}
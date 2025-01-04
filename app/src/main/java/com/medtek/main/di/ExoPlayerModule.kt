package com.medtek.main.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ExoPlayerModule {

    @ServiceScoped
    @Provides
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return SimpleExoPlayer.Builder(context).build()
    }
}
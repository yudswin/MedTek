package com.medtek.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.medtek.main.core.HomeActivity
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.remote.network.RetrofitClient
import com.medtek.main.data.remote.services.QuoteService
import com.medtek.main.data.remote.services.WeatherService
import com.medtek.main.data.repository.greeting.QuoteRepository
import com.medtek.main.data.repository.weather.WeatherRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "MedTekLocalDB"
        ).build()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}

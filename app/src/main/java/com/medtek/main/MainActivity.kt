package com.medtek.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.medtek.main.core.HomeActivity
import com.medtek.main.core.presentation.home.HabitScreen
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Weather
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "MedTekLocalDB",
        ).build()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}


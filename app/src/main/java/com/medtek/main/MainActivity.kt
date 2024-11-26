package com.medtek.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.medtek.main.core.HomeActivity
import com.medtek.main.core.presentation.home.HabitScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}


package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val ritualsHistory: List<String> = emptyList<String>(),
    val surveyHistory: List<Survey>? = null,
    val notificationHistory: List<String>? = null,
    val ritualsOverview: Rituals? = null,
    val city: String? = null,
    val isPriorityUser: Boolean? = null,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
)





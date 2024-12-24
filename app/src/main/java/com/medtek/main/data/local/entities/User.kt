package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val ritualsHistory: List<DailyRitual>? = null,
    val surveyHistory: List<Survey>? = null,
    val notificationHistory: List<String>? = null,
    val ritualsOverview: Rituals? = null,
    val city: String? = null,
    val isPriorityUser: Boolean? = null
)

data class DailyRitual(
    val id: String,
    val name: String,
    val completed: Boolean
)

data class Survey(
    val id: String,
    val question: String,
    val answer: String
)

data class Rituals(
    val totalCompleted: Int,
    val streak: Int
)
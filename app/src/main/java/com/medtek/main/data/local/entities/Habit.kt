package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = DayPlan::class,
            parentColumns = ["id"],
            childColumns = ["dayPlanId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Habit(
    @PrimaryKey
    val trackingId: String,
    val dayPlanId: Int,
    val habitName: String,
    val habitType: String,  // e.g., "DAILY", "WEEKLY", "MONTHLY"
    val defaultScore: Int,
    val description: String,
    val targetUnit: String, // e.g., "HOUR", "MINUTES"
    val progress: Int,
    val goal: Int,
    val icon: String       // e.g., "💡"
)
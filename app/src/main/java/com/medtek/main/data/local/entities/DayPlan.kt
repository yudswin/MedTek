package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "day_plans",
    foreignKeys = [
        ForeignKey(
            entity = Plan::class,
            parentColumns = ["planId"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DayPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val planId: String,
    val userId: String,
    val date: String // Format: YYYY-MM-DD
)

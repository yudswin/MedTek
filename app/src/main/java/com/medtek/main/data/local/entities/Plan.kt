package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey
    val planId: String,
    val startDate: String, // Format: YYYY-MM-DD
    val endDate: String,   // Format: YYYY-MM-DD
)
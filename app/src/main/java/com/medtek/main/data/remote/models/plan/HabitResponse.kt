package com.medtek.main.data.remote.models.plan

data class HabitResponse(
    val trackingId: String,
    val habitName: String,
    val habitType: String,
    val defaultScore: Int,
    val description: String,
    val targetUnit: String,
    val progress: Int,
    val goal: Int,
    val icon: String
)
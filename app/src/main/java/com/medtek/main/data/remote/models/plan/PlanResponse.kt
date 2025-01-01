package com.medtek.main.data.remote.models.plan

data class PlanResponse(
    val planId: String,
    val startDate: String,
    val endDate: String,
    val dailyPlans: Map<String, List<HabitResponse>>
)
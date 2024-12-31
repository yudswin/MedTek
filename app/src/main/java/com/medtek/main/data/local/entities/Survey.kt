package com.medtek.main.data.local.entities

import java.util.UUID
import java.util.Date

data class Survey(
    val id: String = UUID.randomUUID().toString(),
    val workFields: List<String>,
    val sportFields: List<String>,
    val hobbies: List<String>,
    val timeUsingPhone: Int,
    val exerciseTimePerWeek: Int,
    val addedDate: Date = Date()
)
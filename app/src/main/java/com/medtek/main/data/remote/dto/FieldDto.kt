package com.medtek.main.data.remote.dto

data class FieldDto(
    val _id: String,
    val configName: String,
    val configValue: List<String>,
    val isActive: Boolean
)

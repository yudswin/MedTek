package com.medtek.main.data.remote.models

data class FieldRemote(
    val _id: String,
    val configName: String,
    val configValue: List<String>,
    val isActive: Boolean
)

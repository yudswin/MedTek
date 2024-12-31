package com.medtek.main.data.remote.models

data class FieldResponseItem(
    val configName: String,
    val configValue: List<ConfigValue>
)
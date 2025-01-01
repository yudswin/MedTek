package com.medtek.main.data.remote.models.survey

data class FieldResponseItem(
    val configName: String,
    val configValue: List<ConfigValue>
)
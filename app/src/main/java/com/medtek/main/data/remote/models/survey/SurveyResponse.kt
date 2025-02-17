package com.medtek.main.data.remote.models.survey

data class SurveyResponse(
    val acknowledged: Boolean,
    val matchedCount: Int,
    val modifiedCount: Int,
    val upsertedCount: Int,
    val upsertedId: Any
)
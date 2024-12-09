package com.medtek.main.data.remote.models

data class QuoteRemote(
    val id: String,
    val weeklyQuotes: List<DailyQuote>,
    val createdAt: String,
    val updateAt: String
)



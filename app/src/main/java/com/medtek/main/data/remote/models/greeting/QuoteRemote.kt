package com.medtek.main.data.remote.models.greeting

data class QuoteRemote(
    val _id: String,
    val weeklyQuotes: List<WeeklyQuote>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
) {
    data class WeeklyQuote(
        val content: String,
        val author: String,
        val htmlContent: String,
        val _id: String
    )
}
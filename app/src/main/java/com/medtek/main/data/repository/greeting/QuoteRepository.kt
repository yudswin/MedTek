package com.medtek.main.data.repository.greeting

import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.remote.services.QuoteService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuoteRepository(
    private val quoteDao: QuoteDao,
    private val quoteService: QuoteService
) {

    suspend fun fetchAndStoreQuotes(limit: Int) {
        try {
            val response = quoteService.getLatestQuotes(limit)
            if (response.isSuccessful) {
                val quotesRemote = response.body() ?: emptyList()
                val quotes = quotesRemote.flatMap { quoteRemote ->
                    quoteRemote.weeklyQuotes.map { weeklyQuote ->
                        Quote(
                            id = 0,
                            quote = weeklyQuote.content,
                            author = weeklyQuote.author,
                            dateAdded = quoteRemote.createdAt,
                            dateUsed = null
                        )
                    }
                }
                quoteDao.insertQuotes(quotes)
            } else {
                throw Exception("Failed to fetch quotes. HTTP code: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error during API call: ${e.message}", e)
        }
    }

    suspend fun getNextQuote(): Quote? {
        val nextQuote = quoteDao.getNextQuote()
        return nextQuote ?: run {
            quoteDao.resetQuoteQueue()
            quoteDao.getNextQuote()
        }
    }

    suspend fun getUnusedQuotes(count: Int): List<Quote> {
        return quoteDao.getUnusedQuotes(count)
    }

    suspend fun markQuoteAsUsed(quote: Quote) {
        val currentDate = getCurrentDate()
        quoteDao.markQuoteAsUsed(quote.id, currentDate)
    }

    private fun getCurrentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    }
}

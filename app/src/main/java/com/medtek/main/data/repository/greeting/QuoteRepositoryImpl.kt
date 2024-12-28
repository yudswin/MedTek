package com.medtek.main.data.repository.greeting

import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.remote.services.QuoteService
import com.medtek.main.utilties.Resource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class QuoteRepositoryImpl @Inject constructor(
    private val dao: QuoteDao,
    private val api: QuoteService
) : QuoteRepository {

    override suspend fun fetchAndStoreQuotes(limit: Int): Resource<Unit> {
        val response = try {
            api.getLatestQuotes(limit)
        } catch (e: Exception) {
            return Resource.Error("Error during API call: ${e.message}")
        }

        if (!response.isSuccessful) {
            return Resource.Error("Failed to fetch quotes. HTTP code: ${response.code()}")
        }

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

        return try {
            dao.insertQuotes(quotes)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error storing quotes: ${e.message}")
        }
    }

    override suspend fun getNextQuote(): Resource<Quote?> {
        val nextQuote = try {
            dao.getNextQuote()
        } catch (e: Exception) {
            return Resource.Error("Error fetching next quote: ${e.message}")
        }

        if (nextQuote == null) {
            try {
                dao.resetQuoteQueue()
            } catch (e: Exception) {
                return Resource.Error("Error resetting quote queue: ${e.message}")
            }

            val resetQuote = try {
                dao.getNextQuote()
            } catch (e: Exception) {
                return Resource.Error("Error fetching next quote after reset: ${e.message}")
            }

            return Resource.Success(resetQuote)
        }

        return Resource.Success(nextQuote)
    }

    override suspend fun getUnusedQuotes(count: Int): Resource<List<Quote>> {
        val unusedQuotes = try {
            dao.getUnusedQuotes(count)
        } catch (e: Exception) {
            return Resource.Error("Error fetching unused quotes: ${e.message}")
        }

        return Resource.Success(unusedQuotes)
    }

    override suspend fun markQuoteAsUsed(quote: Quote): Resource<Unit> {
        val currentDate = getCurrentDate()
        return try {
            dao.markQuoteAsUsed(quote.id, currentDate)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error marking quote as used: ${e.message}")
        }
    }
}


private fun getCurrentDate(): String {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE)
}
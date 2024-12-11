package com.medtek.main

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Quote
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class QuoteDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var quoteDao: QuoteDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        quoteDao = database.quoteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertQuotesAndGetUnusedQuotes() = runBlocking {
        val sampleQuotes = listOf(
            Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null),
            Quote(id = 2, quote = "Quote 2", author = "Author 2", dateAdded = "2024-12-11", dateUsed = null)
        )

        quoteDao.insertQuotes(sampleQuotes)
        val unusedQuotes = quoteDao.getUnusedQuotes(2)

        assertEquals(2, unusedQuotes.size)
        assertEquals(sampleQuotes[0], unusedQuotes[0])
        assertEquals(sampleQuotes[1], unusedQuotes[1])
    }

    @Test
    fun getUnusedQuoteCount() = runBlocking {
        val sampleQuotes = listOf(
            Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null),
            Quote(id = 2, quote = "Quote 2", author = "Author 2", dateAdded = "2024-12-11", dateUsed = null)
        )

        quoteDao.insertQuotes(sampleQuotes)
        val count = quoteDao.getUnusedQuoteCount()

        assertEquals(2, count)
    }

    @Test
    fun updateQuote() = runBlocking {
        val sampleQuote = Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null)
        quoteDao.insertQuotes(listOf(sampleQuote))

        val updatedQuote = sampleQuote.copy(quote = "Updated Quote 1")
        quoteDao.updateQuote(updatedQuote)
        val unusedQuotes = quoteDao.getUnusedQuotes(1)

        assertEquals(updatedQuote, unusedQuotes[0])
    }

    @Test
    fun deleteOldQuotes() = runBlocking {
        val sampleQuotes = listOf(
            Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null),
            Quote(id = 2, quote = "Quote 2", author = "Author 2", dateAdded = "2024-12-11", dateUsed = null)
        )

        quoteDao.insertQuotes(sampleQuotes)
        quoteDao.deleteOldQuotes("2024-12-11")
        val unusedQuotes = quoteDao.getUnusedQuotes(2)

        assertEquals(1, unusedQuotes.size)
        assertEquals(sampleQuotes[1], unusedQuotes[0])
    }

    @Test
    fun getNextQuote() = runBlocking {
        val sampleQuote = Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null)
        quoteDao.insertQuotes(listOf(sampleQuote))

        val nextQuote = quoteDao.getNextQuote()

        assertEquals(sampleQuote, nextQuote)
    }

    @Test
    fun markQuoteAsUsed() = runBlocking {
        val sampleQuote = Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = null)
        quoteDao.insertQuotes(listOf(sampleQuote))

        quoteDao.markQuoteAsUsed(1, "2024-12-11")
        val nextQuote = quoteDao.getNextQuote()

        assertEquals(null, nextQuote)
    }

    @Test
    fun resetQuoteQueue() = runBlocking {
        val sampleQuote = Quote(id = 1, quote = "Quote 1", author = "Author 1", dateAdded = "2024-12-10", dateUsed = "2024-12-11")
        quoteDao.insertQuotes(listOf(sampleQuote))

        quoteDao.resetQuoteQueue()
        val nextQuote = quoteDao.getNextQuote()

        assertEquals(sampleQuote.copy(dateUsed = null), nextQuote)
    }
}
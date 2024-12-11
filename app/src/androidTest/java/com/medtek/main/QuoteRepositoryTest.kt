package com.medtek.main

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.database.AppDatabase
import com.medtek.main.data.local.entities.Quote
import com.medtek.main.data.remote.models.QuoteRemote
import com.medtek.main.data.remote.services.QuoteService
import com.medtek.main.data.repository.greeting.QuoteRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
@SmallTest
class QuoteRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var quoteDao: QuoteDao
    private lateinit var quoteService: QuoteService
    private lateinit var quoteRepository: QuoteRepository

    @Before
    fun setup() {
        // Initialize in-memory Room database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        quoteDao = database.quoteDao()

        // Mock the QuoteService using Mockito
        quoteService = Mockito.mock(QuoteService::class.java)

        // Initialize repository with the mocked service and DAO
        quoteRepository = QuoteRepository(quoteDao, quoteService)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun fetchAndStoreQuotes() = runBlocking {
        // Define mocked response
        val quoteRemote = QuoteRemote(
            _id = "6750ed02c0781b8a77a39c57",
            weeklyQuotes = listOf(
                QuoteRemote.WeeklyQuote(
                    content = "Success is not what you do when you are on top. Success is how high you bounce when you hit the bottom.",
                    author = "Sonia Ricotti",
                    htmlContent = "<blockquote>&ldquo;Success is not what you do when you are on top. Success is how high you bounce when you hit the bottom.&rdquo; &mdash; <footer>Sonia Ricotti</footer></blockquote>",
                    _id = "6750ed02c0781b8a77a39c58"
                )
            ),
            createdAt = "2024-12-05T00:00:02.750Z",
            updatedAt = "2024-12-05T00:00:02.750Z",
            __v = 0
        )

        // Mock service response
        `when`(quoteService.getLatestQuotes(1)).thenReturn(
            retrofit2.Response.success(listOf(quoteRemote))
        )

        // Test repository method
        quoteRepository.fetchAndStoreQuotes(1)

        // Validate database contents
        val storedQuotes = quoteDao.getUnusedQuotes(1)

        // Assertions
        assertEquals(1, storedQuotes.size) // Check number of stored quotes
        assertEquals(
            quoteRemote.weeklyQuotes[0].content,
            storedQuotes[0].quote
        ) // Validate content
        assertEquals(
            quoteRemote.weeklyQuotes[0].author,
            storedQuotes[0].author
        ) // Validate author
        assertEquals(
            quoteRemote.createdAt,
            storedQuotes[0].dateAdded
        ) // Validate creation date
    }
}

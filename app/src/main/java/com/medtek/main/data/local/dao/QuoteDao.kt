package com.medtek.main.data.local.dao

import com.medtek.main.data.local.entities.Quote
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuoteDao {
    @Query("SELECT * FROM quotes WHERE dateUsed IS NULL LIMIT :count")
    suspend fun getUnusedQuotes(count: Int): List<Quote>

    @Query("SELECT COUNT(*) FROM quotes WHERE dateUsed IS NULL")
    suspend fun getUnusedQuoteCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<Quote>)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("DELETE FROM quotes WHERE dateAdded < :oldestDate")
    suspend fun deleteOldQuotes(oldestDate: String)

    // Fetch the next unused quote
    @Query("SELECT * FROM quotes WHERE dateUsed IS NULL ORDER BY dateAdded ASC LIMIT 1")
    suspend fun getNextQuote(): Quote?

    // Mark a quote as used
    @Query("UPDATE quotes SET dateUsed = :currentDate WHERE id = :quoteId")
    suspend fun markQuoteAsUsed(quoteId: Int, currentDate: String)

    // Reset the queue when all quotes are used
    @Query("UPDATE quotes SET dateUsed = NULL")
    suspend fun resetQuoteQueue()
}
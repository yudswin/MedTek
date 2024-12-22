package com.medtek.main.data.repository.greeting


import com.medtek.main.data.local.entities.Quote
import com.medtek.main.utilties.Resource


interface QuoteRepository {

    suspend fun fetchAndStoreQuotes(limit: Int): Resource<Unit>

    suspend fun getNextQuote(): Resource<Quote?>

    suspend fun getUnusedQuotes(count: Int): Resource<List<Quote>>

    suspend fun markQuoteAsUsed(quote: Quote): Resource<Unit>


}


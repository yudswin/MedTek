package com.medtek.main.data.repository.greeting

import com.medtek.main.data.local.dao.QuoteDao
import com.medtek.main.data.local.entities.Quote

//class QuoteRepository(private val quoteDao: QuoteDao) {
//
//    suspend fun getNextQuote(): Quote? {
//        val nextQuote = quoteDao.getNextQuote()
//        if (nextQuote == null) {
//            quoteDao.resetQuoteQueue()
//            return quoteDao.getNextQuote()
//        }
//        return nextQuote
//    }
//
//    suspend fun markQuoteAsUsed(quote: Quote) {
//        val currentDate = getCurrentDate()
//        quoteDao.markQuoteAsUsed(quote.id, currentDate)
//    }
//
//    private fun getCurrentDate(): String {
//        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
//        return sdf.format(java.util.Date())
//    }
//}
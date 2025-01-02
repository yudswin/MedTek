package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.medtek.main.data.local.entities.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY date DESC")
    suspend fun getNews(): List<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<News>)

    @Update
    suspend fun updateNews(news: News)

    @Query("DELETE FROM news WHERE date < :oldestDate")
    suspend fun deleteOldNews(oldestDate: String)

}
package com.medtek.main.data.remote.models.news

import com.medtek.main.data.local.entities.News

data class NewsResponse (
    val message: String,
    val count: Int,
    val data: List<News>
)
{
    data class News(
        val author: String,
        val content: String,
        val date: String,
        val title: String,
        val sourceName: String
    )
}
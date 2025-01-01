package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class News (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val author: String,
    val content: String,
    val date: String,
    val title: String,
    val sourceName: String,
)
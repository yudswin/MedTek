package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val quote: String,
    val author: String?,
    val dateUsed: String?,
    val dateAdded: String
)
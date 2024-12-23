package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configFields")
data class Field(
    @PrimaryKey val id: String,
    val configName: String,
    val configValue: String,
    val isActive: Boolean
)

//{
//    companion object {
//        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
//    }
//}
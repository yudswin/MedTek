package com.medtek.main.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.medtek.main.data.remote.models.ConfigValue

@Entity(tableName = "configFields")
data class Field(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val configName: String,
    val configValues: List<ConfigValue>,
    val isActive: Boolean,
    val color: String? = null
)
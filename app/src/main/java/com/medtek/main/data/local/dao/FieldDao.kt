package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.Field

@Dao
interface FieldDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigs(configs: List<Field>)

    @Query("SELECT * FROM configFields")
    suspend fun getAllConfigs(): List<Field>

    @Query("SELECT * FROM configFields WHERE configName IS :config")
    suspend fun getConfigsByName(config: String): Field

    @Query("SELECT configName FROM configFields")
    suspend fun getAllConfigNames(): List<String>

    @Query("SELECT COUNT(*) FROM configFields")
    suspend fun getConfigCount(): Int
}
package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.medtek.main.data.local.entities.DayPlan

@Dao
interface DayPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayPlan(dayPlan: DayPlan): Long
}
package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.DayPlan

@Dao
interface DayPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayPlan(dayPlan: DayPlan): Long

    @Query("SELECT * FROM day_plans WHERE date = :date")
    suspend fun getDayPlansByDate(date: String): List<DayPlan>

    @Query("SELECT * FROM day_plans WHERE userId = :userId")
    suspend fun getAllDayPlansByUserId(userId: String): List<DayPlan>

    @Query("SELECT date FROM day_plans WHERE planId = :planId")
    suspend fun getAllDayPlanDatesByPlanId(planId: String): List<String>
}
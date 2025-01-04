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

    @Query("SELECT id FROM day_plans WHERE date = :date")
    suspend fun getDayPlanIdByDate(date: String): Int?

    @Query(
        """
    SELECT day_plans.date
    FROM day_plans
    INNER JOIN habits ON habits.dayPlanId = day_plans.id
    WHERE day_plans.userId = :userId
    GROUP BY day_plans.date
    HAVING COUNT(habits.trackingId) = SUM(CASE WHEN (habits.progress / habits.goal) = 1 THEN 1 ELSE 0 END)
    """
    )
    suspend fun getCompletedDates(userId: String): List<String>
}
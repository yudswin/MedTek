package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.Habit

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE dayPlanId = :dayPlanId")
    suspend fun getHabitsByDayPlanId(dayPlanId: Int): List<Habit>

    @Query("UPDATE habits SET progress = :progress WHERE trackingId = :trackingId")
    suspend fun updateProgress(trackingId: String, progress: Int)

    @Query("SELECT * FROM habits WHERE trackingId = :trackingId")
    suspend fun getHabitById(trackingId: String): Habit?

    @Query("SELECT COUNT(*) FROM habits WHERE dayPlanId = :dayPlanId AND progress >= goal")
    suspend fun getProgressByDayPlanId(dayPlanId: Int): Int

    @Query(
        """
            SELECT COUNT(*)
            FROM habits 
            INNER JOIN day_plans ON habits.dayPlanId = day_plans.id
            WHERE day_plans.userId = :userId AND (habits.progress / habits.goal) = 1
        """
    )
    suspend fun getTotalHabitsDone(userId: String): Int

}

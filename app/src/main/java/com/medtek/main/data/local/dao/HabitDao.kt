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
}

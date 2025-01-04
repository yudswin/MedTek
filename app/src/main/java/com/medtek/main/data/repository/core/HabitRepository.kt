package com.medtek.main.data.repository.core

import com.medtek.main.data.local.entities.DayPlan
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.utilties.Resource
import java.time.LocalDate

interface HabitRepository {
    suspend fun insertHabit(habit: Habit)

    suspend fun getHabitsByDayPlanId(dayPlanId: Int): Resource<List<Habit>>

    suspend fun insertDayPlan(dayPlan: DayPlan): Resource<Long>

    suspend fun getDayPlansByDate(date: String): Resource<List<DayPlan>>

    suspend fun getAllDayPlansByUserId(userId: String): Resource<List<DayPlan>>

    suspend fun updateProgressByHabitId(trackingId: String, amount: Int): Resource<Unit>

    suspend fun getProgressForDate(date: LocalDate): Resource<Float>

    suspend fun getHabitsForDate(date: LocalDate): Resource<List<Habit>>

    suspend fun getTotalHabitsDone(userId: String): Resource<Int>

    suspend fun getCurrentStreak(userId: String): Resource<Int>

    suspend fun updateAndCheckStreak(userId: String): Resource<Unit>
}
package com.medtek.main.data.repository.core

import android.util.Log
import com.medtek.main.data.local.dao.DayPlanDao
import com.medtek.main.data.local.dao.HabitDao
import com.medtek.main.data.local.entities.DayPlan
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.utilties.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val dayPlanDao: DayPlanDao

) : HabitRepository {
    override suspend fun insertHabit(habit: Habit) {
        Log.d("HabitRepositoryImpl", "insertHabit: Inserting habit started")
        habitDao.insertHabit(habit)
        Log.d("HabitRepositoryImpl", "insertHabit: Inserting habit completed")
    }

    override suspend fun getHabitsByDayPlanId(dayPlanId: Int): Resource<List<Habit>> {
        Log.d(
            "HabitRepositoryImpl",
            "getHabitsByDayPlanId: Fetching habits for dayPlanId $dayPlanId started"
        )
        return try {
            val habits = habitDao.getHabitsByDayPlanId(dayPlanId)
            Log.d(
                "HabitRepositoryImpl",
                "getHabitsByDayPlanId: Fetching habits completed successfully"
            )
            Resource.Success(habits)
        } catch (e: Exception) {
            Log.e(
                "HabitRepositoryImpl",
                "getHabitsByDayPlanId: Error fetching habits - ${e.localizedMessage}"
            )
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun insertDayPlan(dayPlan: DayPlan): Resource<Long> {
        Log.d("HabitRepositoryImpl", "insertDayPlan: Inserting day plan started")
        return try {
            val id = dayPlanDao.insertDayPlan(dayPlan)
            Log.d(
                "HabitRepositoryImpl",
                "insertDayPlan: Inserting day plan completed successfully with ID $id"
            )
            Resource.Success(id)
        } catch (e: Exception) {
            Log.e(
                "HabitRepositoryImpl",
                "insertDayPlan: Error inserting day plan - ${e.localizedMessage}"
            )
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getDayPlansByDate(date: String): Resource<List<DayPlan>> {
        Log.d("HabitRepositoryImpl", "getDayPlansByDate: Fetching day plans for date $date started")
        return try {
            val dayPlans = dayPlanDao.getDayPlansByDate(date)
            Log.d(
                "HabitRepositoryImpl",
                "getDayPlansByDate: Fetching day plans completed successfully"
            )
            Resource.Success(dayPlans)
        } catch (e: Exception) {
            Log.e(
                "HabitRepositoryImpl",
                "getDayPlansByDate: Error fetching day plans - ${e.localizedMessage}"
            )
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun getAllDayPlansByUserId(userId: String): Resource<List<DayPlan>> {
        return withContext(Dispatchers.IO) {
            Log.d(
                "HabitRepositoryImpl",
                "getAllDayPlansByUserId: Fetching all day plans for userId $userId started"
            )
            try {
                val dayPlans = dayPlanDao.getAllDayPlansByUserId(userId)
                Log.d(
                    "HabitRepositoryImpl",
                    "getAllDayPlansByUserId: Fetching all day plans completed successfully"
                )
                Resource.Success(dayPlans)
            } catch (e: Exception) {
                Log.e(
                    "HabitRepositoryImpl",
                    "getAllDayPlansByUserId: Error fetching all day plans - ${e.localizedMessage}"
                )
                Resource.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun updateProgressByHabitId(trackingId: String, amount: Int): Resource<Unit> {
        Log.d(
            "HabitRepositoryImpl",
            "updateProgressByHabitId: Updating progress for habitId $trackingId started"
        )
        return try {
            val habit = habitDao.getHabitById(trackingId)
                ?: return Resource.Error("Habit with ID $trackingId not found")

            val newProgress = (habit.progress + amount).coerceIn(0, habit.goal)
            habitDao.updateProgress(trackingId, newProgress)
            Log.d(
                "HabitRepositoryImpl",
                "updateProgressByHabitId: Updating progress completed successfully"
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(
                "HabitRepositoryImpl",
                "updateProgressByHabitId: Error updating progress - ${e.localizedMessage}"
            )
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }
}
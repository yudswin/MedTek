package com.medtek.main.data.repository.core

import android.util.Log
import com.medtek.main.data.local.dao.DayPlanDao
import com.medtek.main.data.local.dao.HabitDao
import com.medtek.main.data.local.dao.PlanDao
import com.medtek.main.data.local.dao.UserDao
import com.medtek.main.data.local.entities.DayPlan
import com.medtek.main.data.local.entities.Habit
import com.medtek.main.data.local.entities.Plan
import com.medtek.main.data.local.entities.Survey
import com.medtek.main.data.local.entities.User
import com.medtek.main.data.remote.services.UserService
import com.medtek.main.utilties.Resource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val api: UserService,
    private val planDao: PlanDao,
    private val dayPlanDao: DayPlanDao,
    private val habitDao: HabitDao
) : UserRepository {

    override suspend fun getUserId(): Resource<String> {
        Log.d("UserRepositoryImpl", "getUserId: Fetching user ID started")
        val response = try {
            dao.getUserID()
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "getUserId: Error fetching user ID - ${e.message}")
            return Resource.Error("getUserId failed: $e")
        }
        Log.d("UserRepositoryImpl", "getUserId: Fetching user ID successful")
        return Resource.Success(response)
    }

    override suspend fun addSurvey(userId: String, survey: Survey): Resource<Unit> {
        Log.d("UserRepositoryImpl", "addSurvey: Adding survey to user $userId started")
        return try {
            dao.addSurveyToUser(userId, survey)
            Log.d("UserRepositoryImpl", "addSurvey: Survey added successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "addSurvey: Error adding survey - ${e.message}")
            Resource.Error("Error adding survey: ${e.message}")
        }
    }

    override suspend fun clearSurvey(userId: String): Resource<Unit> {
        Log.d("UserRepositoryImpl", "clearSurvey: Clearing survey history for user $userId started")
        return try {
            dao.clearAllSurveyHistory(userId)
            Log.d("UserRepositoryImpl", "clearSurvey: Survey history cleared successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "clearSurvey: Error clearing survey history - ${e.message}")
            Resource.Error("Error clearing survey history: ${e.message}")
        }
    }

    override suspend fun updateSurvey(userId: String, surveyHistory: List<Survey>): Resource<Unit> {
        Log.d(
            "UserRepositoryImpl",
            "updateSurvey: Updating survey history for user $userId started"
        )
        return try {
            dao.updateUserSurvey(userId, surveyHistory)
            Log.d("UserRepositoryImpl", "updateSurvey: Survey history updated successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(
                "UserRepositoryImpl",
                "updateSurvey: Error updating survey history - ${e.message}"
            )
            Resource.Error("Error updating survey history: ${e.message}")
        }
    }

    override suspend fun addSurveyCurrentUser(survey: Survey): Resource<Unit> {
        Log.d("UserRepositoryImpl", "addSurveyCurrentUser: Adding survey to current user started")
        val userId = when (val result = getUserId()) {
            is Resource.Success -> result.data
            is Resource.Error -> {
                Log.e(
                    "UserRepositoryImpl",
                    "addSurveyCurrentUser: Error getting user ID - ${result.message}"
                )
                return Resource.Error("UserId get failed: ${result.message}")
            }
        } ?: return Resource.Error("UserId is null")

        return try {
            addSurvey(userId, survey)
            Log.d(
                "UserRepositoryImpl",
                "addSurveyCurrentUser: Survey added to current user successfully"
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", "addSurveyCurrentUser: Error adding survey - ${e.message}")
            Resource.Error("Error adding survey: ${e.message}")
        }
    }

    override suspend fun isUserDoSurvey(userId: String): Resource<Boolean> {
        Log.d(
            "UserRepositoryImpl",
            "isUserDoSurvey: Checking if user $userId has done survey started"
        )
        return try {
            val surveyHistoryCount = dao.getSurveyHistoryCount(userId)
            if (surveyHistoryCount > 0) {
                Log.d("UserRepositoryImpl", "isUserDoSurvey: User $userId has done survey")
                Resource.Success(true)
            } else {
                Log.d("UserRepositoryImpl", "isUserDoSurvey: User $userId has not done any survey")
                Resource.Success(false)
            }
        } catch (e: Exception) {
            Log.e(
                "UserRepositoryImpl",
                "isUserDoSurvey: Error checking survey history - ${e.message}"
            )
            Resource.Error("Error checking survey history: ${e.message}")
        }
    }

    override suspend fun isCurrentUserDoSurvey(): Resource<Boolean> {
        Log.d(
            "UserRepositoryImpl",
            "isCurrentUserDoSurvey: Checking if current user has done survey started"
        )
        val userId = when (val result = getUserId()) {
            is Resource.Success -> result.data
            is Resource.Error -> {
                Log.e(
                    "UserRepositoryImpl",
                    "isCurrentUserDoSurvey: Error getting user ID - ${result.message}"
                )
                return Resource.Error("UserId get failed: ${result.message}")
            }
        } ?: return Resource.Error("UserId is null")

        return isUserDoSurvey(userId)
    }

    override suspend fun savePlanResponse(userId: String): Resource<Unit> {
        Log.d(
            "UserRepositoryImpl",
            "savePlanResponse: Fetching plan response for user $userId started"
        )
        return try {
            val response = api.fetchUserPlan(userId)

            response.body()?.let { planResponse ->
                if (planDao.isPlanExists(planResponse.planId)) {
                    Log.d(
                        "UserRepositoryImpl",
                        "savePlanResponse: Plan already exists, skipping insertion"
                    )
                    return Resource.Success(Unit)
                }

                val plan = Plan(
                    planId = planResponse.planId,
                    startDate = planResponse.startDate,
                    endDate = planResponse.endDate
                )
                planDao.insertPlan(plan)
                dao.insertPlan(userId, planResponse.planId)
                Log.d("UserRepositoryImpl", "savePlanResponse: Plan inserted successfully")

                val formatter = DateTimeFormatter.ISO_DATE
                val startDate = LocalDate.parse(planResponse.startDate, formatter)

                var currentDate = startDate
                planResponse.dailyPlans.forEach { (_, habits) ->
                    val dayPlan = DayPlan(
                        userId = userId,
                        planId = planResponse.planId,
                        date = currentDate.toString()
                    )
                    val dayPlanId = dayPlanDao.insertDayPlan(dayPlan).toInt()
                    Log.d(
                        "UserRepositoryImpl",
                        "savePlanResponse: DayPlan inserted successfully with ID $dayPlanId"
                    )

                    habits.forEach { habitResponse ->
                        val habit = Habit(
                            trackingId = habitResponse.trackingId,
                            dayPlanId = dayPlanId,
                            habitName = habitResponse.habitName,
                            habitType = habitResponse.habitType,
                            defaultScore = habitResponse.defaultScore,
                            description = habitResponse.description,
                            targetUnit = habitResponse.targetUnit,
                            progress = habitResponse.progress,
                            goal = habitResponse.goal,
                            icon = habitResponse.icon
                        )
                        habitDao.insertHabit(habit)
                        Log.d(
                            "UserRepositoryImpl",
                            "savePlanResponse: Habit inserted successfully with tracking ID ${habit.trackingId}"
                        )
                    }

                    currentDate = currentDate.plusDays(1)
                }
            } ?: run {
                Log.e("UserRepositoryImpl", "savePlanResponse: Response is null")
                return Resource.Error("Response is null")
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e(
                "UserRepositoryImpl",
                "savePlanResponse: Error saving plan response - ${e.message}"
            )
            Resource.Error("Error saving plan response: ${e.message}")
        }
    }

    override suspend fun hasCurrentWeekPlan(userId: String): Resource<Boolean> {
        if (userId.isBlank()) {
            Log.e("UserRepositoryImpl", "hasCurrentWeekPlan: User ID is null or blank")
            return Resource.Error("User ID cannot be null or blank")
        }

        Log.d(
            "UserRepositoryImpl",
            "hasCurrentWeekPlan: Checking if user $userId has a plan for the current week started"
        )
        return try {
            val currentDate = LocalDate.now()
            val planIds = dao.getRitualsHistory(userId) ?: emptyList()
            if (planIds.isEmpty()) {
                Log.d("UserRepositoryImpl", "hasCurrentWeekPlan: No plans found for user $userId")
                return Resource.Success(false)
            }

            val listDate = mutableListOf<String>()
            planIds.forEach { planId ->
                dayPlanDao.getAllDayPlanDatesByPlanId(planId).forEach { date ->
                    listDate.add(date)
                }
            }

            val hasPlan = listDate.any { date ->
                currentDate == LocalDate.parse(date)
            }

            Log.d(
                "UserRepositoryImpl",
                "hasCurrentWeekPlan: User $userId has a plan for the current week: $hasPlan"
            )
            Resource.Success(hasPlan)
        } catch (e: Exception) {
            Log.e(
                "UserRepositoryImpl",
                "hasCurrentWeekPlan: Error checking current week plan - ${e.message}"
            )
            Resource.Error("Error checking current week plan: ${e.message}")
        }
    }

    override suspend fun getUserById(userId: String): Resource<User> {
        return try {
            val user = dao.getUserById(userId)
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateCurrentStreak(userId: String, currentStreak: Int): Resource<Unit> {
        return try {
            dao.updateCurrentStreak(userId, currentStreak)
            dao.updateLongestStreak(userId, currentStreak)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error updating current streak: ${e.message}")
        }
    }

    override suspend fun getUserLongestStreak(userId: String): Resource<Int> {
        return try {
            val longestStreak = dao.getLongestStreak(userId)
            Resource.Success(longestStreak)
        } catch (e: Exception) {
            Resource.Error("Error fetching longest streak: ${e.message}")
        }
    }
}
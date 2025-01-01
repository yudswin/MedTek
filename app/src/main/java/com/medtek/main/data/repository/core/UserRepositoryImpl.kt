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
import com.medtek.main.data.remote.services.UserService
import com.medtek.main.utilties.Resource
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
        Log.d("UserRepositoryImpl", "addSurvey: Adding survey to user $userId")
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
        Log.d("UserRepositoryImpl", "clearSurvey: Clearing survey history for user $userId")
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
        Log.d("UserRepositoryImpl", "updateSurvey: Updating survey history for user $userId")
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
        Log.d("UserRepositoryImpl", "isUserDoSurvey: Checking if user $userId has done survey")
        return try {
            val surveyHistory = dao.getSurveyHistory(userId)
            if (surveyHistory.isNullOrEmpty()) {
                Log.d("UserRepositoryImpl", "isUserDoSurvey: User $userId has not done any survey")
                Resource.Success(false)
            } else {
                Log.d("UserRepositoryImpl", "isUserDoSurvey: User $userId has done survey")
                Resource.Success(true)
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
        Log.d("UserRepositoryImpl", "savePlanResponse: Fetching plan response for user $userId")
        return try {
            val response = api.fetchUserPlan(userId)

            response.body()?.let {
                val plan = Plan(
                    planId = it.planId,
                    startDate = it.startDate,
                    endDate = it.endDate
                )
                planDao.insertPlan(plan)
                dao.insertPlan(userId, it.planId)
                Log.d("UserRepositoryImpl", "savePlanResponse: Plan inserted successfully")
            }

            response.body()?.let { responseBody ->
                responseBody.dailyPlans.forEach { (dayOfWeek, habits) ->
                    // Insert DayPlan
                    val dayPlan = DayPlan(
                        planId = responseBody.planId,
                        dayOfWeek = dayOfWeek
                    )
                    val dayPlanId = dayPlanDao.insertDayPlan(dayPlan).toInt()
                    Log.d(
                        "UserRepositoryImpl",
                        "savePlanResponse: DayPlan inserted successfully with ID $dayPlanId"
                    )

                    // Insert associated Habits
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

}
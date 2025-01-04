package com.medtek.main.data.local.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.Survey
import com.medtek.main.data.local.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: User)

    @Query("SELECT id FROM user LIMIT 1")
    suspend fun getUserID(): String

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: String): User?

    @Query("SELECT COUNT(*) FROM user WHERE ritualsHistory IS NOT NULL AND surveyHistory IS NOT NULL AND notificationHistory IS NOT NULL AND ritualsOverview IS NOT NULL")
    suspend fun isUserDoneSurvey(): Boolean

    @Query("UPDATE user SET surveyHistory = :survey WHERE id = :userId")
    suspend fun addSurveyToUser(userId: String, survey: Survey)

    @Query("UPDATE user SET surveyHistory = NULL WHERE id = :userId")
    suspend fun clearAllSurveyHistory(userId: String)

    @Query("UPDATE user SET surveyHistory = :surveyHistory WHERE id = :userId")
    suspend fun updateUserSurvey(userId: String, surveyHistory: List<Survey>)

    @Query("SELECT COUNT(surveyHistory) FROM user WHERE id = :userId")
    suspend fun getSurveyHistoryCount(userId: String): Int

    suspend fun insertPlan(userId: String, ritualId: String) {
        val currentHistory = getRitualsHistory(userId)?.toMutableList() ?: mutableListOf()

        if (!currentHistory.contains(ritualId)) {
            currentHistory.add(ritualId)
            updateRitualsHistory(userId, currentHistory)
        } else {
            Log.d(
                "UserDao",
                "insertPlan: Ritual ID $ritualId already exists in ritualsHistory for user $userId"
            )
        }
    }

    @Query("UPDATE user SET ritualsHistory = :ritualsHistory WHERE id = :userId")
    suspend fun updateRitualsHistory(userId: String, ritualsHistory: List<String>)

    @Query("SELECT ritualsHistory FROM user WHERE id = :userId")
    suspend fun getRitualsHistory(userId: String): List<String>?

    @Query("UPDATE user SET ritualsHistory = NULL")
    suspend fun clearAllRitualsHistory()

    @Query("UPDATE user SET currentStreak = :currentStreak WHERE id = :userId")
    suspend fun updateCurrentStreak(userId: String, currentStreak: Int)

    @Query("SELECT longestStreak FROM user WHERE id = :userId")
    suspend fun getLongestStreak(userId: String): Int

    @Query("UPDATE user SET longestStreak = CASE WHEN :longestStreak > longestStreak THEN :longestStreak ELSE longestStreak END WHERE id = :userId")
    suspend fun updateLongestStreak(userId: String, longestStreak: Int?)
}


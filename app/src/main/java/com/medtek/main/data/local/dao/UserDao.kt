package com.medtek.main.data.local.dao

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

    @Query("SELECT COUNT(*) FROM user WHERE ritualsHistory IS NOT NULL AND surveyHistory IS NOT NULL AND notificationHistory IS NOT NULL AND ritualsOverview IS NOT NULL")
    suspend fun isUserDoneSurvey(): Boolean

    @Query("UPDATE user SET surveyHistory = :survey WHERE id = :userId")
    suspend fun addSurveyToUser(userId: String, survey: Survey)

    @Query("UPDATE user SET surveyHistory = NULL WHERE id = :userId")
    suspend fun clearAllSurveyHistory(userId: String)

    @Query("UPDATE user SET surveyHistory = :surveyHistory WHERE id = :userId")
    suspend fun updateUserSurvey(userId: String, surveyHistory: List<Survey>)

    @Query("SELECT surveyHistory FROM user WHERE id = :userId")
    suspend fun getSurveyHistory(userId: String): List<Survey>?
}
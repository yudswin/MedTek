package com.medtek.main.data.repository.core

import com.medtek.main.data.local.entities.Survey
import com.medtek.main.utilties.Resource

interface UserRepository {
    suspend fun getUserId(): Resource<String>

    suspend fun addSurvey(userId: String, survey: Survey): Resource<Unit>

    suspend fun addSurveyCurrentUser(survey: Survey): Resource<Unit>

    suspend fun clearSurvey(userId: String): Resource<Unit>

    suspend fun updateSurvey(userId: String, surveyHistory: List<Survey>): Resource<Unit>

    suspend fun isUserDoSurvey(userId: String): Resource<Boolean>

    suspend fun isCurrentUserDoSurvey(): Resource<Boolean>

    suspend fun savePlanResponse(userId: String): Resource<Unit>
}
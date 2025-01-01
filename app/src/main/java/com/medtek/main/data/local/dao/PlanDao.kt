package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.Plan

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: Plan)

    @Query("SELECT * FROM plans WHERE planId = :planId")
    suspend fun getPlanById(planId: String): Plan?
}

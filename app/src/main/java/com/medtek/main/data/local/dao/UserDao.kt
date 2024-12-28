package com.medtek.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medtek.main.data.local.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: User)

    @Query("SELECT id FROM user LIMIT 1")
    suspend fun getUserID(): String
}
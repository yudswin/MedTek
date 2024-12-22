package com.medtek.main.data.repository.survey

import com.medtek.main.data.local.dao.FieldDao
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.remote.services.FieldService

class FieldRepository(
    private val fieldDao: FieldDao,
    private val fieldService: FieldService
) {
    suspend fun fetchConfigs() {
        try {
            val res = fieldService.getAllField()
            if (res.isSuccessful && res.body() != null) {
                val fieldEntities = res.body()!!.map { dto ->
                    Field(
                        id = dto._id,
                        configName = dto.configName,
                        configValue = dto.configValue,
                        isActive = dto.isActive
                    )
                }
                fieldDao.insertConfigs(fieldEntities)
            } else {
                throw Exception("Failed to fetch quotes. HTTP code: ${res.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error during API call: ${e.message}", e)
        }
    }
}
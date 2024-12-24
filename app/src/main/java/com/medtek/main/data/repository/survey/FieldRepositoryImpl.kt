package com.medtek.main.data.repository.survey

import android.util.Log
import com.medtek.main.data.local.dao.FieldDao
import com.medtek.main.data.local.entities.Field
import com.medtek.main.data.remote.services.FieldService
import com.medtek.main.utilties.Resource
import javax.inject.Inject

class FieldRepositoryImpl @Inject constructor(
    private val fieldDao: FieldDao,
    private val fieldService: FieldService
) : FieldRepository {

    override suspend fun fetchConfigs(): Resource<Unit> {
        Log.d("FieldRepositoryImpl", "fetchConfigs: Fetching configs started")
        val response = try {
            fieldService.getAllField()
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "fetchConfigs: Error during API call - ${e.message}")
            return Resource.Error("Error during API call: ${e.message}")
        }

        if (!response.isSuccessful) {
            Log.e(
                "FieldRepositoryImpl",
                "fetchConfigs: Failed to fetch configs. HTTP code: ${response.code()}"
            )
            return Resource.Error("Failed to fetch configs. HTTP code: ${response.code()}")
        }

        val fieldEntities = response.body()?.map { dto ->
            val configValueAsString = when (dto.configValue) {
                is List<*> -> (dto.configValue as List<String>).joinToString(",")
                is Int -> dto.configValue.toString()
                else -> ""
            }

            Field(
                id = dto._id,
                configName = dto.configName,
                configValue = configValueAsString,
                isActive = dto.isActive
            )
        } ?: emptyList()

        return try {
            fieldDao.insertConfigs(fieldEntities)
            Log.d("FieldRepositoryImpl", "fetchConfigs: Configs stored successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "fetchConfigs: Error storing configs - ${e.message}")
            return Resource.Error("Error storing configs: ${e.message}")
        }
    }


    override suspend fun getAllConfig(): Resource<List<Field?>> {
        Log.d("FieldRepositoryImpl", "getAllConfig: Fetching all configs started")
        val configs = try {
            fieldDao.getAllConfigs()
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "getAllConfig: Error fetching all configs - ${e.message}")
            return Resource.Error("Error fetching all configs: ${e.message}")
        }

        Log.d("FieldRepositoryImpl", "getAllConfig: Fetching all configs successful")
        return Resource.Success(configs)
    }

    override suspend fun getConfigByName(config: String): Resource<Field?> {
        Log.d("FieldRepositoryImpl", "getConfigByName: Fetching config by name started")
        val configByName = try {
            fieldDao.getConfigsByName(config)
        } catch (e: Exception) {
            Log.e(
                "FieldRepositoryImpl",
                "getConfigByName: Error fetching config by name - ${e.message}"
            )
            return Resource.Error("Error fetching config by name: ${e.message}")
        }

        Log.d("FieldRepositoryImpl", "getConfigByName: Fetching config by name successful")
        return Resource.Success(configByName)
    }
}
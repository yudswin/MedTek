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

    override suspend fun getAllFieldName(): Resource<List<String?>> {
        Log.d("FieldRepositoryImpl", "getAllFieldName: Fetching all config names started")
        val response = try {
            fieldDao.getAllConfigNames()
        } catch (e: Exception) {
            Log.e(
                "FieldRepositoryImpl",
                "getAllFieldName: Error fetching config names - ${e.message}"
            )
            return Resource.Error("Error fetching config names: ${e.message}")
        }

        Log.d("FieldRepositoryImpl", "getAllFieldName: Fetching all config names successful")
        return Resource.Success(response)
    }

    override suspend fun areAllFieldsFetched(): Resource<Boolean> {
        Log.d("FieldRepositoryImpl", "areAllFieldsFetched: Checking if all fields are fetched")
        val response = try {
            fieldDao.getConfigCount()
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "areAllFieldsFetched: Error - ${e.message}")
            return Resource.Error("Error validating fields: ${e.message}")
        }

        Log.d(
            "FieldRepositoryImpl",
            "areAllFieldsFetched: All fields fetched status - ${response > 0}"
        )
        return if (response > 0) Resource.Success(true)
        else Resource.Success(false)
    }

    override suspend fun fetchFields(): Resource<Unit> {
        Log.d("FieldRepositoryImpl", "fetchFields: Fetching fields started")
        val response = try {
            fieldService.getAllField()
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "fetchFields: Error during API call - ${e.message}")
            return Resource.Error("Error during API call: ${e.message}")
        }

        Log.d("FieldRepositoryImpl", "fetchFields: response: ${response.body()}")

        if (!response.isSuccessful) {
            Log.e(
                "FieldRepositoryImpl",
                "fetchFields: Failed to fetch configs. HTTP code: ${response.code()}"
            )
            return Resource.Error("Failed to fetch configs. HTTP code: ${response.code()}")
        }

        return try {
            val responseBody = response.body()
            if (responseBody == null) {
                Log.e("FieldRepositoryImpl", "fetchFields: Response body is null")
                return Resource.Error("Response body is null")
            }

            val fieldEntities = responseBody.map { dto ->
                Field(
                    configName = dto.configName,
                    configValues = dto.configValue,
                    isActive = true
                )
            }

            fieldDao.insertConfigs(fieldEntities)
            Log.d("FieldRepositoryImpl", "fetchFields: Configs stored successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("FieldRepositoryImpl", "fetchFields: Error storing configs - ${e.message}")
            Resource.Error("Error storing configs: ${e.message}")
        } finally {
            Log.d("FieldRepositoryImpl", "fetchFields: Fetching fields finished")
        }
    }
}
package com.medtek.main.data.repository.survey

import com.medtek.main.data.local.entities.Field
import com.medtek.main.utilties.Resource

interface FieldRepository {

    suspend fun fetchConfigs(): Resource<Unit>

    suspend fun getAllConfig(): Resource<List<Field?>>

    suspend fun getConfigByName(config: String): Resource<Field?>
}
package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.data.mapper.toDomain
import com.msme.plus.shared.data.mock.FinancialHealthMockData
import com.msme.plus.shared.data.model.health.FinancialHealthDto
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import com.msme.plus.shared.domain.repository.FinancialHealthRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class FinancialHealthRepositoryImpl : FinancialHealthRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getFinancialHealth(): Resource<FinancialHealthData> {
        return safeApiCall {
            delay(1000)
            val dto = json.decodeFromString(
                FinancialHealthDto.serializer(),
                FinancialHealthMockData.FINANCIAL_HEALTH_JSON
            )
            dto.toDomain()
        }
    }
}

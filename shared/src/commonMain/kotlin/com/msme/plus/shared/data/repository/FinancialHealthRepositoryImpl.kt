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

import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.data.model.health.FinancialHealthResponseDto
import com.msme.plus.shared.domain.models.MsmeProfile

class FinancialHealthRepositoryImpl(
    private val apiService: ApiService,
    private val settingsManager: SettingsManager
) : FinancialHealthRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getFinancialHealth(): Resource<FinancialHealthData> {
        return safeApiCall {
            // --- OLD MOCK CALL (kept commented out as requested) ---
            /*
            delay(1000)
            val responseDto = json.decodeFromString(
                FinancialHealthResponseDto.serializer(),
                FinancialHealthMockData.FINANCIAL_HEALTH_JSON
            )
            responseDto.data.toDomain()
            */
            
            // --- REAL API CALL ---
            val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("User profile not found")
            val msme = json.decodeFromString(MsmeProfile.serializer(), msmeJson)
            val response = apiService.getFinancialHealth(msme.id)
            
            if (response.statusCode != 200) {
                throw Exception(response.message)
            }
            
            response.data.toDomain()
        }
    }
}

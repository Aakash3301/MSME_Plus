package com.msme.plus.shared.data.repository.analytics

import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsMockData
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.domain.repository.analytics.RevenueAnalyticsRepository
import kotlinx.coroutines.delay

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.mapper.toDomain
import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsResponseDto
import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.domain.models.MsmeProfile
import kotlinx.serialization.json.Json

class RevenueAnalyticsRepositoryImpl(
    private val apiService: ApiService,
    private val settingsManager: SettingsManager
) : RevenueAnalyticsRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getRevenueAnalytics(): Resource<RevenueAnalyticsData> {
        return safeApiCall {
            // --- OLD MOCK CALL (kept commented out as requested) ---
            /*
            delay(1500)
            val responseDto = json.decodeFromString(
                RevenueAnalyticsResponseDto.serializer(),
                RevenueAnalyticsMockData.REVENUE_ANALYTICS_JSON
            )
            responseDto.data.toDomain()
            */
            
            // --- REAL API CALL ---
            val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("User profile not found")
            val msme = json.decodeFromString(MsmeProfile.serializer(), msmeJson)
            val response = apiService.getRevenueAnalytics(msme.id)
            
            if (response.statusCode != 200) {
                throw Exception(response.message)
            }
            val data = response.data ?: com.msme.plus.shared.data.model.analytics.RevenueAnalyticsDto()
            data.toDomain()
        }
    }
}

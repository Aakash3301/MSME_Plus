package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.data.mapper.toDomain
import com.msme.plus.shared.data.mock.DashboardMockData
import com.msme.plus.shared.data.model.dashboard.DashboardDto
import com.msme.plus.shared.domain.model.dashboard.DashboardData
import com.msme.plus.shared.domain.repository.DashboardRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.data.model.dashboard.DashboardResponseDto
import com.msme.plus.shared.domain.models.MsmeProfile

class DashboardRepositoryImpl(
    private val apiService: ApiService,
    private val settingsManager: SettingsManager
) : DashboardRepository {
    private val jsonConfig = Json { ignoreUnknownKeys = true }

    override suspend fun getDashboardData(): Resource<DashboardData> {
        return safeApiCall {
            // --- OLD MOCK CALL (kept commented out as requested) ---
            /*
            delay(1500)
            val responseDto = jsonConfig.decodeFromString<DashboardResponseDto>(DashboardMockData.DASHBOARD_JSON)
            responseDto.data.toDomain()
            */
            
            // --- REAL API CALL ---
            val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("User profile not found")
            val msme = jsonConfig.decodeFromString<MsmeProfile>(msmeJson)
            val response = apiService.getDashboard(msme.id)
            
            if (response.statusCode != 200) {
                throw Exception(response.message)
            }
            
            response.data.toDomain()
        }
    }
}

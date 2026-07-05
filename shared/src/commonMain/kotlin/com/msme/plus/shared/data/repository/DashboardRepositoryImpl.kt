package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.data.mapper.toDomain
import com.msme.plus.shared.data.mock.DashboardMockData
import com.msme.plus.shared.data.model.dashboard.DashboardDto
import com.msme.plus.shared.domain.model.dashboard.DashboardData
import com.msme.plus.shared.domain.repository.DashboardRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class DashboardRepositoryImpl : DashboardRepository {
    private val jsonConfig = Json { ignoreUnknownKeys = true }

    override suspend fun getDashboardData(): Resource<DashboardData> {
        return try {
            // Simulate network delay
            delay(1500)
            
            // Parse Mock JSON
            val dto = jsonConfig.decodeFromString<DashboardDto>(DashboardMockData.DASHBOARD_JSON)
            
            // Map to Domain model
            Resource.Success(dto.toDomain())
        } catch (e: Exception) {
            Resource.Error(e, "Failed to load dashboard data")
        }
    }
}

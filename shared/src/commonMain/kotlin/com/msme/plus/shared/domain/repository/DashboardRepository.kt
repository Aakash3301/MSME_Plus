package com.msme.plus.shared.domain.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.dashboard.DashboardData

interface DashboardRepository {
    suspend fun getDashboardData(): Resource<DashboardData>
}

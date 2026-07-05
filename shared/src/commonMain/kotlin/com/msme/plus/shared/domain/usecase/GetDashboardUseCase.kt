package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.dashboard.DashboardData
import com.msme.plus.shared.domain.repository.DashboardRepository

class GetDashboardUseCase(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Resource<DashboardData> {
        return repository.getDashboardData()
    }
}

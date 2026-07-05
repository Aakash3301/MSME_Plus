package com.msme.plus.shared.domain.usecase.analytics

import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.domain.repository.analytics.RevenueAnalyticsRepository

class GetRevenueAnalyticsUseCase(
    private val repository: RevenueAnalyticsRepository
) {
    suspend operator fun invoke(): Result<RevenueAnalyticsData> {
        return repository.getRevenueAnalytics()
    }
}

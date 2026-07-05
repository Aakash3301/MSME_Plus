package com.msme.plus.shared.domain.repository.analytics

import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData

interface RevenueAnalyticsRepository {
    suspend fun getRevenueAnalytics(): Result<RevenueAnalyticsData>
}

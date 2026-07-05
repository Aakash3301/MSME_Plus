package com.msme.plus.shared.data.repository.analytics

import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsMockData
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.domain.repository.analytics.RevenueAnalyticsRepository
import kotlinx.coroutines.delay

class RevenueAnalyticsRepositoryImpl : RevenueAnalyticsRepository {
    override suspend fun getRevenueAnalytics(): Result<RevenueAnalyticsData> {
        // Simulate network delay to demonstrate the shimmer effect as requested
        delay(1500)
        return Result.success(RevenueAnalyticsMockData.analyticsData)
    }
}

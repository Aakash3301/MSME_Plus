package com.msme.plus.shared.domain.repository.analytics

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData

interface RevenueAnalyticsRepository {
    suspend fun getRevenueAnalytics(): Resource<RevenueAnalyticsData>
}

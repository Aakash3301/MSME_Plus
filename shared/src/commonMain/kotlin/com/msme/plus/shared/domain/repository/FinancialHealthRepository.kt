package com.msme.plus.shared.domain.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import kotlinx.coroutines.flow.Flow

interface FinancialHealthRepository {
    suspend fun getFinancialHealth(): Resource<FinancialHealthData>
}

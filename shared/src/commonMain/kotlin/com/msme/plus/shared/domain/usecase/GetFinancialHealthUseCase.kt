package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import com.msme.plus.shared.domain.repository.FinancialHealthRepository

class GetFinancialHealthUseCase(
    private val repository: FinancialHealthRepository
) {
    suspend operator fun invoke(): Resource<FinancialHealthData> {
        return repository.getFinancialHealth()
    }
}

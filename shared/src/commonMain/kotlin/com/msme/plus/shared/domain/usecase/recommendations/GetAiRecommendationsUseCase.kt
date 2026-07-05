package com.msme.plus.shared.domain.usecase.recommendations

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.recommendations.AiRecommendation
import com.msme.plus.shared.domain.repository.recommendations.AiRecommendationsRepository
import kotlinx.coroutines.flow.Flow

class GetAiRecommendationsUseCase(
    private val repository: AiRecommendationsRepository
) {
    operator fun invoke(): Flow<Resource<List<AiRecommendation>>> = repository.getRecommendations()
}

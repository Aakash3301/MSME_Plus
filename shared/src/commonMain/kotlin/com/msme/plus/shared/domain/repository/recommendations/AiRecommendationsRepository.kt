package com.msme.plus.shared.domain.repository.recommendations

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.recommendations.AiRecommendation
import kotlinx.coroutines.flow.Flow

interface AiRecommendationsRepository {
    fun getRecommendations(): Flow<Resource<List<AiRecommendation>>>
}

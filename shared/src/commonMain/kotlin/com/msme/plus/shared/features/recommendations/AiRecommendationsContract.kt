package com.msme.plus.shared.features.recommendations

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.recommendations.AiRecommendation

data class AiRecommendationsState(
    val isLoading: Boolean = false,
    val recommendations: List<AiRecommendation> = emptyList(),
    val potentialGrowth: Int = 42,
    val error: String? = null
) : UiState

sealed interface AiRecommendationsIntent : UiIntent {
    data object LoadRecommendations : AiRecommendationsIntent
    data object NavigateBack : AiRecommendationsIntent
    data object DownloadPlan : AiRecommendationsIntent
}

sealed interface AiRecommendationsAction : UiAction {
    data object LoadRecommendations : AiRecommendationsAction
    data object NavigateBack : AiRecommendationsAction
    data object DownloadPlan : AiRecommendationsAction
}

sealed interface AiRecommendationsResult : UiResult {
    data object Loading : AiRecommendationsResult
    data class Success(val recommendations: List<AiRecommendation>) : AiRecommendationsResult
    data class Error(val message: String) : AiRecommendationsResult
}

sealed interface AiRecommendationsEffect : UiEffect {
    data object NavigateBack : AiRecommendationsEffect
    data class ShowToast(val message: String) : AiRecommendationsEffect
}

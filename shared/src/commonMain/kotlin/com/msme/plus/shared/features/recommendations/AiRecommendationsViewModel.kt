package com.msme.plus.shared.features.recommendations

import androidx.lifecycle.viewModelScope
import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.recommendations.GetAiRecommendationsUseCase
import kotlinx.coroutines.launch

class AiRecommendationsViewModel(
    private val getAiRecommendationsUseCase: GetAiRecommendationsUseCase
) : Store<AiRecommendationsState, AiRecommendationsIntent, AiRecommendationsAction, AiRecommendationsResult, AiRecommendationsEffect>(
    initialState = AiRecommendationsState()
) {

    override fun intentToAction(intent: AiRecommendationsIntent): AiRecommendationsAction {
        return when (intent) {
            is AiRecommendationsIntent.LoadRecommendations -> AiRecommendationsAction.LoadRecommendations
            is AiRecommendationsIntent.NavigateBack -> AiRecommendationsAction.NavigateBack
            is AiRecommendationsIntent.DownloadPlan -> AiRecommendationsAction.DownloadPlan
        }
    }

    override suspend fun executeAction(action: AiRecommendationsAction) {
        when (action) {
            is AiRecommendationsAction.LoadRecommendations -> {
                viewModelScope.launch {
                    getAiRecommendationsUseCase().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> dispatch(AiRecommendationsResult.Loading)
                            is Resource.Success -> dispatch(AiRecommendationsResult.Success(resource.data))
                            is Resource.Error -> dispatch(AiRecommendationsResult.Error(resource.message ?: resource.exception.message ?: "Failed to load recommendations"))
                        }
                    }
                }
            }
            is AiRecommendationsAction.NavigateBack -> {
                viewModelScope.launch {
                    emitEffect(AiRecommendationsEffect.NavigateBack)
                }
            }
            is AiRecommendationsAction.DownloadPlan -> {
                viewModelScope.launch {
                    emitEffect(AiRecommendationsEffect.ShowToast("Downloading detailed plan..."))
                }
            }
        }
    }

    override fun reduce(state: AiRecommendationsState, result: AiRecommendationsResult): AiRecommendationsState {
        return aiRecommendationsReducer(state, result)
    }
}

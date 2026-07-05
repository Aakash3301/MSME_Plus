package com.msme.plus.shared.features.recommendations

fun aiRecommendationsReducer(state: AiRecommendationsState, result: AiRecommendationsResult): AiRecommendationsState {
    return when (result) {
        is AiRecommendationsResult.Loading -> state.copy(
            isLoading = true,
            error = null
        )
        is AiRecommendationsResult.Success -> state.copy(
            isLoading = false,
            recommendations = result.recommendations,
            error = null
        )
        is AiRecommendationsResult.Error -> state.copy(
            isLoading = false,
            error = result.message
        )
    }
}

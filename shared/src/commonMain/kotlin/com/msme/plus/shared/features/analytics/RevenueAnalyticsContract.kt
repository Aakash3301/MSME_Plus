package com.msme.plus.shared.features.analytics

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData

data class RevenueAnalyticsState(
    val isLoading: Boolean = false,
    val data: RevenueAnalyticsData? = null,
    val error: String? = null
) : UiState

sealed interface RevenueAnalyticsIntent : UiIntent {
    data object LoadData : RevenueAnalyticsIntent
    data object NavigateBack : RevenueAnalyticsIntent
    data object GenerateReport : RevenueAnalyticsIntent
}

sealed interface RevenueAnalyticsAction : UiAction {
    data object LoadData : RevenueAnalyticsAction
    data object NavigateBack : RevenueAnalyticsAction
    data object GenerateReport : RevenueAnalyticsAction
}

sealed interface RevenueAnalyticsResult : UiResult {
    data object Loading : RevenueAnalyticsResult
    data class Success(val data: RevenueAnalyticsData) : RevenueAnalyticsResult
    data class Error(val message: String) : RevenueAnalyticsResult
}

sealed interface RevenueAnalyticsEffect : UiEffect {
    data object NavigateBack : RevenueAnalyticsEffect
    data class ShowToast(val message: String) : RevenueAnalyticsEffect
}

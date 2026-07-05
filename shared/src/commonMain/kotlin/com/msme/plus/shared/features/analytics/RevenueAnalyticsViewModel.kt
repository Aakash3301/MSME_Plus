package com.msme.plus.shared.features.analytics

import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.analytics.GetRevenueAnalyticsUseCase

class RevenueAnalyticsViewModel(
    private val getRevenueAnalyticsUseCase: GetRevenueAnalyticsUseCase
) : Store<RevenueAnalyticsState, RevenueAnalyticsIntent, RevenueAnalyticsAction, RevenueAnalyticsResult, RevenueAnalyticsEffect>(
    initialState = RevenueAnalyticsState()
) {
    override fun intentToAction(intent: RevenueAnalyticsIntent): RevenueAnalyticsAction {
        return when (intent) {
            RevenueAnalyticsIntent.LoadData -> RevenueAnalyticsAction.LoadData
            RevenueAnalyticsIntent.NavigateBack -> RevenueAnalyticsAction.NavigateBack
            RevenueAnalyticsIntent.GenerateReport -> RevenueAnalyticsAction.GenerateReport
        }
    }

    override suspend fun executeAction(action: RevenueAnalyticsAction) {
        when (action) {
            RevenueAnalyticsAction.LoadData -> {
                dispatch(RevenueAnalyticsResult.Loading)
                getRevenueAnalyticsUseCase().fold(
                    onSuccess = { dispatch(RevenueAnalyticsResult.Success(it)) },
                    onFailure = { dispatch(RevenueAnalyticsResult.Error(it.message ?: "Unknown Error")) }
                )
            }
            RevenueAnalyticsAction.NavigateBack -> {
                emitEffect(RevenueAnalyticsEffect.NavigateBack)
            }
            RevenueAnalyticsAction.GenerateReport -> {
                emitEffect(RevenueAnalyticsEffect.ShowToast("Generating Report..."))
            }
        }
    }

    override fun reduce(state: RevenueAnalyticsState, result: RevenueAnalyticsResult): RevenueAnalyticsState {
        return when (result) {
            is RevenueAnalyticsResult.Error -> state.copy(isLoading = false, error = result.message)
            RevenueAnalyticsResult.Loading -> state.copy(isLoading = true, error = null)
            is RevenueAnalyticsResult.Success -> state.copy(isLoading = false, data = result.data, error = null)
        }
    }
}

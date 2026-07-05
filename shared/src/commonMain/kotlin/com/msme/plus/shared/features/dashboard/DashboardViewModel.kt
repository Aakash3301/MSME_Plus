package com.msme.plus.shared.features.dashboard

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.GetDashboardUseCase

import com.msme.plus.shared.domain.usecase.LogoutUseCase

class DashboardViewModel(
    private val getDashboardUseCase: GetDashboardUseCase,
    private val logoutUseCase: LogoutUseCase
) : Store<DashboardState, DashboardIntent, DashboardAction, DashboardResult, DashboardEffect>(DashboardState()) {

    override fun intentToAction(intent: DashboardIntent): DashboardAction {
        return when (intent) {
            is DashboardIntent.LoadData -> DashboardAction.LoadDashboard
            is DashboardIntent.QuickActionClicked -> {
                when (intent.actionId) {
                    "health_card" -> DashboardAction.NavigateToFinancialHealth
                    "loan_assessment" -> DashboardAction.NavigateToLoanAssessment
                    "revenue_analytics" -> DashboardAction.NavigateToAnalytics
                    "alternate_data" -> DashboardAction.NavigateToAlternateData
                    "ai_advisor" -> DashboardAction.NavigateToAiAdvisor
                    else -> DashboardAction.ShowQuickActionToast(intent.actionId)
                }
            }
            is DashboardIntent.NavigateToFinancialHealth -> DashboardAction.NavigateToFinancialHealth
            is DashboardIntent.NavigateToLoanAssessment -> DashboardAction.NavigateToLoanAssessment
            is DashboardIntent.NavigateToAnalytics -> DashboardAction.NavigateToAnalytics
            is DashboardIntent.NavigateToAlternateData -> DashboardAction.NavigateToAlternateData
            is DashboardIntent.NavigateToAiAdvisor -> DashboardAction.NavigateToAiAdvisor
            is DashboardIntent.ApplyLoanClicked -> DashboardAction.ApplyLoan
            is DashboardIntent.LogoutClicked -> DashboardAction.Logout
        }
    }

    override suspend fun executeAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.LoadDashboard -> {
                dispatch(DashboardResult.Loading)
                when (val result = getDashboardUseCase()) {
                    is Resource.Success -> {
                        dispatch(DashboardResult.Success(result.data))
                    }
                    is Resource.Error -> {
                        dispatch(DashboardResult.Error(result.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        // Not used by this usecase
                    }
                }
            }
            is DashboardAction.ShowQuickActionToast -> {
                emitEffect(DashboardEffect.ShowToast("Clicked: ${action.actionId}"))
            }
            is DashboardAction.NavigateToFinancialHealth -> {
                emitEffect(DashboardEffect.NavigateToFinancialHealth)
            }
            is DashboardAction.NavigateToLoanAssessment -> {
                emitEffect(DashboardEffect.NavigateToLoanAssessment)
            }
            is DashboardAction.NavigateToAnalytics -> {
                emitEffect(DashboardEffect.NavigateToAnalytics)
            }
            is DashboardAction.NavigateToAlternateData -> {
                emitEffect(DashboardEffect.NavigateToAlternateData)
            }
            is DashboardAction.NavigateToAiAdvisor -> {
                emitEffect(DashboardEffect.NavigateToAiAdvisor)
            }
            is DashboardAction.ApplyLoan -> {
                emitEffect(DashboardEffect.ShowToast("Apply Loan Clicked"))
            }
            is DashboardAction.Logout -> {
                logoutUseCase()
                emitEffect(DashboardEffect.NavigateToLogin)
            }
        }
    }

    override fun reduce(state: DashboardState, result: DashboardResult): DashboardState {
        return dashboardReducer(state, result)
    }
}

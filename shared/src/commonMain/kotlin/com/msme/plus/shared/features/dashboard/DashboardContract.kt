package com.msme.plus.shared.features.dashboard

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.dashboard.DashboardData

sealed interface DashboardAction : UiAction {
    data object LoadDashboard : DashboardAction
    data class ShowQuickActionToast(val actionId: String) : DashboardAction
    data object NavigateToFinancialHealth : DashboardAction
    data object NavigateToLoanAssessment : DashboardAction
    data object ApplyLoan : DashboardAction
    data object NavigateToAnalytics : DashboardAction
    data object NavigateToAlternateData : DashboardAction
    data object NavigateToAiAdvisor : DashboardAction
    data object Logout : DashboardAction
}

sealed interface DashboardEffect : UiEffect {
    data class ShowToast(val message: String) : DashboardEffect
    data object NavigateToLogin : DashboardEffect
    data object NavigateToFinancialHealth : DashboardEffect
    data object NavigateToLoanAssessment : DashboardEffect
    data object NavigateToAnalytics : DashboardEffect
    data object NavigateToAlternateData : DashboardEffect
    data object NavigateToAiAdvisor : DashboardEffect
}

sealed interface DashboardResult : UiResult {
    data object Loading : DashboardResult
    data class Success(val data: DashboardData) : DashboardResult
    data class Error(val message: String) : DashboardResult
}

sealed interface DashboardIntent : UiIntent {
    data object LoadData : DashboardIntent
    data class QuickActionClicked(val actionId: String) : DashboardIntent
    data object ApplyLoanClicked : DashboardIntent
    data object LogoutClicked : DashboardIntent
    data object NavigateToFinancialHealth : DashboardIntent
    data object NavigateToLoanAssessment : DashboardIntent
    data object NavigateToAnalytics : DashboardIntent
    data object NavigateToAlternateData : DashboardIntent
    data object NavigateToAiAdvisor : DashboardIntent
}

data class DashboardState(
    val isLoading: Boolean = false,
    val data: DashboardData? = null,
    val error: String? = null
) : UiState

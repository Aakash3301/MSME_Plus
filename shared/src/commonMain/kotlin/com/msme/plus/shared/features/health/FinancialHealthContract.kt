package com.msme.plus.shared.features.health

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.health.FinancialHealthData

sealed interface FinancialHealthIntent : UiIntent {
    data object LoadData : FinancialHealthIntent
    data object ViewMitigationPlanClicked : FinancialHealthIntent
    data object ApplyLoanClicked : FinancialHealthIntent
    data object BackClicked : FinancialHealthIntent
}

sealed interface FinancialHealthAction : UiAction {
    data object LoadHealthData : FinancialHealthAction
    data object NavigateBack : FinancialHealthAction
    data object ViewMitigationPlan : FinancialHealthAction
    data object ApplyLoan : FinancialHealthAction
}

sealed interface FinancialHealthResult : UiResult {
    data object Loading : FinancialHealthResult
    data class Success(val data: FinancialHealthData) : FinancialHealthResult
    data class Error(val message: String) : FinancialHealthResult
}

sealed interface FinancialHealthEffect : UiEffect {
    data class ShowToast(val message: String) : FinancialHealthEffect
    data object NavigateBack : FinancialHealthEffect
}

data class FinancialHealthState(
    val isLoading: Boolean = false,
    val data: FinancialHealthData? = null,
    val error: String? = null
) : UiState

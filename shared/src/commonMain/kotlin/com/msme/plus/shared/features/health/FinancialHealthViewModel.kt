package com.msme.plus.shared.features.health

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.GetFinancialHealthUseCase

class FinancialHealthViewModel(
    private val getFinancialHealthUseCase: GetFinancialHealthUseCase
) : Store<FinancialHealthState, FinancialHealthIntent, FinancialHealthAction, FinancialHealthResult, FinancialHealthEffect>(FinancialHealthState()) {

    override fun intentToAction(intent: FinancialHealthIntent): FinancialHealthAction {
        return when (intent) {
            is FinancialHealthIntent.LoadData -> FinancialHealthAction.LoadHealthData
            is FinancialHealthIntent.BackClicked -> FinancialHealthAction.NavigateBack
            is FinancialHealthIntent.ApplyLoanClicked -> FinancialHealthAction.ApplyLoan
            is FinancialHealthIntent.ViewMitigationPlanClicked -> FinancialHealthAction.ViewMitigationPlan
        }
    }

    override suspend fun executeAction(action: FinancialHealthAction) {
        when (action) {
            is FinancialHealthAction.LoadHealthData -> {
                dispatch(FinancialHealthResult.Loading)
                when (val result = getFinancialHealthUseCase()) {
                    is Resource.Success -> {
                        dispatch(FinancialHealthResult.Success(result.data))
                    }
                    is Resource.Error -> {
                        dispatch(FinancialHealthResult.Error(result.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {}
                }
            }
            is FinancialHealthAction.NavigateBack -> {
                emitEffect(FinancialHealthEffect.NavigateBack)
            }
            is FinancialHealthAction.ViewMitigationPlan -> {
                emitEffect(FinancialHealthEffect.ShowToast("Mitigation Plan Clicked"))
            }
            is FinancialHealthAction.ApplyLoan -> {
                emitEffect(FinancialHealthEffect.ShowToast("Apply Loan Clicked"))
            }
        }
    }

    override fun reduce(state: FinancialHealthState, result: FinancialHealthResult): FinancialHealthState {
        return when (result) {
            is FinancialHealthResult.Loading -> state.copy(isLoading = true, error = null)
            is FinancialHealthResult.Success -> state.copy(isLoading = false, data = result.data, error = null)
            is FinancialHealthResult.Error -> state.copy(isLoading = false, error = result.message)
        }
    }
}

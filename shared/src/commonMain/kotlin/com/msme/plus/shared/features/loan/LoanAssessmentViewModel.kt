package com.msme.plus.shared.features.loan

import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.loan.GetLoanAssessmentUseCase

class LoanAssessmentViewModel(
    private val getLoanAssessmentUseCase: GetLoanAssessmentUseCase
) : Store<LoanAssessmentState, LoanAssessmentIntent, LoanAssessmentAction, LoanAssessmentResult, LoanAssessmentEffect>(
    initialState = LoanAssessmentState()
) {
    override fun intentToAction(intent: LoanAssessmentIntent): LoanAssessmentAction {
        return when (intent) {
            is LoanAssessmentIntent.UpdateRequestedLoan -> LoanAssessmentAction.UpdateRequestedLoan(intent.amount)
            is LoanAssessmentIntent.UpdateLoanPurpose -> LoanAssessmentAction.UpdateLoanPurpose(intent.purpose)
            is LoanAssessmentIntent.UpdateBusinessAge -> LoanAssessmentAction.UpdateBusinessAge(intent.age)
            is LoanAssessmentIntent.UpdateWcRequirement -> LoanAssessmentAction.UpdateWcRequirement(intent.amount)
            is LoanAssessmentIntent.CheckEligibilityClicked -> LoanAssessmentAction.PerformEligibilityCheck(
                requestedLoan = currentState.data.requestedLoan,
                loanPurpose = currentState.data.loanPurpose,
                businessAge = currentState.data.businessAge,
                wcRequirement = currentState.data.wcRequirement
            )
            is LoanAssessmentIntent.BackClicked -> LoanAssessmentAction.NavigateBack
            is LoanAssessmentIntent.AskAdvisorClicked -> LoanAssessmentAction.NavigateToAiAdvisor
        }
    }

    override suspend fun executeAction(action: LoanAssessmentAction) {
        when (action) {
            is LoanAssessmentAction.UpdateRequestedLoan -> {
                dispatch(LoanAssessmentResult.InputUpdated(currentState.data.copy(requestedLoan = action.amount)))
            }
            is LoanAssessmentAction.UpdateLoanPurpose -> {
                dispatch(LoanAssessmentResult.InputUpdated(currentState.data.copy(loanPurpose = action.purpose)))
            }
            is LoanAssessmentAction.UpdateBusinessAge -> {
                dispatch(LoanAssessmentResult.InputUpdated(currentState.data.copy(businessAge = action.age)))
            }
            is LoanAssessmentAction.UpdateWcRequirement -> {
                dispatch(LoanAssessmentResult.InputUpdated(currentState.data.copy(wcRequirement = action.amount)))
            }
            is LoanAssessmentAction.PerformEligibilityCheck -> {
                dispatch(LoanAssessmentResult.AssessmentLoading)
                getLoanAssessmentUseCase(
                    requestedLoan = action.requestedLoan,
                    loanPurpose = action.loanPurpose,
                    businessAge = action.businessAge,
                    wcRequirement = action.wcRequirement
                ).fold(
                    onSuccess = { result ->
                        dispatch(LoanAssessmentResult.AssessmentSuccess(result))
                    },
                    onFailure = { error ->
                        dispatch(LoanAssessmentResult.AssessmentError(error.message ?: "An error occurred"))
                    }
                )
            }
            is LoanAssessmentAction.NavigateBack -> {
                emitEffect(LoanAssessmentEffect.NavigateBack)
            }
            is LoanAssessmentAction.NavigateToAiAdvisor -> {
                emitEffect(LoanAssessmentEffect.NavigateToAiAdvisor)
            }
        }
    }

    override fun reduce(state: LoanAssessmentState, result: LoanAssessmentResult): LoanAssessmentState {
        return when (result) {
            is LoanAssessmentResult.InputUpdated -> {
                state.copy(data = result.data)
            }
            is LoanAssessmentResult.AssessmentLoading -> {
                state.copy(isLoading = true, error = null)
            }
            is LoanAssessmentResult.AssessmentSuccess -> {
                state.copy(
                    isLoading = false,
                    data = state.data.copy(assessmentResult = result.result),
                    error = null
                )
            }
            is LoanAssessmentResult.AssessmentError -> {
                state.copy(isLoading = false, error = result.message)
            }
        }
    }
}

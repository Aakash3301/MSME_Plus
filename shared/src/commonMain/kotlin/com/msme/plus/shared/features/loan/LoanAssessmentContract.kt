package com.msme.plus.shared.features.loan

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.model.loan.LoanAssessmentData

data class LoanAssessmentState(
    val isLoading: Boolean = false,
    val data: LoanAssessmentData = LoanAssessmentData(),
    val error: String? = null
) : UiState

sealed interface LoanAssessmentIntent : UiIntent {
    data class UpdateRequestedLoan(val amount: String) : LoanAssessmentIntent
    data class UpdateLoanPurpose(val purpose: String) : LoanAssessmentIntent
    data class UpdateBusinessAge(val age: String) : LoanAssessmentIntent
    data class UpdateWcRequirement(val amount: String) : LoanAssessmentIntent
    object CheckEligibilityClicked : LoanAssessmentIntent
    object BackClicked : LoanAssessmentIntent
    object AskAdvisorClicked : LoanAssessmentIntent
}

sealed interface LoanAssessmentAction : UiAction {
    data class UpdateRequestedLoan(val amount: String) : LoanAssessmentAction
    data class UpdateLoanPurpose(val purpose: String) : LoanAssessmentAction
    data class UpdateBusinessAge(val age: String) : LoanAssessmentAction
    data class UpdateWcRequirement(val amount: String) : LoanAssessmentAction
    data class PerformEligibilityCheck(
        val requestedLoan: String,
        val loanPurpose: String,
        val businessAge: String,
        val wcRequirement: String
    ) : LoanAssessmentAction
    object NavigateBack : LoanAssessmentAction
    object NavigateToAiAdvisor : LoanAssessmentAction
}

sealed interface LoanAssessmentResult : UiResult {
    data class InputUpdated(val data: LoanAssessmentData) : LoanAssessmentResult
    object AssessmentLoading : LoanAssessmentResult
    data class AssessmentSuccess(val result: AssessmentResult) : LoanAssessmentResult
    data class AssessmentError(val message: String) : LoanAssessmentResult
}

sealed interface LoanAssessmentEffect : UiEffect {
    object NavigateBack : LoanAssessmentEffect
    object NavigateToAiAdvisor : LoanAssessmentEffect
}

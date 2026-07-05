package com.msme.plus.shared.domain.usecase.loan

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.repository.loan.LoanAssessmentRepository

class GetLoanAssessmentUseCase(
    private val repository: LoanAssessmentRepository
) {
    suspend operator fun invoke(
        requestedLoan: String,
        loanPurpose: String,
        businessAge: String,
        wcRequirement: String
    ): Resource<AssessmentResult> {
        return repository.getAssessment(requestedLoan, loanPurpose, businessAge, wcRequirement)
    }
}

package com.msme.plus.shared.domain.repository.loan

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.loan.AssessmentResult

interface LoanAssessmentRepository {
    suspend fun getAssessment(
        requestedLoan: String,
        loanPurpose: String,
        businessAge: String,
        wcRequirement: String
    ): Resource<AssessmentResult>
}

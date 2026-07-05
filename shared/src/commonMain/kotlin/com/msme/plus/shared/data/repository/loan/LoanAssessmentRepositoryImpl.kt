package com.msme.plus.shared.data.repository.loan

import com.msme.plus.shared.data.mapper.loan.toDomain
import com.msme.plus.shared.data.model.loan.LoanAssessmentMockData
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.repository.loan.LoanAssessmentRepository
import kotlinx.coroutines.delay

class LoanAssessmentRepositoryImpl : LoanAssessmentRepository {
    override suspend fun getAssessment(
        requestedLoan: String,
        loanPurpose: String,
        businessAge: String,
        wcRequirement: String
    ): Result<AssessmentResult> {
        return try {
            delay(1500) // Simulate network delay
            Result.success(LoanAssessmentMockData.assessmentResult.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

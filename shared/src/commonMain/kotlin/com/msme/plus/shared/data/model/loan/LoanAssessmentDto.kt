package com.msme.plus.shared.data.model.loan

import kotlinx.serialization.Serializable

@Serializable
data class LoanAssessmentRequestDto(
    val id: String,
    val requestedLoan: String,
    val loanPurpose: String,
    val businessAge: String,
    val wcRequirement: String
)

@Serializable
data class LoanAssessmentResponseDto(
    val statusCode: Int? = null,
    val message: String? = null,
    val data: AssessmentResultDto? = null
)

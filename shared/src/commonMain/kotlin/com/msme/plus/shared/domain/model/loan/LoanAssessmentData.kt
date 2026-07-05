package com.msme.plus.shared.domain.model.loan

data class LoanAssessmentData(
    val requestedLoan: String = "",
    val loanPurpose: String = "Working Capital",
    val businessAge: String = "<1 Year",
    val wcRequirement: String = "",
    val assessmentResult: AssessmentResult? = null
)

package com.msme.plus.shared.domain.model.loan

data class AssessmentResult(
    val isEligible: Boolean,
    val recommendedLoan: String,
    val riskLevel: String,
    val healthStatus: String,
    val confidencePercentage: Int,
    val aiInsightsText: String,
    val aiInsightsList: List<String>
)

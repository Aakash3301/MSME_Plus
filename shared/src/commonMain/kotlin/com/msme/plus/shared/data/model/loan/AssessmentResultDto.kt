package com.msme.plus.shared.data.model.loan

import kotlinx.serialization.Serializable

@Serializable
data class AssessmentResultDto(
    val isEligible: Boolean,
    val recommendedLoan: String,
    val riskLevel: String,
    val healthStatus: String,
    val confidencePercentage: Int,
    val aiInsightsText: String,
    val aiInsightsList: List<String>
)

package com.msme.plus.shared.data.model.loan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssessmentResultDto(
    @SerialName("eligible") val isEligible: Boolean = false,
    val recommendedLoan: String = "",
    val riskLevel: String = "",
    val healthStatus: String = "",
    val confidencePercentage: Int = 0,
    val aiInsightsText: String = "",
    val aiInsightsList: List<String> = emptyList()
)

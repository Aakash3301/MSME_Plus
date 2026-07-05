package com.msme.plus.shared.data.model.loan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssessmentResultDto(
    @SerialName("eligible") val isEligible: Boolean? = null,
    val recommendedLoan: String? = null,
    val riskLevel: String? = null,
    val healthStatus: String? = null,
    val confidencePercentage: Int? = null,
    val aiInsightsText: String? = null,
    val aiInsightsList: List<String>? = null
)

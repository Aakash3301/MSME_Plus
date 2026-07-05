package com.msme.plus.shared.data.mapper.loan

import com.msme.plus.shared.data.model.loan.AssessmentResultDto
import com.msme.plus.shared.domain.model.loan.AssessmentResult

fun AssessmentResultDto.toDomain() = AssessmentResult(
    isEligible = isEligible ?: false,
    recommendedLoan = recommendedLoan ?: "",
    riskLevel = riskLevel ?: "",
    healthStatus = healthStatus ?: "",
    confidencePercentage = confidencePercentage ?: 0,
    aiInsightsText = aiInsightsText ?: "",
    aiInsightsList = aiInsightsList ?: emptyList()
)

package com.msme.plus.shared.data.model.loan

object LoanAssessmentMockData {
    val assessmentResult = AssessmentResultDto(
        isEligible = true,
        recommendedLoan = "₹25 Lakh",
        riskLevel = "Low",
        healthStatus = "Healthy",
        confidencePercentage = 92,
        aiInsightsText = "Your business qualifies for the requested amount based on strong GST compliance (98%) and stable monthly revenue trends. High digital adoption (91%) further strengthens your credit profile.",
        aiInsightsList = listOf(
            "Consistent Cash Flow",
            "Excellent Compliance",
            "Positive Growth Trend"
        )
    )
}

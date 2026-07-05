package com.msme.plus.features.loan

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.model.loan.LoanAssessmentData

class LoanAssessmentPreviewProvider : PreviewParameterProvider<LoanAssessmentData> {
    override val values = sequenceOf(
        LoanAssessmentData(
            requestedLoan = "2500000",
            loanPurpose = "Working Capital",
            businessAge = "1-3 Years",
            wcRequirement = "500000",
            assessmentResult = AssessmentResult(
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
        )
    )
}

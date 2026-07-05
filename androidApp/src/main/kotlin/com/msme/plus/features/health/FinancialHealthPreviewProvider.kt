package com.msme.plus.features.health

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msme.plus.shared.domain.model.health.BadgeType
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import com.msme.plus.shared.domain.model.health.HealthBadge
import com.msme.plus.shared.domain.model.health.LoanOffer
import com.msme.plus.shared.domain.model.health.ScoreBreakdown

class FinancialHealthPreviewProvider : PreviewParameterProvider<FinancialHealthData> {
    override val values = sequenceOf(
        FinancialHealthData(
            companyName = "IDBI MSME",
            profileImageUrl = "",
            overallScore = 87,
            maxScore = 100,
            statusText = "Your business shows high fiscal discipline and robust digital adoption. You are in the top 10% of MSMEs in your sector.",
            badges = listOf(
                HealthBadge("Platinum Grade", "verified", BadgeType.PRIMARY),
                HealthBadge("Improving", "trending_up", BadgeType.TERTIARY)
            ),
            scoreBreakdowns = listOf(
                ScoreBreakdown("GST Compliance", 95, BadgeType.PRIMARY),
                ScoreBreakdown("Revenue Stability", 92, BadgeType.PRIMARY),
                ScoreBreakdown("Digital Transactions", 91, BadgeType.PRIMARY),
                ScoreBreakdown("Employee Stability", 90, BadgeType.PRIMARY),
                ScoreBreakdown("Cash Flow", 88, BadgeType.PRIMARY),
                ScoreBreakdown("Vendor Payment", 86, BadgeType.PRIMARY),
                ScoreBreakdown("Business Growth", 83, BadgeType.PRIMARY)
            ),
            strengths = listOf(
                "GST filed consistently",
                "Revenue growing",
                "Healthy UPI collections"
            ),
            risks = listOf(
                "Cash reserves slightly low",
                "Vendor payment cycle increasing"
            ),
            loanOffer = LoanOffer(
                title = "Unlock Business Loan Up to ₹50L",
                description = "Based on your excellent Health Score, you are pre-approved for immediate disbursement at competitive rates.",
                buttonText = "Apply Now"
            )
        )
    )
}

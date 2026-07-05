package com.msme.plus.shared.domain.model.health

data class FinancialHealthData(
    val companyName: String,
    val profileImageUrl: String,
    val overallScore: Int,
    val maxScore: Int,
    val statusText: String,
    val badges: List<HealthBadge>,
    val scoreBreakdowns: List<ScoreBreakdown>,
    val strengths: List<String>,
    val risks: List<String>,
    val loanOffer: LoanOffer?
)

data class HealthBadge(
    val text: String,
    val icon: String,
    val type: BadgeType
)

enum class BadgeType {
    PRIMARY, SECONDARY, TERTIARY
}

data class ScoreBreakdown(
    val label: String,
    val value: Int,
    val color: BadgeType
)

data class LoanOffer(
    val title: String,
    val description: String,
    val buttonText: String
)

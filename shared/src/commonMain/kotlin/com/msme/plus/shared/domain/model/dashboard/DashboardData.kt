package com.msme.plus.shared.domain.model.dashboard

data class DashboardData(
    val companyName: String,
    val sector: String,
    val hasNotifications: Boolean,
    val profileImageUrl: String,
    val healthScore: HealthScore,
    val kpis: List<Kpi>,
    val loanEligibility: LoanEligibility,
    val quickActions: List<QuickAction>
)

data class HealthScore(
    val score: Int,
    val maxScore: Int,
    val statusText: String,
    val tags: List<String>
)

data class Kpi(
    val id: String,
    val icon: String,
    val title: String,
    val value: String,
    val trend: String,
    val trendDirection: TrendDirection,
    val colorType: KpiColorType
)

enum class TrendDirection {
    UP, DOWN, NEUTRAL
}

enum class KpiColorType {
    PRIMARY, SECONDARY, TERTIARY
}

data class LoanEligibility(
    val status: String,
    val amountText: String,
    val confidencePercentage: Int
)

data class QuickAction(
    val id: String,
    val icon: String,
    val title: String
)

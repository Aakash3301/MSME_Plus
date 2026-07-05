package com.msme.plus.shared.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    val companyName: String = "",
    val sector: String = "",
    val hasNotifications: Boolean = false,
    val profileImageUrl: String = "",
    val healthScore: HealthScoreDto = HealthScoreDto(),
    val kpis: List<KpiDto> = emptyList(),
    val loanEligibility: LoanEligibilityDto = LoanEligibilityDto(),
    val quickActions: List<QuickActionDto> = emptyList()
)

@Serializable
data class HealthScoreDto(
    val score: Int = 0,
    val maxScore: Int = 100,
    val statusText: String = "",
    val tags: List<String> = emptyList()
)

@Serializable
data class KpiDto(
    val id: String = "",
    val icon: String = "",
    val title: String = "",
    val value: String = "",
    val trend: String = "",
    val trendDirection: String = "", // "up", "down", "neutral"
    val colorType: String = "" // "primary", "tertiary", "secondary"
)

@Serializable
data class LoanEligibilityDto(
    val status: String = "",
    val amountText: String = "",
    val confidencePercentage: Int = 0
)

@Serializable
data class QuickActionDto(
    val id: String = "",
    val icon: String = "",
    val title: String = ""
)

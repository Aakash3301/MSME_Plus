package com.msme.plus.shared.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    val companyName: String? = null,
    val sector: String? = null,
    val hasNotifications: Boolean? = null,
    val profileImageUrl: String? = null,
    val healthScore: HealthScoreDto? = null,
    val kpis: List<KpiDto>? = null,
    val loanEligibility: LoanEligibilityDto? = null,
    val quickActions: List<QuickActionDto>? = null
)

@Serializable
data class HealthScoreDto(
    val score: Int? = null,
    val maxScore: Int? = null,
    val statusText: String? = null,
    val tags: List<String>? = null
)

@Serializable
data class KpiDto(
    val id: String? = null,
    val icon: String? = null,
    val title: String? = null,
    val value: String? = null,
    val trend: String? = null,
    val trendDirection: String? = null, // "up", "down", "neutral"
    val colorType: String? = null // "primary", "tertiary", "secondary"
)

@Serializable
data class LoanEligibilityDto(
    val status: String? = null,
    val amountText: String? = null,
    val confidencePercentage: Int? = null
)

@Serializable
data class QuickActionDto(
    val id: String? = null,
    val icon: String? = null,
    val title: String? = null
)

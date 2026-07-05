package com.msme.plus.shared.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    val companyName: String,
    val sector: String,
    val hasNotifications: Boolean,
    val profileImageUrl: String,
    val healthScore: HealthScoreDto,
    val kpis: List<KpiDto>,
    val loanEligibility: LoanEligibilityDto,
    val quickActions: List<QuickActionDto>
)

@Serializable
data class HealthScoreDto(
    val score: Int,
    val maxScore: Int,
    val statusText: String,
    val tags: List<String>
)

@Serializable
data class KpiDto(
    val id: String,
    val icon: String,
    val title: String,
    val value: String,
    val trend: String,
    val trendDirection: String, // "up", "down", "neutral"
    val colorType: String // "primary", "tertiary", "secondary"
)

@Serializable
data class LoanEligibilityDto(
    val status: String,
    val amountText: String,
    val confidencePercentage: Int
)

@Serializable
data class QuickActionDto(
    val id: String,
    val icon: String,
    val title: String
)

package com.msme.plus.shared.data.model.health

import kotlinx.serialization.Serializable

@Serializable
data class FinancialHealthDto(
    val companyName: String? = null,
    val profileImageUrl: String? = null,
    val overallScore: Int? = null,
    val maxScore: Int? = null,
    val statusText: String? = null,
    val badges: List<HealthBadgeDto>? = null,
    val scoreBreakdowns: List<ScoreBreakdownDto>? = null,
    val strengths: List<String>? = null,
    val risks: List<String>? = null,
    val loanOffer: LoanOfferDto? = null
)

@Serializable
data class HealthBadgeDto(
    val text: String? = null,
    val icon: String? = null,
    val type: String? = null
)

@Serializable
data class ScoreBreakdownDto(
    val label: String? = null,
    val value: Int? = null,
    val color: String? = null
)

@Serializable
data class LoanOfferDto(
    val title: String? = null,
    val description: String? = null,
    val buttonText: String? = null
)

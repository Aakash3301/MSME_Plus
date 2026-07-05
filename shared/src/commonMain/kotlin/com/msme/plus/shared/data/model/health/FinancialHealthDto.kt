package com.msme.plus.shared.data.model.health

import kotlinx.serialization.Serializable

@Serializable
data class FinancialHealthDto(
    val companyName: String = "",
    val profileImageUrl: String = "",
    val overallScore: Int = 0,
    val maxScore: Int = 100,
    val statusText: String = "",
    val badges: List<HealthBadgeDto> = emptyList(),
    val scoreBreakdowns: List<ScoreBreakdownDto> = emptyList(),
    val strengths: List<String> = emptyList(),
    val risks: List<String> = emptyList(),
    val loanOffer: LoanOfferDto? = null
)

@Serializable
data class HealthBadgeDto(
    val text: String = "",
    val icon: String = "",
    val type: String = ""
)

@Serializable
data class ScoreBreakdownDto(
    val label: String = "",
    val value: Int = 0,
    val color: String = ""
)

@Serializable
data class LoanOfferDto(
    val title: String = "",
    val description: String = "",
    val buttonText: String = ""
)

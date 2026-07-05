package com.msme.plus.shared.data.model.health

import kotlinx.serialization.Serializable

@Serializable
data class FinancialHealthDto(
    val companyName: String,
    val profileImageUrl: String,
    val overallScore: Int,
    val maxScore: Int,
    val statusText: String,
    val badges: List<HealthBadgeDto>,
    val scoreBreakdowns: List<ScoreBreakdownDto>,
    val strengths: List<String>,
    val risks: List<String>,
    val loanOffer: LoanOfferDto? = null
)

@Serializable
data class HealthBadgeDto(
    val text: String,
    val icon: String,
    val type: String
)

@Serializable
data class ScoreBreakdownDto(
    val label: String,
    val value: Int,
    val color: String
)

@Serializable
data class LoanOfferDto(
    val title: String,
    val description: String,
    val buttonText: String
)

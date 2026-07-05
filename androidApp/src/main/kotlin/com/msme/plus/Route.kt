package com.msme.plus

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Splash : Route
    @Serializable data object Login : Route
    @Serializable data object Dashboard : Route
    @Serializable data object FinancialHealth : Route
    @Serializable data object LoanAssessment : Route
    @Serializable data object RevenueAnalytics : Route
    @Serializable data object AlternateData : Route
    @Serializable data object AiAdvisor : Route
    @Serializable data object AiRecommendations : Route
    @Serializable data object BusinessProfile : Route
}

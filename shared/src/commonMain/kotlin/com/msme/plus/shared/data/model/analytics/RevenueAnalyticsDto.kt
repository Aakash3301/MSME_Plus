package com.msme.plus.shared.data.model.analytics

import kotlinx.serialization.Serializable

@Serializable
data class RevenueAnalyticsResponseDto(
    val statusCode: Int,
    val message: String,
    val data: RevenueAnalyticsDto
)

@Serializable
data class RevenueAnalyticsDto(
    val aiInsights: List<String> = emptyList(),
    val totalRevenue: String = "",
    val revenueGrowth: String = "",
    val netCashFlow: String = "",
    val gstTurnover: String = "",
    val revenueTrend: List<TrendPointDto> = emptyList(),
    val cashFlows: List<CashFlowMonthDto> = emptyList(),
    val gstTaxableValues: List<GstMonthDto> = emptyList(),
    val digitalAdoptionPercentage: Int = 0,
    val costCenters: List<CostCenterDto> = emptyList(),
    val dsoDays: Int = 0,
    val dsoTrend: String = ""
)

@Serializable
data class TrendPointDto(val month: String, val value: Float)

@Serializable
data class CashFlowMonthDto(val month: String, val inflow: Float, val outflow: Float)

@Serializable
data class GstMonthDto(val month: String, val value: String, val percentage: Float)

@Serializable
data class CostCenterDto(val name: String, val percentage: Int, val iconName: String)

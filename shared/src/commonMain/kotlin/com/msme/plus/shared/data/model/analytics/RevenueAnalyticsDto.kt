package com.msme.plus.shared.data.model.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevenueAnalyticsResponseDto(
    val statusCode: Int? = null,
    val message: String? = null,
    val data: RevenueAnalyticsDto? = null
)

@Serializable
data class RevenueAnalyticsDto(
    val aiInsights: List<String>? = null,
    val totalRevenue: String? = null,
    val revenueGrowth: String? = null,
    val netCashFlow: String? = null,
    val gstTurnover: String? = null,
    val revenueTrend: List<TrendPointDto>? = null,
    val cashFlows: List<CashFlowMonthDto>? = null,
    val gstTaxableValues: List<GstMonthDto>? = null,
    val digitalAdoptionPercentage: Int? = null,
    val costCenters: List<CostCenterDto>? = null,
    val dsoDays: Int? = null,
    val dsoTrend: String? = null
)

@Serializable
data class TrendPointDto(val month: String? = null, val value: Float? = null)

@Serializable
data class CashFlowMonthDto(val month: String? = null, val inflow: Float? = null, val outflow: Float? = null)

@Serializable
data class GstMonthDto(
    val month: String? = null, 
    @SerialName("taxableValue") val value: String? = null, 
    @SerialName("progress") val percentage: Float? = null
)

@Serializable
data class CostCenterDto(
    val name: String? = null, 
    val percentage: Int? = null, 
    @SerialName("icon") val iconName: String? = null
)

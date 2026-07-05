package com.msme.plus.shared.data.model.analytics

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevenueAnalyticsResponseDto(
    val statusCode: Int = 200,
    val message: String = "",
    val data: RevenueAnalyticsDto = RevenueAnalyticsDto()
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
data class TrendPointDto(val month: String = "", val value: Float = 0f)

@Serializable
data class CashFlowMonthDto(val month: String = "", val inflow: Float = 0f, val outflow: Float = 0f)

@Serializable
data class GstMonthDto(
    val month: String = "", 
    @SerialName("taxableValue") val value: String = "", 
    @SerialName("progress") val percentage: Float = 0f
)

@Serializable
data class CostCenterDto(
    val name: String = "", 
    val percentage: Int = 0, 
    @SerialName("icon") val iconName: String = ""
)

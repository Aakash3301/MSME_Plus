package com.msme.plus.shared.domain.model.analytics

data class RevenueAnalyticsData(
    val aiInsights: List<String> = emptyList(),
    val totalRevenue: String = "",
    val revenueGrowth: String = "",
    val netCashFlow: String = "",
    val gstTurnover: String = "",
    val revenueTrend: List<TrendPoint> = emptyList(),
    val cashFlows: List<CashFlowMonth> = emptyList(),
    val gstTaxableValues: List<GstMonth> = emptyList(),
    val digitalAdoptionPercentage: Int = 0,
    val costCenters: List<CostCenter> = emptyList(),
    val dsoDays: Int = 0,
    val dsoTrend: String = ""
)

data class TrendPoint(val month: String, val value: Float)
data class CashFlowMonth(val month: String, val inflow: Float, val outflow: Float)
data class GstMonth(val month: String, val value: String, val percentage: Float)
data class CostCenter(val name: String, val percentage: Int, val iconName: String)

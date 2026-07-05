package com.msme.plus.shared.data.mapper

import com.msme.plus.shared.data.model.analytics.CashFlowMonthDto
import com.msme.plus.shared.data.model.analytics.CostCenterDto
import com.msme.plus.shared.data.model.analytics.GstMonthDto
import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsDto
import com.msme.plus.shared.data.model.analytics.TrendPointDto
import com.msme.plus.shared.domain.model.analytics.CashFlowMonth
import com.msme.plus.shared.domain.model.analytics.CostCenter
import com.msme.plus.shared.domain.model.analytics.GstMonth
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.domain.model.analytics.TrendPoint

fun RevenueAnalyticsDto.toDomain(): RevenueAnalyticsData {
    return RevenueAnalyticsData(
        aiInsights = aiInsights,
        totalRevenue = totalRevenue,
        revenueGrowth = revenueGrowth,
        netCashFlow = netCashFlow,
        gstTurnover = gstTurnover,
        revenueTrend = revenueTrend.map { it.toDomain() },
        cashFlows = cashFlows.map { it.toDomain() },
        gstTaxableValues = gstTaxableValues.map { it.toDomain() },
        digitalAdoptionPercentage = digitalAdoptionPercentage,
        costCenters = costCenters.map { it.toDomain() },
        dsoDays = dsoDays,
        dsoTrend = dsoTrend
    )
}

fun TrendPointDto.toDomain() = TrendPoint(month, value)
fun CashFlowMonthDto.toDomain() = CashFlowMonth(month, inflow, outflow)
fun GstMonthDto.toDomain() = GstMonth(month, value, percentage)
fun CostCenterDto.toDomain() = CostCenter(name, percentage, iconName)

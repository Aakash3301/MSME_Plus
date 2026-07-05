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
        aiInsights = aiInsights ?: emptyList(),
        totalRevenue = totalRevenue ?: "",
        revenueGrowth = revenueGrowth ?: "",
        netCashFlow = netCashFlow ?: "",
        gstTurnover = gstTurnover ?: "",
        revenueTrend = revenueTrend?.map { it.toDomain() } ?: emptyList(),
        cashFlows = cashFlows?.map { it.toDomain() } ?: emptyList(),
        gstTaxableValues = gstTaxableValues?.map { it.toDomain() } ?: emptyList(),
        digitalAdoptionPercentage = digitalAdoptionPercentage ?: 0,
        costCenters = costCenters?.map { it.toDomain() } ?: emptyList(),
        dsoDays = dsoDays ?: 0,
        dsoTrend = dsoTrend ?: ""
    )
}

fun TrendPointDto.toDomain() = TrendPoint(month ?: "", value ?: 0f)
fun CashFlowMonthDto.toDomain() = CashFlowMonth(month ?: "", inflow ?: 0f, outflow ?: 0f)
fun GstMonthDto.toDomain() = GstMonth(month ?: "", value ?: "", percentage ?: 0f)
fun CostCenterDto.toDomain() = CostCenter(name ?: "", percentage ?: 0, iconName ?: "")

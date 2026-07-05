package com.msme.plus.shared.data.model.analytics

import com.msme.plus.shared.domain.model.analytics.CashFlowMonth
import com.msme.plus.shared.domain.model.analytics.CostCenter
import com.msme.plus.shared.domain.model.analytics.GstMonth
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.domain.model.analytics.TrendPoint

object RevenueAnalyticsMockData {
    val analyticsData = RevenueAnalyticsData(
        aiInsights = listOf(
            "Revenue increased by 16% this quarter",
            "Cash flow remains stable with 12% surplus",
            "GST compliance is excellent (100% on-time filing)"
        ),
        totalRevenue = "₹1.2 Cr",
        revenueGrowth = "+16%",
        netCashFlow = "₹18.5 L",
        gstTurnover = "₹85 L",
        revenueTrend = listOf(
            TrendPoint("Jan", 140f),
            TrendPoint("Feb", 110f),
            TrendPoint("Mar", 120f),
            TrendPoint("Apr", 70f),
            TrendPoint("May", 60f),
            TrendPoint("Jun", 30f)
        ),
        cashFlows = listOf(
            CashFlowMonth("Apr", inflow = 80f, outflow = 64f),
            CashFlowMonth("May", inflow = 96f, outflow = 56f),
            CashFlowMonth("Jun", inflow = 112f, outflow = 72f)
        ),
        gstTaxableValues = listOf(
            GstMonth("July", "₹24.5L", 0.85f),
            GstMonth("August", "₹18.2L", 0.65f),
            GstMonth("September", "₹32.0L", 1.0f)
        ),
        digitalAdoptionPercentage = 72,
        costCenters = listOf(
            CostCenter("Inventory", 42, "inventory_2"), // we'll use fallback texts
            CostCenter("Salaries", 28, "payments"),
            CostCenter("Utilities", 12, "bolt")
        ),
        dsoDays = 34,
        dsoTrend = "-4 days vs LY"
    )
}

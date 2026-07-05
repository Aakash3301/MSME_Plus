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
    val REVENUE_ANALYTICS_JSON = """
        {
            "statusCode": 200,
            "message": "Success",
            "data": {
                "aiInsights": [
                    "Revenue increased by 16% this quarter",
                    "Cash flow remains stable with 12% surplus",
                    "GST compliance is excellent (100% on-time filing)"
                ],
                "totalRevenue": "₹1.2 Cr",
                "revenueGrowth": "+16%",
                "netCashFlow": "₹18.5 L",
                "gstTurnover": "₹85 L",
                "revenueTrend": [
                    {"month": "Jan", "value": 140.0},
                    {"month": "Feb", "value": 110.0},
                    {"month": "Mar", "value": 120.0},
                    {"month": "Apr", "value": 70.0},
                    {"month": "May", "value": 60.0},
                    {"month": "Jun", "value": 30.0}
                ],
                "cashFlows": [
                    {"month": "Apr", "inflow": 80.0, "outflow": 64.0},
                    {"month": "May", "inflow": 96.0, "outflow": 56.0},
                    {"month": "Jun", "inflow": 112.0, "outflow": 72.0}
                ],
                "gstTaxableValues": [
                    {"month": "July", "value": "₹24.5L", "percentage": 0.85},
                    {"month": "August", "value": "₹18.2L", "percentage": 0.65},
                    {"month": "September", "value": "₹32.0L", "percentage": 1.0}
                ],
                "digitalAdoptionPercentage": 72,
                "costCenters": [
                    {"name": "Inventory", "percentage": 42, "iconName": "inventory_2"},
                    {"name": "Salaries", "percentage": 28, "iconName": "payments"},
                    {"name": "Utilities", "percentage": 12, "iconName": "bolt"}
                ],
                "dsoDays": 34,
                "dsoTrend": "-4 days vs LY"
            }
        }
    """.trimIndent()
}

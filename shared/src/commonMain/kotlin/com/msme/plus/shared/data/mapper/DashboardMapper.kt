package com.msme.plus.shared.data.mapper

import com.msme.plus.shared.data.model.dashboard.DashboardDto
import com.msme.plus.shared.data.model.dashboard.HealthScoreDto
import com.msme.plus.shared.data.model.dashboard.KpiDto
import com.msme.plus.shared.data.model.dashboard.LoanEligibilityDto
import com.msme.plus.shared.data.model.dashboard.QuickActionDto
import com.msme.plus.shared.domain.model.dashboard.DashboardData
import com.msme.plus.shared.domain.model.dashboard.HealthScore
import com.msme.plus.shared.domain.model.dashboard.Kpi
import com.msme.plus.shared.domain.model.dashboard.KpiColorType
import com.msme.plus.shared.domain.model.dashboard.LoanEligibility
import com.msme.plus.shared.domain.model.dashboard.QuickAction
import com.msme.plus.shared.domain.model.dashboard.TrendDirection

fun DashboardDto.toDomain(): DashboardData {
    return DashboardData(
        companyName = this.companyName,
        sector = this.sector,
        hasNotifications = this.hasNotifications,
        profileImageUrl = this.profileImageUrl,
        healthScore = this.healthScore.toDomain(),
        kpis = this.kpis.map { it.toDomain() },
        loanEligibility = this.loanEligibility.toDomain(),
        quickActions = this.quickActions.map { it.toDomain() }
    )
}

fun HealthScoreDto.toDomain(): HealthScore {
    return HealthScore(
        score = this.score,
        maxScore = this.maxScore,
        statusText = this.statusText,
        tags = this.tags
    )
}

fun KpiDto.toDomain(): Kpi {
    return Kpi(
        id = this.id,
        icon = this.icon,
        title = this.title,
        value = this.value,
        trend = this.trend,
        trendDirection = when (this.trendDirection.lowercase()) {
            "up" -> TrendDirection.UP
            "down" -> TrendDirection.DOWN
            else -> TrendDirection.NEUTRAL
        },
        colorType = when (this.colorType.lowercase()) {
            "primary" -> KpiColorType.PRIMARY
            "secondary" -> KpiColorType.SECONDARY
            "tertiary" -> KpiColorType.TERTIARY
            else -> KpiColorType.PRIMARY
        }
    )
}

fun LoanEligibilityDto.toDomain(): LoanEligibility {
    return LoanEligibility(
        status = this.status,
        amountText = this.amountText,
        confidencePercentage = this.confidencePercentage
    )
}

fun QuickActionDto.toDomain(): QuickAction {
    return QuickAction(
        id = this.id,
        icon = this.icon,
        title = this.title
    )
}

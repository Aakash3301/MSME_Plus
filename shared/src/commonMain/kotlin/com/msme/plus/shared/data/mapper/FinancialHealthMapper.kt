package com.msme.plus.shared.data.mapper

import com.msme.plus.shared.data.model.health.FinancialHealthDto
import com.msme.plus.shared.data.model.health.HealthBadgeDto
import com.msme.plus.shared.data.model.health.LoanOfferDto
import com.msme.plus.shared.data.model.health.ScoreBreakdownDto
import com.msme.plus.shared.domain.model.health.BadgeType
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import com.msme.plus.shared.domain.model.health.HealthBadge
import com.msme.plus.shared.domain.model.health.LoanOffer
import com.msme.plus.shared.domain.model.health.ScoreBreakdown

fun FinancialHealthDto.toDomain(): FinancialHealthData {
    return FinancialHealthData(
        companyName = companyName,
        profileImageUrl = profileImageUrl,
        overallScore = overallScore,
        maxScore = maxScore,
        statusText = statusText,
        badges = badges.map { it.toDomain() },
        scoreBreakdowns = scoreBreakdowns.map { it.toDomain() },
        strengths = strengths,
        risks = risks,
        loanOffer = loanOffer?.toDomain()
    )
}

fun HealthBadgeDto.toDomain(): HealthBadge {
    val parsedType = try {
        BadgeType.valueOf(type)
    } catch (e: Exception) {
        BadgeType.PRIMARY
    }
    return HealthBadge(
        text = text,
        icon = icon,
        type = parsedType
    )
}

fun ScoreBreakdownDto.toDomain(): ScoreBreakdown {
    val parsedType = try {
        BadgeType.valueOf(color)
    } catch (e: Exception) {
        BadgeType.PRIMARY
    }
    return ScoreBreakdown(
        label = label,
        value = value,
        color = parsedType
    )
}

fun LoanOfferDto.toDomain(): LoanOffer {
    return LoanOffer(
        title = title,
        description = description,
        buttonText = buttonText
    )
}

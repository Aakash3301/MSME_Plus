package com.msme.plus.shared.data.repository.recommendations

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.recommendations.AiRecommendation
import com.msme.plus.shared.domain.model.recommendations.RecommendationPriority
import com.msme.plus.shared.domain.repository.recommendations.AiRecommendationsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AiRecommendationsRepositoryImpl : AiRecommendationsRepository {

    override fun getRecommendations(): Flow<Resource<List<AiRecommendation>>> = flow<Resource<List<AiRecommendation>>> {
        emit(Resource.Loading)
        delay(1500) // Simulating network delay for shimmer effect
        emit(Resource.Success(
            listOf(
                AiRecommendation(
                    id = "1",
                    title = "Improve Cash Reserve by 15%",
                    description = "Increasing liquid reserves provides a buffer against volatility, significantly improving debt serviceability perception.",
                    priority = RecommendationPriority.HIGH,
                    icon = "💰",
                    scoreImprovement = "+12 pts",
                    timeline = "90 Days"
                ),
                AiRecommendation(
                    id = "2",
                    title = "Complete Account Aggregator Consent",
                    description = "Providing digital verified data access reduces information asymmetry and allows for higher credit limits.",
                    priority = RecommendationPriority.HIGH,
                    icon = "🔐",
                    scoreImprovement = "+8 pts",
                    timeline = "Instant"
                ),
                AiRecommendation(
                    id = "3",
                    title = "Increase Digital Transactions",
                    description = "Moving 30% of cash sales to digital channels (UPI/Card) builds a verifiable trail of revenue consistency.",
                    priority = RecommendationPriority.HIGH,
                    icon = "📱",
                    scoreImprovement = "+6 pts",
                    timeline = "60 Days"
                ),
                AiRecommendation(
                    id = "4",
                    title = "Reduce Receivable Cycle",
                    description = "Optimizing invoice collections from 45 to 30 days will improve working capital efficiency and credit rating.",
                    priority = RecommendationPriority.MEDIUM,
                    icon = "⏱️",
                    scoreImprovement = "+7 pts",
                    timeline = "45 Days"
                ),
                AiRecommendation(
                    id = "5",
                    title = "Improve Vendor Payment Discipline",
                    description = "Ensuring timely payments to key suppliers reduces external liability risk alerts in trade reports.",
                    priority = RecommendationPriority.MEDIUM,
                    icon = "💳",
                    scoreImprovement = "+4 pts",
                    timeline = "30 Days"
                ),
                AiRecommendation(
                    id = "6",
                    title = "Maintain GST Compliance",
                    description = "Consistent and timely GST filing is critical for maintaining current score. Lapses will result in immediate penalty.",
                    priority = RecommendationPriority.HIGH,
                    icon = "✅",
                    scoreImprovement = "Retention",
                    timeline = "Ongoing"
                )
            )
        ))
    }
}

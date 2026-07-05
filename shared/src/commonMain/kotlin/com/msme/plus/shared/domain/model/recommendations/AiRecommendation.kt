package com.msme.plus.shared.domain.model.recommendations

enum class RecommendationPriority {
    HIGH, MEDIUM, LOW
}

data class AiRecommendation(
    val id: String,
    val title: String,
    val description: String,
    val priority: RecommendationPriority,
    val icon: String,
    val scoreImprovement: String,
    val timeline: String
)

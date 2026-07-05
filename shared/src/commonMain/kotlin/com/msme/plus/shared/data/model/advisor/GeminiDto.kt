package com.msme.plus.shared.data.model.advisor

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequestDto(
    val contents: List<GeminiContentDto>
)

@Serializable
data class GeminiContentDto(
    val parts: List<GeminiPartDto>,
    val role: String? = null
)

@Serializable
data class GeminiPartDto(
    val text: String
)

@Serializable
data class GeminiResponseDto(
    val candidates: List<GeminiCandidateDto>? = null
)

@Serializable
data class GeminiCandidateDto(
    val content: GeminiContentDto? = null
)

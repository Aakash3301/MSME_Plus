package com.msme.plus.shared.data.model.health

import kotlinx.serialization.Serializable

@Serializable
data class FinancialHealthResponseDto(
    val statusCode: Int,
    val message: String,
    val data: FinancialHealthDto
)

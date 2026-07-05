package com.msme.plus.shared.data.model.health

import kotlinx.serialization.Serializable

@Serializable
data class FinancialHealthResponseDto(
    val statusCode: Int? = null,
    val message: String? = null,
    val data: FinancialHealthDto? = null
)

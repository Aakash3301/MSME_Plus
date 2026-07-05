package com.msme.plus.shared.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponseDto(
    val statusCode: Int? = null,
    val message: String? = null,
    val data: DashboardDto? = null
)

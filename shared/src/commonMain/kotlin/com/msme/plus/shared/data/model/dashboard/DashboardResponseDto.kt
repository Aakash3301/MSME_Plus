package com.msme.plus.shared.data.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponseDto(
    val statusCode: Int,
    val message: String,
    val data: DashboardDto
)

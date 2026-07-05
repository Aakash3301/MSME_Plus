package com.msme.plus.shared.data.network.dto

import com.msme.plus.shared.domain.models.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val mobileNumber: String? = null
)

@Serializable
data class LoginResponseDto(
    val statusCode: Int? = null,
    val message: String? = null,
    val data: LoginResponseData? = null
)

@Serializable
data class LoginResponseData(
    val token: AuthToken? = null,
    val msme: MsmeProfileDto? = null,
    val valid: Boolean? = null
)

@Serializable
data class MsmeProfileDto(
    val id: String? = null,
    val businessName: String? = null,
    val pan: String? = null,
    val gstNumber: String? = null,
    val industryType: String? = null,
    val mobileNumber: String? = null,
    val createdAt: String? = null
)

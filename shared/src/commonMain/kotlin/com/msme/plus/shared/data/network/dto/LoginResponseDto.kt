package com.msme.plus.shared.data.network.dto

import com.msme.plus.shared.domain.models.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val mobileNumber: String
)

@Serializable
data class LoginResponseDto(
    val statusCode: Int,
    val message: String,
    val data: LoginResponseData? = null
)

@Serializable
data class LoginResponseData(
    val token: AuthToken,
    val msme: MsmeProfileDto,
    val valid: Boolean
)

@Serializable
data class MsmeProfileDto(
    val id: String,
    val businessName: String,
    val pan: String,
    val gstNumber: String,
    val industryType: String,
    val mobileNumber: String,
    val createdAt: String
)

package com.msme.plus.shared.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

@Serializable
data class MsmeProfile(
    val id: String,
    val businessName: String,
    val pan: String,
    val gstNumber: String,
    val industryType: String,
    val mobileNumber: String,
    val createdAt: String
)

@Serializable
data class UserSession(
    val isValid: Boolean,
    val token: AuthToken? = null,
    val msme: MsmeProfile? = null,
    val errorMessage: String? = null
)

package com.msme.plus.shared.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

@Serializable
data class UserSession(
    val isValid: Boolean,
    val token: AuthToken? = null,
    val errorMessage: String? = null
)

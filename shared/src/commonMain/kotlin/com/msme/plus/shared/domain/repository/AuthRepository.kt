package com.msme.plus.shared.domain.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.models.UserSession

interface AuthRepository {
    suspend fun checkTokenValidity(): Resource<UserSession>
    suspend fun login(mobile: String, gstin: String?): Resource<UserSession>
    suspend fun logout()
}

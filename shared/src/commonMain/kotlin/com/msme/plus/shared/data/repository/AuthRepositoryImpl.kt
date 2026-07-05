package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.MockJsonData
import com.msme.plus.shared.domain.models.UserSession
import com.msme.plus.shared.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val settingsManager: SettingsManager
) : AuthRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun checkTokenValidity(): Resource<UserSession> {
        return safeApiCall {
            delay(1500)
            
            val localToken = settingsManager.getToken()
            if (localToken != null) {
                // If token exists, mock a successful response instead of using TOKEN_JSON
                UserSession(
                    isValid = true,
                    token = com.msme.plus.shared.domain.models.AuthToken(
                        accessToken = localToken,
                        refreshToken = "mock_refresh_token",
                        expiresIn = 3600
                    )
                )
            } else {
                // Parse the mock token.json data which defaults to invalid
                json.decodeFromString(
                    UserSession.serializer(),
                    MockJsonData.TOKEN_JSON
                )
            }
        }
    }

    override suspend fun login(mobile: String, gstin: String?): Resource<UserSession> {
        return safeApiCall {
            delay(1500)

            if (mobile.length < 10) {
                throw Exception("Invalid mobile number")
            }

            val session = json.decodeFromString(
                UserSession.serializer(),
                MockJsonData.LOGIN_JSON
            )
            
            // Save the token upon successful login
            session.token?.accessToken?.let { settingsManager.saveToken(it) }
            
            session
        }
    }

    override suspend fun logout() {
        // Clear local token
        settingsManager.clearToken()
    }
}

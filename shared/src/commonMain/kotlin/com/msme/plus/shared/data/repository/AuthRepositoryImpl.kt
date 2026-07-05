package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.MockJsonData
import com.msme.plus.shared.domain.models.UserSession
import com.msme.plus.shared.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.domain.models.MsmeProfile
import kotlinx.serialization.encodeToString

class AuthRepositoryImpl(
    private val settingsManager: SettingsManager,
    private val apiService: ApiService
) : AuthRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun checkTokenValidity(): Resource<UserSession> {
        return safeApiCall {
            delay(1500)
            
            val localToken = settingsManager.getToken()
            if (localToken != null) {
                // If token exists, mock a successful response instead of using TOKEN_JSON
                val msmeJson = settingsManager.getMsmeProfile()
                val msme = msmeJson?.let { json.decodeFromString(MsmeProfile.serializer(), it) }
                UserSession(
                    isValid = true,
                    token = com.msme.plus.shared.domain.models.AuthToken(
                        accessToken = localToken,
                        refreshToken = "mock_refresh_token",
                        expiresIn = 3600
                    ),
                    msme = msme
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
            if (mobile.length < 10) {
                throw Exception("Invalid mobile number")
            }

            // --- OLD MOCK CALL (kept commented out as requested) ---
            /*
            delay(1500)
            val mockResponse = json.decodeFromString(
                com.msme.plus.shared.data.network.dto.LoginResponseDto.serializer(),
                MockJsonData.LOGIN_JSON
            )
            val data = mockResponse.data!!
            val msmeProfile = MsmeProfile(
                id = data.msme.id,
                businessName = data.msme.businessName,
                pan = data.msme.pan,
                gstNumber = data.msme.gstNumber,
                industryType = data.msme.industryType,
                mobileNumber = data.msme.mobileNumber,
                createdAt = data.msme.createdAt
            )
            val session = UserSession(
                isValid = data.valid,
                token = data.token,
                msme = msmeProfile
            )
            session.token.accessToken.let { settingsManager.saveToken(it) }
            settingsManager.saveMsmeProfile(json.encodeToString(MsmeProfile.serializer(), msmeProfile))
            session
            */

            // --- REAL API CALL ---
            val response = apiService.login(mobile)
            
            if (response.statusCode != 200 || response.data == null) {
                throw Exception(response.message)
            }

            val data = response.data
            
            // Map DTO to Domain Model
            val msmeProfile = MsmeProfile(
                id = data.msme.id,
                businessName = data.msme.businessName,
                pan = data.msme.pan,
                gstNumber = data.msme.gstNumber,
                industryType = data.msme.industryType,
                mobileNumber = data.msme.mobileNumber,
                createdAt = data.msme.createdAt
            )

            val session = UserSession(
                isValid = data.valid,
                token = data.token,
                msme = msmeProfile
            )
            
            // Save token and profile to settings
            session.token?.accessToken?.let { settingsManager.saveToken(it) }
            settingsManager.saveMsmeProfile(json.encodeToString(MsmeProfile.serializer(), msmeProfile))
            
            session
        }
    }

    override suspend fun logout() {
        // Clear local token
        settingsManager.clearToken()
    }
}

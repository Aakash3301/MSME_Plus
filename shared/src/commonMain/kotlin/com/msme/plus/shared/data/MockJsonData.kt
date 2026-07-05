package com.msme.plus.shared.data

object MockJsonData {
    // Represents the response when checking if a user token is valid
    val TOKEN_JSON = """
        {
            "isValid": false,
            "errorMessage": "Session expired. Please login again."
        }
    """.trimIndent()

    val LOGIN_JSON = """
        {
            "isValid": true,
            "token": {
                "accessToken": "mock_access_token_123",
                "refreshToken": "mock_refresh_token_456",
                "expiresIn": 3600
            }
        }
    """.trimIndent()
}

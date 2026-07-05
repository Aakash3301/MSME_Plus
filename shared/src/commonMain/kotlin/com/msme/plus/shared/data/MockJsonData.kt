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
            "statusCode": 200,
            "message": "Login successful",
            "data": {
                "token": {
                    "accessToken": "mock_access_token_123",
                    "refreshToken": "mock_refresh_token_456",
                    "expiresIn": 3600
                },
                "msme": {
                    "id": "fadfda6e-075b-4fd3-b3ec-e668e22ea413",
                    "businessName": "Acme Arcane Solutions",
                    "pan": "ABCDE1234675",
                    "gstNumber": "22AAAAA1111A187",
                    "industryType": "MANUFACTURING",
                    "mobileNumber": "8795381476",
                    "createdAt": "2026-07-05T14:01:22.198979"
                },
                "valid": true
            }
        }
    """.trimIndent()
}

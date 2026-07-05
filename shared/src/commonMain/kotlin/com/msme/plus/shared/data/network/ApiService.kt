package com.msme.plus.shared.data.network

import com.msme.plus.shared.core.network.ApiConstant
import com.msme.plus.shared.data.network.dto.LoginRequestDto
import com.msme.plus.shared.data.network.dto.LoginResponseDto
import com.msme.plus.shared.data.model.dashboard.DashboardResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

import io.ktor.client.request.header

class ApiService(private val httpClient: HttpClient) {
    suspend fun login(mobileNumber: String): LoginResponseDto {
        return httpClient.post("${ApiConstant.BASE_URL}/api/v1/msmes/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(mobileNumber = mobileNumber))
        }.body()
    }

    suspend fun getDashboard(msmeId: String): DashboardResponseDto {
        return httpClient.get("${ApiConstant.BASE_URL}/api/v1/dashboard/$msmeId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getFinancialHealth(msmeId: String): com.msme.plus.shared.data.model.health.FinancialHealthResponseDto {
        return httpClient.get("${ApiConstant.BASE_URL}/api/v1/msmes/$msmeId/health-card") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getRevenueAnalytics(msmeId: String): com.msme.plus.shared.data.model.analytics.RevenueAnalyticsResponseDto {
        return httpClient.get("${ApiConstant.BASE_URL}/api/v1/msmes/$msmeId/revenue-analytics") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun assessLoan(request: com.msme.plus.shared.data.model.loan.LoanAssessmentRequestDto): com.msme.plus.shared.data.model.loan.LoanAssessmentResponseDto {
        return httpClient.post("${ApiConstant.BASE_URL}/api/v1/msmes/loanAssessment") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun askGemini(request: com.msme.plus.shared.data.model.advisor.GeminiRequestDto): com.msme.plus.shared.data.model.advisor.GeminiResponseDto {
        val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent"
        val response = httpClient.post(endpoint) {
            contentType(ContentType.Application.Json)
            header("X-goog-api-key", ApiConstant.GEMINI_API_KEY)
            setBody(request)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Gemini API Error: ${response.bodyAsText()}")
        }
        return response.body()
    }
}

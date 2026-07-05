package com.msme.plus.shared.data.network

import com.msme.plus.shared.core.network.ApiConstant
import com.msme.plus.shared.data.network.dto.LoginRequestDto
import com.msme.plus.shared.data.network.dto.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(private val httpClient: HttpClient) {
    suspend fun login(mobileNumber: String): LoginResponseDto {
        return httpClient.post("${ApiConstant.BASE_URL}}api/v1/msmes/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(mobileNumber = mobileNumber))
        }.body()
    }
}

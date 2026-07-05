package com.msme.plus.shared.core.network

import kotlinx.coroutines.CancellationException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(apiCall())
    } catch (e: Exception) {
        when (e) {
            is CancellationException -> throw e
            is ClientRequestException -> {
                Resource.Error(e, "Client request error: ${e.response.status}")
            }
            is ServerResponseException -> {
                Resource.Error(e, "Server response error: ${e.response.status}")
            }
            else -> {
                Resource.Error(e, e.message ?: "An unknown error occurred")
            }
        }
    }
}

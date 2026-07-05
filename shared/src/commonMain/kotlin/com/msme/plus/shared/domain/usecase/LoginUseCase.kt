package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.models.UserSession
import com.msme.plus.shared.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(mobile: String, gstin: String?): Flow<Resource<UserSession>> = flow {
        // Business Validation
        if (mobile.isBlank() || mobile.length < 10) {
            emit(Resource.Error(Exception("Invalid mobile number"), "Please enter a valid mobile number."))
            return@flow
        }

        emit(Resource.Loading)
        val result = repository.login(mobile, gstin)
        emit(result)
    }
}

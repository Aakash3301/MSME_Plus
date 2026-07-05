package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.models.UserSession
import com.msme.plus.shared.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckTokenValidityUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<UserSession>> = flow {
        emit(Resource.Loading)
        val result = repository.checkTokenValidity()
        emit(result)
    }
}

package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}

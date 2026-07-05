package com.msme.plus.shared.domain.usecase.profile

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.profile.BusinessProfileData
import com.msme.plus.shared.domain.repository.profile.BusinessProfileRepository
import kotlinx.coroutines.flow.Flow

class GetBusinessProfileUseCase(
    private val repository: BusinessProfileRepository
) {
    operator fun invoke(): Flow<Resource<BusinessProfileData>> {
        return repository.getBusinessProfile()
    }
}

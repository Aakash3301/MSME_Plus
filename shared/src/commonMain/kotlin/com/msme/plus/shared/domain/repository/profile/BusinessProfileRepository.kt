package com.msme.plus.shared.domain.repository.profile

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.profile.BusinessProfileData
import kotlinx.coroutines.flow.Flow

interface BusinessProfileRepository {
    fun getBusinessProfile(): Flow<Resource<BusinessProfileData>>
}

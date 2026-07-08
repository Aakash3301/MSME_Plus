package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.domain.model.profile.BusinessProfileData
import com.msme.plus.shared.domain.models.MsmeProfile
import com.msme.plus.shared.domain.repository.profile.BusinessProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class BusinessProfileRepositoryImpl(
    private val settingsManager: SettingsManager
) : BusinessProfileRepository {
    val json = Json { ignoreUnknownKeys = true }
    override fun getBusinessProfile(): Flow<Resource<BusinessProfileData>> = flow<Resource<BusinessProfileData>> {
        emit(Resource.Loading)

        // Simulate network delay for shimmer effect
        delay(1500)
        val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("Profile not found")
        val msmeData = json.decodeFromString<MsmeProfile>(msmeJson)

        val mockData = BusinessProfileData(
            companyName = msmeData?.businessName ?:"ABC Manufacturing Pvt Ltd",
            companyType = "Precision Engineering ",
            tier = "Gold Tier Merchant",
            gstin = "29AAAAA0000A1Z5",
            panCard = "ABCDE1234F",
            businessAge = "8 Years (Est. 2016)",
            registeredAddress = "123 Industrial Estate, Phase 2, Bangalore, KA - 560058",
            directorName = "Rajesh Kumar",
            email = "rajesh.kumar@abcmanf.in",
            isNotificationsEnabled = true
        )
        
        emit(Resource.Success(mockData))
    }
}

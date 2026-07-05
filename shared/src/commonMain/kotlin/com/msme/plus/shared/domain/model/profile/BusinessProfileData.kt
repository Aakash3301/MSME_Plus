package com.msme.plus.shared.domain.model.profile

data class BusinessProfileData(
    val companyName: String,
    val companyType: String,
    val tier: String,
    val gstin: String,
    val panCard: String,
    val businessAge: String,
    val registeredAddress: String,
    val directorName: String,
    val email: String,
    val isNotificationsEnabled: Boolean
)

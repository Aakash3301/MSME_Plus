package com.msme.plus.shared.features.profile

fun businessProfileReducer(
    state: BusinessProfileState,
    result: BusinessProfileResult
): BusinessProfileState {
    return when (result) {
        is BusinessProfileResult.Loading -> {
            state.copy(isLoading = true, error = null)
        }
        is BusinessProfileResult.Success -> {
            state.copy(
                isLoading = false,
                profileData = result.profileData,
                error = null
            )
        }
        is BusinessProfileResult.Error -> {
            state.copy(
                isLoading = false,
                error = result.message
            )
        }
        is BusinessProfileResult.NotificationsToggled -> {
            state.copy(
                profileData = state.profileData?.copy(isNotificationsEnabled = result.enabled)
            )
        }
    }
}

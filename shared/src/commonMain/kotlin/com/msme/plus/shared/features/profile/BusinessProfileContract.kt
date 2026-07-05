package com.msme.plus.shared.features.profile

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.profile.BusinessProfileData

data class BusinessProfileState(
    val isLoading: Boolean = false,
    val profileData: BusinessProfileData? = null,
    val error: String? = null
) : UiState

sealed interface BusinessProfileIntent : UiIntent {
    data object LoadProfile : BusinessProfileIntent
    data object NavigateBack : BusinessProfileIntent
    data object NavigateToDashboard : BusinessProfileIntent
    data class ToggleNotifications(val enabled: Boolean) : BusinessProfileIntent
    data object Logout : BusinessProfileIntent
    data class PerformAction(val actionName: String) : BusinessProfileIntent
}

sealed interface BusinessProfileAction : UiAction {
    data object LoadProfile : BusinessProfileAction
    data object NavigateBack : BusinessProfileAction
    data object NavigateToDashboard : BusinessProfileAction
    data class ToggleNotifications(val enabled: Boolean) : BusinessProfileAction
    data object Logout : BusinessProfileAction
    data class PerformAction(val actionName: String) : BusinessProfileAction
}

sealed interface BusinessProfileResult : UiResult {
    data object Loading : BusinessProfileResult
    data class Success(val profileData: BusinessProfileData) : BusinessProfileResult
    data class Error(val message: String) : BusinessProfileResult
    data class NotificationsToggled(val enabled: Boolean) : BusinessProfileResult
}

sealed interface BusinessProfileEffect : UiEffect {
    data object NavigateBack : BusinessProfileEffect
    data object NavigateToDashboard : BusinessProfileEffect
    data object NavigateToLogin : BusinessProfileEffect
    data class ShowToast(val message: String) : BusinessProfileEffect
}

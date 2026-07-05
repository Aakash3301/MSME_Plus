package com.msme.plus.shared.features.profile

import androidx.lifecycle.viewModelScope
import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.profile.GetBusinessProfileUseCase
import kotlinx.coroutines.launch

class BusinessProfileViewModel(
    private val getBusinessProfileUseCase: GetBusinessProfileUseCase
) : Store<BusinessProfileState, BusinessProfileIntent, BusinessProfileAction, BusinessProfileResult, BusinessProfileEffect>(
    initialState = BusinessProfileState()
) {

    override fun intentToAction(intent: BusinessProfileIntent): BusinessProfileAction {
        return when (intent) {
            is BusinessProfileIntent.LoadProfile -> BusinessProfileAction.LoadProfile
            is BusinessProfileIntent.NavigateBack -> BusinessProfileAction.NavigateBack
            is BusinessProfileIntent.NavigateToDashboard -> BusinessProfileAction.NavigateToDashboard
            is BusinessProfileIntent.ToggleNotifications -> BusinessProfileAction.ToggleNotifications(intent.enabled)
            is BusinessProfileIntent.Logout -> BusinessProfileAction.Logout
            is BusinessProfileIntent.PerformAction -> BusinessProfileAction.PerformAction(intent.actionName)
        }
    }

    override suspend fun executeAction(action: BusinessProfileAction) {
        when (action) {
            is BusinessProfileAction.LoadProfile -> {
                viewModelScope.launch {
                    getBusinessProfileUseCase().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> dispatch(BusinessProfileResult.Loading)
                            is Resource.Success -> dispatch(BusinessProfileResult.Success(resource.data))
                            is Resource.Error -> dispatch(BusinessProfileResult.Error(resource.message ?: resource.exception.message ?: "Failed to load profile"))
                        }
                    }
                }
            }
            is BusinessProfileAction.NavigateBack -> {
                viewModelScope.launch { emitEffect(BusinessProfileEffect.NavigateBack) }
            }
            is BusinessProfileAction.NavigateToDashboard -> {
                viewModelScope.launch { emitEffect(BusinessProfileEffect.NavigateToDashboard) }
            }
            is BusinessProfileAction.ToggleNotifications -> {
                dispatch(BusinessProfileResult.NotificationsToggled(action.enabled))
            }
            is BusinessProfileAction.Logout -> {
                viewModelScope.launch { emitEffect(BusinessProfileEffect.NavigateToLogin) }
            }
            is BusinessProfileAction.PerformAction -> {
                viewModelScope.launch { emitEffect(BusinessProfileEffect.ShowToast("${action.actionName} Selected")) }
            }
        }
    }

    override fun reduce(state: BusinessProfileState, result: BusinessProfileResult): BusinessProfileState {
        return businessProfileReducer(state, result)
    }
}

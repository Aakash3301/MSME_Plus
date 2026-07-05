package com.msme.plus.shared.features.login

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState

// Empty file - classes are in their own files
sealed interface LoginAction : UiAction {
    data class SubmitLogin(val mobile: String, val gstin: String) : LoginAction
    data class UpdateMobileNumber(val mobile: String) : LoginAction
    data class UpdateGstin(val gstin: String) : LoginAction
}

sealed interface LoginEffect : UiEffect {
    data object NavigateToDashboard : LoginEffect
    data class ShowToast(val message: String) : LoginEffect
}

sealed interface LoginResult : UiResult {
    data class MobileNumberUpdated(val mobile: String) : LoginResult
    data class GstinUpdated(val gstin: String) : LoginResult
    data object Loading : LoginResult
    data object Success : LoginResult
    data class Error(val message: String) : LoginResult
}


sealed interface LoginIntent : UiIntent {
    data class MobileNumberChanged(val mobile: String) : LoginIntent
    data class GstinChanged(val gstin: String) : LoginIntent
    data object SubmitClicked : LoginIntent
}

data class LoginState(
    val mobileNumber: String = "",
    val gstin: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

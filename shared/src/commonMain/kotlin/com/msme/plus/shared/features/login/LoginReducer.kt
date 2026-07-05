package com.msme.plus.shared.features.login

fun loginReducer(state: LoginState, result: LoginResult): LoginState {
    return when (result) {
        is LoginResult.MobileNumberUpdated -> state.copy(mobileNumber = result.mobile, error = null)
        is LoginResult.GstinUpdated -> state.copy(gstin = result.gstin, error = null)
        is LoginResult.Loading -> state.copy(isLoading = true, error = null)
        is LoginResult.Success -> state.copy(isLoading = false, error = null)
        is LoginResult.Error -> state.copy(isLoading = false, error = result.message)
    }
}

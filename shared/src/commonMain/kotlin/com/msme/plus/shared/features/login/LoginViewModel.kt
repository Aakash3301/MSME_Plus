package com.msme.plus.shared.features.login

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : Store<LoginState, LoginIntent, LoginAction, LoginResult, LoginEffect>(LoginState()) {

    override fun intentToAction(intent: LoginIntent): LoginAction {
        return when (intent) {
            is LoginIntent.MobileNumberChanged -> LoginAction.UpdateMobileNumber(intent.mobile)
            is LoginIntent.GstinChanged -> LoginAction.UpdateGstin(intent.gstin)
            is LoginIntent.SubmitClicked -> {
                val state = currentState
                LoginAction.SubmitLogin(state.mobileNumber, state.gstin)
            }
        }
    }

    override suspend fun executeAction(action: LoginAction) {
        when (action) {
            is LoginAction.UpdateMobileNumber -> {
                dispatch(LoginResult.MobileNumberUpdated(action.mobile))
            }
            is LoginAction.UpdateGstin -> {
                dispatch(LoginResult.GstinUpdated(action.gstin))
            }
            is LoginAction.SubmitLogin -> {
                loginUseCase(action.mobile, action.gstin).collect { resource ->
                    when (resource) {
                        is Resource.Loading -> dispatch(LoginResult.Loading)
                        is Resource.Success -> {
                            dispatch(LoginResult.Success)
                            emitEffect(LoginEffect.NavigateToDashboard)
                        }
                        is Resource.Error -> {
                            val errorMsg = resource.message ?: "Login failed"
                            dispatch(LoginResult.Error(errorMsg))
                            emitEffect(LoginEffect.ShowToast(errorMsg))
                        }
                    }
                }
            }
        }
    }

    override fun reduce(state: LoginState, result: LoginResult): LoginState {
        // Delegate state reduction to the pure external function
        return loginReducer(state, result)
    }
}

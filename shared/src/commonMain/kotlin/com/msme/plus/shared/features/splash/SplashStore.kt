package com.msme.plus.shared.features.splash

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.usecase.CheckTokenValidityUseCase

// ─── State ───────────────────────────────────────────────────────────
data class SplashState(
    val isLoading: Boolean = false
) : UiState

// ─── Intent (from UI) ────────────────────────────────────────────────
sealed interface SplashIntent : UiIntent {
    data object CheckAuthStatus : SplashIntent
}

// ─── Action (internal operation) ─────────────────────────────────────
sealed interface SplashAction : UiAction {
    data object VerifyToken : SplashAction
}

// ─── Result (outcome fed into Reducer) ───────────────────────────────
sealed interface SplashResult : UiResult {
    data object Loading : SplashResult
    data object TokenValid : SplashResult
    data object TokenInvalid : SplashResult
    data class Error(val message: String) : SplashResult
}

// ─── Effect (one-shot navigation/toast) ──────────────────────────────
sealed interface SplashEffect : UiEffect {
    data object NavigateToLogin : SplashEffect
    data object NavigateToDashboard : SplashEffect
    data class ShowError(val message: String) : SplashEffect
}

// ─── ViewModel (Store) ───────────────────────────────────────────────
class SplashViewModel(
    private val checkTokenValidityUseCase: CheckTokenValidityUseCase
) : Store<SplashState, SplashIntent, SplashAction, SplashResult, SplashEffect>(SplashState()) {

    // Pure mapping: Intent → Action
    override fun intentToAction(intent: SplashIntent): SplashAction {
        return when (intent) {
            is SplashIntent.CheckAuthStatus -> SplashAction.VerifyToken
        }
    }

    // Side effects: Action → dispatch Results + emit Effects
    override suspend fun executeAction(action: SplashAction) {
        when (action) {
            is SplashAction.VerifyToken -> {
                checkTokenValidityUseCase().collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            dispatch(SplashResult.Loading)
                        }
                        is Resource.Success -> {
                            if (resource.data.isValid) {
                                dispatch(SplashResult.TokenValid)
                                emitEffect(SplashEffect.NavigateToDashboard)
                            } else {
                                dispatch(SplashResult.TokenInvalid)
                                emitEffect(SplashEffect.NavigateToLogin)
                            }
                        }
                        is Resource.Error -> {
                            dispatch(SplashResult.Error(resource.message ?: "Auth check failed"))
                            emitEffect(SplashEffect.ShowError(resource.message ?: "Auth check failed"))
                            emitEffect(SplashEffect.NavigateToLogin)
                        }
                    }
                }
            }
        }
    }

    // PURE REDUCER: (State, Result) → New State
    // No side effects, no coroutines — just a pure function.
    override fun reduce(state: SplashState, result: SplashResult): SplashState {
        return when (result) {
            is SplashResult.Loading -> state.copy(isLoading = true)
            is SplashResult.TokenValid -> state.copy(isLoading = false)
            is SplashResult.TokenInvalid -> state.copy(isLoading = false)
            is SplashResult.Error -> state.copy(isLoading = false)
        }
    }
}

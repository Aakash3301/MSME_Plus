package com.msme.plus.shared.core.redux

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

/**
 * Marker interfaces for the MVI contract.
 */
interface UiState
interface UiIntent
interface UiAction
interface UiResult
interface UiEffect

/**
 *
 * Flow: Intent → Action → (Side Effect) → Result → Reducer(pure) → New State
 *
 * @param S The UI State — an immutable data class.
 * @param I The Intent — user actions from the UI layer.
 * @param A The Action — internal operations (API calls, DB reads).
 * @param R The Result — outcomes of Actions fed into the Reducer.
 * @param E The Effect — one-shot side effects (navigation, toasts).
 */
abstract class Store<S : UiState, I : UiIntent, A : UiAction, R : UiResult, E : UiEffect>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E>()
    val effectFlow: SharedFlow<E> = _effect.asSharedFlow()

    /** Current snapshot of state — use inside executeAction only. */
    protected val currentState: S
        get() = _state.value

    fun sendIntent(intent: I) {
        viewModelScope.launch {
            val action = intentToAction(intent)
            executeAction(action)
        }
    }

    protected abstract fun intentToAction(intent: I): A

    protected abstract suspend fun executeAction(action: A)

    protected abstract fun reduce(state: S, result: R): S

    protected fun dispatch(result: R) {
        _state.value = reduce(_state.value, result)
    }

    protected suspend fun emitEffect(effect: E) {
        _effect.emit(effect)
    }
}

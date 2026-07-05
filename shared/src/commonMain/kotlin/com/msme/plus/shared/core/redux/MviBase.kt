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
 * Each feature screen defines its own sealed classes implementing these.
 */
interface UiState
interface UiIntent
interface UiAction
interface UiResult
interface UiEffect

/**
 * Base MVI Store with a pure Reducer pattern.
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

    /**
     * Entry point from the UI. Non-suspend, safe to call from onClick etc.
     * Launches in viewModelScope so coroutine is lifecycle-aware.
     */
    fun sendIntent(intent: I) {
        viewModelScope.launch {
            val action = intentToAction(intent)
            executeAction(action)
        }
    }

    /**
     * Pure mapping: translates a user Intent into an internal Action.
     * No side effects allowed here.
     */
    protected abstract fun intentToAction(intent: I): A

    /**
     * Executes side effects (API calls, DB ops) for the given Action.
     * Must call [dispatch] with Results to trigger state changes.
     * May call [emitEffect] for one-shot navigation/toast events.
     */
    protected abstract suspend fun executeAction(action: A)

    /**
     * PURE REDUCER — the heart of the pattern.
     * Given the current state and a result, returns a new state.
     * No side effects, no coroutines, no mutations — just pure transformation.
     * This makes state transitions trivially unit-testable.
     */
    protected abstract fun reduce(state: S, result: R): S

    /**
     * Dispatches a Result through the Reducer to produce a new State.
     * Call this from [executeAction] whenever you have a new Result.
     */
    protected fun dispatch(result: R) {
        _state.value = reduce(_state.value, result)
    }

    /** Emits a one-shot Effect (navigation, snackbar, etc.) */
    protected suspend fun emitEffect(effect: E) {
        _effect.emit(effect)
    }
}

package com.msme.plus.shared.features.advisor

fun aiAdvisorReducer(state: AiAdvisorState, result: AiAdvisorResult): AiAdvisorState {
    return when (result) {
        is AiAdvisorResult.Loading -> state.copy(
            isLoading = true,
            error = null
        )
        is AiAdvisorResult.HistorySuccess -> state.copy(
            isLoading = false,
            messages = result.messages,
            error = null
        )
        is AiAdvisorResult.Error -> state.copy(
            isLoading = false,
            isSending = false,
            error = result.message
        )
        is AiAdvisorResult.Sending -> state.copy(
            isSending = true
        )
        is AiAdvisorResult.MessageSent -> state.copy(
            messages = state.messages + result.message
        )
        is AiAdvisorResult.AiResponseReceived -> state.copy(
            isSending = false,
            messages = state.messages + result.message
        )
    }
}

package com.msme.plus.shared.features.advisor

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.advisor.ChatMessage

data class AiAdvisorState(
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
    val error: String? = null
) : UiState

sealed interface AiAdvisorIntent : UiIntent {
    data object LoadHistory : AiAdvisorIntent
    data class SendMessage(val text: String) : AiAdvisorIntent
    data object NavigateBack : AiAdvisorIntent
}

sealed interface AiAdvisorAction : UiAction {
    data object LoadHistory : AiAdvisorAction
    data class SendMessage(val text: String) : AiAdvisorAction
    data object NavigateBack : AiAdvisorAction
}

sealed interface AiAdvisorResult : UiResult {
    data object Loading : AiAdvisorResult
    data class HistorySuccess(val messages: List<ChatMessage>) : AiAdvisorResult
    data class Error(val message: String) : AiAdvisorResult
    data object Sending : AiAdvisorResult
    data class MessageSent(val message: ChatMessage) : AiAdvisorResult
    data class AiResponseReceived(val message: ChatMessage) : AiAdvisorResult
}

sealed interface AiAdvisorEffect : UiEffect {
    data object NavigateBack : AiAdvisorEffect
    data class ShowToast(val message: String) : AiAdvisorEffect
    data object ScrollToBottom : AiAdvisorEffect
}

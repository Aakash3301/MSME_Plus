package com.msme.plus.shared.features.advisor

import androidx.lifecycle.viewModelScope
import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.model.advisor.MessageSender
import com.msme.plus.shared.domain.usecase.advisor.GetChatHistoryUseCase
import com.msme.plus.shared.domain.usecase.advisor.SendMessageUseCase
import kotlinx.coroutines.launch

class AiAdvisorViewModel(
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : Store<AiAdvisorState, AiAdvisorIntent, AiAdvisorAction, AiAdvisorResult, AiAdvisorEffect>(
    initialState = AiAdvisorState()
) {

    override fun intentToAction(intent: AiAdvisorIntent): AiAdvisorAction {
        return when (intent) {
            is AiAdvisorIntent.LoadHistory -> AiAdvisorAction.LoadHistory
            is AiAdvisorIntent.SendMessage -> AiAdvisorAction.SendMessage(intent.text)
            is AiAdvisorIntent.NavigateBack -> AiAdvisorAction.NavigateBack
        }
    }

    override suspend fun executeAction(action: AiAdvisorAction) {
        when (action) {
            is AiAdvisorAction.LoadHistory -> {
                viewModelScope.launch {
                    getChatHistoryUseCase().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> dispatch(AiAdvisorResult.Loading)
                            is Resource.Success -> {
                                dispatch(AiAdvisorResult.HistorySuccess(resource.data))
                                emitEffect(AiAdvisorEffect.ScrollToBottom)
                            }
                            is Resource.Error -> {
                                dispatch(AiAdvisorResult.Error(resource.message ?: resource.exception.message ?: "Failed to load chat history"))
                            }
                        }
                    }
                }
            }
            is AiAdvisorAction.SendMessage -> {
                viewModelScope.launch {
                    dispatch(AiAdvisorResult.Sending)
                    sendMessageUseCase(action.text).collect { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                // No-op, already dispatching Sending
                            }
                            is Resource.Success -> {
                                val message = resource.data
                                if (message.sender == MessageSender.USER) {
                                    dispatch(AiAdvisorResult.MessageSent(message))
                                    emitEffect(AiAdvisorEffect.ScrollToBottom)
                                } else {
                                    dispatch(AiAdvisorResult.AiResponseReceived(message))
                                    emitEffect(AiAdvisorEffect.ScrollToBottom)
                                }
                            }
                            is Resource.Error -> {
                                dispatch(AiAdvisorResult.Error(resource.message ?: resource.exception.message ?: "Failed to send message"))
                            }
                        }
                    }
                }
            }
            is AiAdvisorAction.NavigateBack -> {
                viewModelScope.launch {
                    emitEffect(AiAdvisorEffect.NavigateBack)
                }
            }
        }
    }

    override fun reduce(state: AiAdvisorState, result: AiAdvisorResult): AiAdvisorState {
        return aiAdvisorReducer(state, result)
    }
}

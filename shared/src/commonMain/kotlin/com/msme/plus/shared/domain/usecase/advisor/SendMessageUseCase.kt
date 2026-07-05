package com.msme.plus.shared.domain.usecase.advisor

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.advisor.ChatMessage
import com.msme.plus.shared.domain.repository.advisor.AiAdvisorRepository
import kotlinx.coroutines.flow.Flow

class SendMessageUseCase(
    private val repository: AiAdvisorRepository
) {
    operator fun invoke(message: String): Flow<Resource<ChatMessage>> = repository.sendMessage(message)
}

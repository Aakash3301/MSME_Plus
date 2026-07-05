package com.msme.plus.shared.domain.repository.advisor

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.advisor.ChatMessage
import kotlinx.coroutines.flow.Flow

interface AiAdvisorRepository {
    fun getChatHistory(): Flow<Resource<List<ChatMessage>>>
    fun sendMessage(message: String): Flow<Resource<ChatMessage>>
}

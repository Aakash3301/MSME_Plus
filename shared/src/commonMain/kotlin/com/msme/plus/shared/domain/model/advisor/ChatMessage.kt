package com.msme.plus.shared.domain.model.advisor

enum class MessageSender {
    USER, AI
}

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: MessageSender,
    val timestamp: String
)

package com.msme.plus.shared.data.repository.advisor

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.advisor.ChatMessage
import com.msme.plus.shared.domain.model.advisor.MessageSender
import com.msme.plus.shared.domain.repository.advisor.AiAdvisorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AiAdvisorRepositoryImpl : AiAdvisorRepository {

    private val chatHistory = mutableListOf<ChatMessage>(
        ChatMessage(
            id = "1",
            text = "Hello! I'm your IDBI Business Advisor. How can I help you optimize your financial health today?",
            sender = MessageSender.AI,
            timestamp = "10:30 AM"
        )
    )

    override fun getChatHistory(): Flow<Resource<List<ChatMessage>>> = flow {
        emit(Resource.Loading)
        delay(1000) // Simulation delay
        emit(Resource.Success(chatHistory.toList()))
    }

    override fun sendMessage(message: String): Flow<Resource<ChatMessage>> = flow {
        val userMsg = ChatMessage(
            id = (chatHistory.size + 1).toString(),
            text = message,
            sender = MessageSender.USER,
            timestamp = "Just Now"
        )
        chatHistory.add(userMsg)
        
        emit(Resource.Success(userMsg))
        delay(1500) // Simulating AI thinking delay
        
        val aiReplyText = when {
            message.contains("credit eligibility", ignoreCase = true) -> {
                "To increase your credit eligibility, we recommend: \n1. Maintaining timely GST filings.\n2. Improving your Debt Service Coverage Ratio (DSCR) to > 1.25.\n3. Reducing your average Days Sales Outstanding (DSO) below 30 days."
            }
            message.contains("weakest", ignoreCase = true) -> {
                "Based on your recent financial data, your weakest metric is your **Days Sales Outstanding (DSO)** which currently stands at 42 days (target is 30 days). Improving collections from your debtors will significantly boost your cash flow health score."
            }
            message.contains("working capital", ignoreCase = true) -> {
                "Yes, your current instant loan eligibility is **₹25 Lakh** with 92% confidence. You can apply directly through the 'Loans' section on the Dashboard."
            }
            message.contains("cash flow", ignoreCase = true) -> {
                "You can improve cash flow by:\n1. Re-negotiating payment terms with major suppliers.\n2. Invoicing customers promptly and using digital payment links (UPI/IMPS) to reduce payment delay.\n3. Utilizing short-term invoice discounting."
            }
            else -> {
                "I have analyzed your Alternate Data sources (GST & Bank statement). Your business shows steady cash inflows, but optimizing your cost centers and collection cycles will help maximize your IDBI Financial Health Card score."
            }
        }
        
        val aiMsg = ChatMessage(
            id = (chatHistory.size + 1).toString(),
            text = aiReplyText,
            sender = MessageSender.AI,
            timestamp = "Just Now"
        )
        chatHistory.add(aiMsg)
        
        emit(Resource.Success(aiMsg))
    }
}

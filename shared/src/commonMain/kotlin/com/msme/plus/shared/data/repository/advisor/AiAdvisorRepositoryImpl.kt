package com.msme.plus.shared.data.repository.advisor

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.model.advisor.GeminiCandidateDto
import com.msme.plus.shared.data.model.advisor.GeminiContentDto
import com.msme.plus.shared.data.model.advisor.GeminiPartDto
import com.msme.plus.shared.data.model.advisor.GeminiRequestDto
import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsDto
import com.msme.plus.shared.data.model.analytics.RevenueAnalyticsResponseDto
import com.msme.plus.shared.data.model.health.FinancialHealthDto
import com.msme.plus.shared.data.model.health.FinancialHealthResponseDto
import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.domain.model.advisor.ChatMessage
import com.msme.plus.shared.domain.model.advisor.MessageSender
import com.msme.plus.shared.domain.models.MsmeProfile
import com.msme.plus.shared.domain.repository.advisor.AiAdvisorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class AiAdvisorRepositoryImpl(
    private val apiService: ApiService,
    private val settingsManager: SettingsManager
) : AiAdvisorRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedSystemContext: String? = null

    private var  healthRes : FinancialHealthDto?=null
    private var  analyticsRes: RevenueAnalyticsDto ?= null
    private var msme: MsmeProfile ?= null

    private val chatHistory = mutableListOf<ChatMessage>(
        ChatMessage(
            id = "1",
            text = "Hello! I'm your Ai Business Advisor. I have analyzed your profile. How can I help you optimize your financial health today?",
            sender = MessageSender.AI,
            timestamp = "Just Now"
        )
    )

    override fun getChatHistory(): Flow<Resource<List<ChatMessage>>> = flow {
        emit(Resource.Loading)
        
        // Prefetch context on screen load so it's ready when the user sends a message
        try {
            ensureContextLoaded()
            delay(500)
            emit(Resource.Success(chatHistory.toList()))
        } catch (e: Exception) {
            emit(Resource.Error(e, "Something went wrong. Please try again later due to server error."))
        }
    }

    private suspend fun ensureContextLoaded() {
        if (analyticsRes != null) return
        
        val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("Profile not found")
         msme = json.decodeFromString<MsmeProfile>(msmeJson)
        
         healthRes = apiService.getFinancialHealth(msme!!.id).data
         analyticsRes = apiService.getRevenueAnalytics(msme!!.id).data

    }

    private fun contextBuilder(userQuestion: String): String {
        // 1. Format lists properly for the LLM to read easily
        val risksFormatted = healthRes?.risks?.takeIf { it.isNotEmpty() }
            ?.joinToString("\n- ")?.let { "- $it" } ?: "- None identified currently."

        val insightsFormatted = analyticsRes?.aiInsights?.takeIf { it.isNotEmpty() }
            ?.joinToString("\n- ")?.let { "- $it" } ?: "- No specific insights available."

        // 2. Build the structured prompt using Markdown formatting
        cachedSystemContext = """
        You are an elite, proactive Financial Advisor and Fractional CFO for an MSME named '${msme?.businessName ?: "this business"}'. 
        You know their operations and financial state intimately. Never mention that you are an AI or that you were fed this data via JSON or code.

        ### CURRENT FINANCIAL CONTEXT
        - Overall Health Score: ${healthRes?.overallScore ?: "Unknown"} / 100
        - GST Turnover: ${analyticsRes?.gstTurnover ?: "Unknown"}
        - Total Revenue: ${analyticsRes?.totalRevenue ?: "Unknown"}
        - DSO (Days Sales Outstanding): ${analyticsRes?.dsoDays ?: "Unknown"} days

        ### IDENTIFIED RISKS & WEAKNESSES
        $risksFormatted

        ### STRATEGIC INSIGHTS
        $insightsFormatted

        ### RESPONSE GUIDELINES
        1. Tone: Professional, pragmatic, and highly analytical.
        2. Actionability: Always provide concrete, practical next steps the MSME can take today.
        3. Grounding: Base your advice strictly on the financial metrics provided above. If DSO is high, address cash flow. If health is low, prioritize stabilization.
        4. Formatting: Use Markdown (bolding, bullet points) to make your response highly scannable for a busy business owner. Be concise.

        ### USER QUESTION
        $userQuestion
    """.trimIndent()

        return cachedSystemContext!!
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
        
        try {
            // Ensure context is loaded (it usually is pre-loaded in getChatHistory)
            ensureContextLoaded()
            cachedSystemContext = contextBuilder(message)
            val systemContext = cachedSystemContext ?: "You are a highly intelligent financial advisor for an MSME."

            // Build chat history for Gemini
            val contents = mutableListOf<GeminiContentDto>()
            
            // 1. Add System Prompt
            contents.add(GeminiContentDto(
                role = "user",
                parts = listOf(GeminiPartDto(text = systemContext))
            ))
            contents.add(GeminiContentDto(
                role = "model",
                parts = listOf(GeminiPartDto(text = "Understood. I will act as the financial advisor using this context."))
            ))
            
            // 2. Add previous chat history
            chatHistory.drop(1).forEach { msg ->
                val role = if (msg.sender == MessageSender.USER) "user" else "model"
                contents.add(GeminiContentDto(
                    role = role,
                    parts = listOf(GeminiPartDto(text = msg.text))
                ))
            }
            
            val request = GeminiRequestDto(contents = contents)
            val geminiResponse = apiService.askGemini(request)
            
            val aiReplyText = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "I'm sorry, I couldn't generate a response at this time."

            val aiMsg = ChatMessage(
                id = (chatHistory.size + 1).toString(),
                text = aiReplyText,
                sender = MessageSender.AI,
                timestamp = "Just Now"
            )
            chatHistory.add(aiMsg)
            
            emit(Resource.Success(aiMsg))
        } catch (e: Exception) {
            val errorMsg = ChatMessage(
                id = (chatHistory.size + 1).toString(),
                text = "Error: ${e.message ?: "Failed to connect to AI Advisor"}",
                sender = MessageSender.AI,
                timestamp = "Just Now"
            )
            chatHistory.add(errorMsg)
            emit(Resource.Error(e, errorMsg.text))
        }
    }
}

package com.msme.plus.shared.data.repository.loan

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.network.safeApiCall
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.data.mapper.loan.toDomain
import com.msme.plus.shared.data.model.loan.LoanAssessmentMockData
import com.msme.plus.shared.data.model.loan.LoanAssessmentRequestDto
import com.msme.plus.shared.data.model.loan.LoanAssessmentResponseDto
import com.msme.plus.shared.data.network.ApiService
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.models.MsmeProfile
import com.msme.plus.shared.domain.repository.loan.LoanAssessmentRepository
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class LoanAssessmentRepositoryImpl(
    private val apiService: ApiService,
    private val settingsManager: SettingsManager
) : LoanAssessmentRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getAssessment(
        requestedLoan: String,
        loanPurpose: String,
        businessAge: String,
        wcRequirement: String
    ): Resource<AssessmentResult> {
        return safeApiCall {
            // --- OLD MOCK CALL (kept commented out as requested) ---
            /*
            delay(1500)
            val responseDto = json.decodeFromString(
                LoanAssessmentResponseDto.serializer(),
                LoanAssessmentMockData.LOAN_ASSESSMENT_JSON
            )
            responseDto.data.toDomain()
            */
            
            // --- REAL API CALL ---
            val msmeJson = settingsManager.getMsmeProfile() ?: throw Exception("User profile not found")
            val msme = json.decodeFromString(MsmeProfile.serializer(), msmeJson)
            
            val requestDto = LoanAssessmentRequestDto(
                id = msme.id,
                requestedLoan = requestedLoan,
                loanPurpose = loanPurpose,
                businessAge = businessAge,
                wcRequirement = wcRequirement
            )
            
            val response = apiService.assessLoan(requestDto)
            
            if (response.statusCode != 200) {
                throw Exception(response.message)
            }
            
            response.data.toDomain()
        }
    }
}

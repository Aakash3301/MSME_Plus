package com.msme.plus.shared.core.di

import com.msme.plus.shared.data.repository.AuthRepositoryImpl
import com.msme.plus.shared.domain.repository.AuthRepository
import com.msme.plus.shared.domain.usecase.CheckTokenValidityUseCase
import com.msme.plus.shared.domain.usecase.LoginUseCase
import com.msme.plus.shared.features.splash.SplashViewModel
import com.msme.plus.shared.features.login.LoginViewModel
import com.msme.plus.shared.data.repository.DashboardRepositoryImpl
import com.msme.plus.shared.domain.repository.DashboardRepository
import com.msme.plus.shared.domain.usecase.GetDashboardUseCase
import com.msme.plus.shared.features.dashboard.DashboardViewModel
import com.msme.plus.shared.core.storage.SettingsManager
import com.msme.plus.shared.domain.usecase.LogoutUseCase
import com.msme.plus.shared.data.repository.FinancialHealthRepositoryImpl
import com.msme.plus.shared.domain.repository.FinancialHealthRepository
import com.msme.plus.shared.domain.usecase.GetFinancialHealthUseCase
import com.msme.plus.shared.features.health.FinancialHealthViewModel
import com.msme.plus.shared.data.repository.loan.LoanAssessmentRepositoryImpl
import com.msme.plus.shared.domain.repository.loan.LoanAssessmentRepository
import com.msme.plus.shared.domain.usecase.loan.GetLoanAssessmentUseCase
import com.msme.plus.shared.features.loan.LoanAssessmentViewModel
import com.msme.plus.shared.features.analytics.RevenueAnalyticsViewModel
import com.msme.plus.shared.domain.repository.analytics.RevenueAnalyticsRepository
import com.msme.plus.shared.data.repository.analytics.RevenueAnalyticsRepositoryImpl
import com.msme.plus.shared.domain.usecase.analytics.GetRevenueAnalyticsUseCase
import com.msme.plus.shared.domain.repository.AlternateDataRepository
import com.msme.plus.shared.data.repository.AlternateDataRepositoryImpl
import com.msme.plus.shared.domain.usecase.GetAlternateDataSourcesUseCase
import com.msme.plus.shared.features.alternate_data.AlternateDataViewModel
import com.msme.plus.shared.domain.repository.advisor.AiAdvisorRepository
import com.msme.plus.shared.data.repository.advisor.AiAdvisorRepositoryImpl
import com.msme.plus.shared.domain.usecase.advisor.GetChatHistoryUseCase
import com.msme.plus.shared.domain.usecase.advisor.SendMessageUseCase
import com.msme.plus.shared.features.advisor.AiAdvisorViewModel
import com.msme.plus.shared.domain.repository.recommendations.AiRecommendationsRepository
import com.msme.plus.shared.data.repository.recommendations.AiRecommendationsRepositoryImpl
import com.msme.plus.shared.domain.usecase.recommendations.GetAiRecommendationsUseCase
import com.msme.plus.shared.features.recommendations.AiRecommendationsViewModel
import com.msme.plus.shared.domain.repository.profile.BusinessProfileRepository
import com.msme.plus.shared.data.repository.BusinessProfileRepositoryImpl
import com.msme.plus.shared.domain.usecase.profile.GetBusinessProfileUseCase
import com.msme.plus.shared.features.profile.BusinessProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.msme.plus.shared.data.network.ApiService

val sharedModule = module {
    // Network
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
        }
    }
    single { ApiService(get()) }

    // Singletons for Repositories and Network clients
    single { SettingsManager() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<DashboardRepository> { DashboardRepositoryImpl() }
    single<FinancialHealthRepository> { FinancialHealthRepositoryImpl() }
    single<LoanAssessmentRepository> { LoanAssessmentRepositoryImpl() }
    single<RevenueAnalyticsRepository> { RevenueAnalyticsRepositoryImpl() }
    single<AlternateDataRepository> { AlternateDataRepositoryImpl() }
    single<AiAdvisorRepository> { AiAdvisorRepositoryImpl() }
    single<AiRecommendationsRepository> { AiRecommendationsRepositoryImpl() }
    single<BusinessProfileRepository> { BusinessProfileRepositoryImpl() }

    // Factories for UseCases
    factory { CheckTokenValidityUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetDashboardUseCase(get()) }
    factory { GetFinancialHealthUseCase(get()) }
    factoryOf(::GetLoanAssessmentUseCase)
    factoryOf(::GetRevenueAnalyticsUseCase)
    factoryOf(::GetAlternateDataSourcesUseCase)
    factoryOf(::GetChatHistoryUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::GetAiRecommendationsUseCase)
    factoryOf(::GetBusinessProfileUseCase)

    // ViewModels
    factory { SplashViewModel(get()) }
    factory { LoginViewModel(get()) }
    factory { DashboardViewModel(get(), get()) }
    factory { FinancialHealthViewModel(get()) }
    factoryOf(::LoanAssessmentViewModel)
    factoryOf(::AlternateDataViewModel)
    factoryOf(::AiAdvisorViewModel)
    factoryOf(::AiRecommendationsViewModel)
    factoryOf(::BusinessProfileViewModel)
    factoryOf(::RevenueAnalyticsViewModel)
}

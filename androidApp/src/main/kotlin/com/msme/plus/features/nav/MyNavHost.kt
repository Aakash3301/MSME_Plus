package com.msme.plus.features.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.msme.plus.Route
import org.koin.androidx.compose.koinViewModel


@Composable
fun MyNavHost()
{
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.Splash)
    {
        composable<Route.Splash> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.splash.SplashViewModel>()
            com.msme.plus.features.splash.SplashScreen(
                viewModel = viewModel,
                onNavigateToDashboard = {
                    navController.navigate(Route.Dashboard) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Login> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.login.LoginViewModel>()
            com.msme.plus.features.login.LoginScreen(
                viewModel = viewModel,
                onNavigateToDashboard = {
                    navController.navigate(Route.Dashboard) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Dashboard> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.dashboard.DashboardViewModel>()
            com.msme.plus.features.dashboard.DashboardScreen(
                viewModel = viewModel,
                onNavigateToProfile = { navController.navigate(Route.BusinessProfile) },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToFinancialHealth = { navController.navigate(Route.FinancialHealth) },
                onNavigateToLoanAssessment = { navController.navigate(Route.LoanAssessment) },
                onNavigateToAnalytics = { navController.navigate(Route.RevenueAnalytics) },
                onNavigateToAlternateData = { navController.navigate(Route.AlternateData) },
                onNavigateToAiAdvisor = { navController.navigate(Route.AiAdvisor) },
                onNavigateToAiRecm = { navController.navigate(Route.AiRecommendations) }

            )
        }

        composable<Route.FinancialHealth> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.health.FinancialHealthViewModel>()
            com.msme.plus.features.health.FinancialHealthScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.LoanAssessment> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.loan.LoanAssessmentViewModel>()
            com.msme.plus.features.loan.LoanAssessmentScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiAdvisor = { navController.navigate(Route.AiAdvisor) }
            )
        }

        composable<Route.RevenueAnalytics> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.analytics.RevenueAnalyticsViewModel>()
            com.msme.plus.features.analytics.RevenueAnalyticsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.AlternateData> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.alternate_data.AlternateDataViewModel>()
            com.msme.plus.features.alternate_data.AlternateDataScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.AiAdvisor> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.advisor.AiAdvisorViewModel>()
            com.msme.plus.features.advisor.AiAdvisorScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable<Route.AiRecommendations> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.recommendations.AiRecommendationsViewModel>()
            com.msme.plus.features.recommendations.AiRecommendationsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Route.BusinessProfile> {
            val viewModel = koinViewModel<com.msme.plus.shared.features.profile.BusinessProfileViewModel>()
            com.msme.plus.features.profile.BusinessProfileScreen(
                viewModel = viewModel,
                onNavigateToDashboard = {
                    navController.navigate(Route.Dashboard) {
                        popUpTo(Route.BusinessProfile) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Route.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
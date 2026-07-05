package com.msme.plus.shared.features.dashboard

fun dashboardReducer(state: DashboardState, result: DashboardResult): DashboardState {
    return when (result) {
        is DashboardResult.Loading -> state.copy(
            isLoading = true,
            error = null
        )
        is DashboardResult.Success -> state.copy(
            isLoading = false,
            data = result.data,
            error = null
        )
        is DashboardResult.Error -> state.copy(
            isLoading = false,
            error = result.message
        )
    }
}

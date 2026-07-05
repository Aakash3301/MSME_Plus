package com.msme.plus.shared.features.alternate_data

import com.msme.plus.shared.core.redux.UiAction
import com.msme.plus.shared.core.redux.UiEffect
import com.msme.plus.shared.core.redux.UiIntent
import com.msme.plus.shared.core.redux.UiResult
import com.msme.plus.shared.core.redux.UiState
import com.msme.plus.shared.domain.model.data_source.AlternateDataSource

data class AlternateDataState(
    val isLoading: Boolean = false,
    val dataSources: List<AlternateDataSource>? = null,
    val error: String? = null
) : UiState

sealed interface AlternateDataIntent : UiIntent {
    data object LoadData : AlternateDataIntent
    data object NavigateBack : AlternateDataIntent
    data class SyncSource(val sourceId: String) : AlternateDataIntent
}

sealed interface AlternateDataAction : UiAction {
    data object LoadData : AlternateDataAction
    data object NavigateBack : AlternateDataAction
    data class SyncSource(val sourceId: String) : AlternateDataAction
}

sealed interface AlternateDataResult : UiResult {
    data object Loading : AlternateDataResult
    data class Success(val dataSources: List<AlternateDataSource>) : AlternateDataResult
    data class Error(val message: String) : AlternateDataResult
    data class SyncSuccess(val sourceId: String) : AlternateDataResult
}

sealed interface AlternateDataEffect : UiEffect {
    data object NavigateBack : AlternateDataEffect
    data class ShowToast(val message: String) : AlternateDataEffect
}

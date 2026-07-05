package com.msme.plus.shared.features.alternate_data

import androidx.lifecycle.viewModelScope
import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.core.redux.Store
import com.msme.plus.shared.domain.usecase.GetAlternateDataSourcesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlternateDataViewModel(
    private val getAlternateDataSourcesUseCase: GetAlternateDataSourcesUseCase
) : Store<AlternateDataState, AlternateDataIntent, AlternateDataAction, AlternateDataResult, AlternateDataEffect>(
    initialState = AlternateDataState()
) {
    override fun intentToAction(intent: AlternateDataIntent): AlternateDataAction {
        return when (intent) {
            is AlternateDataIntent.LoadData -> AlternateDataAction.LoadData
            is AlternateDataIntent.NavigateBack -> AlternateDataAction.NavigateBack
            is AlternateDataIntent.SyncSource -> AlternateDataAction.SyncSource(intent.sourceId)
        }
    }

    override suspend fun executeAction(action: AlternateDataAction) {
        when (action) {
            is AlternateDataAction.LoadData -> {
                viewModelScope.launch {
                    getAlternateDataSourcesUseCase().collect { resource ->
                        when (resource) {
                            is Resource.Loading -> dispatch(AlternateDataResult.Loading)
                            is Resource.Success -> dispatch(AlternateDataResult.Success(resource.data))
                            is Resource.Error -> dispatch(AlternateDataResult.Error(resource.message ?: resource.exception.message ?: "Failed to load data sources"))
                        }
                    }
                }
            }
            is AlternateDataAction.NavigateBack -> {
                viewModelScope.launch {
                    emitEffect(AlternateDataEffect.NavigateBack)
                }
            }
            is AlternateDataAction.SyncSource -> {
                viewModelScope.launch {
                    // Simulate sync network call
                    delay(1500)
                    dispatch(AlternateDataResult.SyncSuccess(action.sourceId))
                    emitEffect(AlternateDataEffect.ShowToast("Synced Successfully!"))
                }
            }
        }
    }

    override fun reduce(state: AlternateDataState, result: AlternateDataResult): AlternateDataState {
        return alternateDataReducer(state, result)
    }
}

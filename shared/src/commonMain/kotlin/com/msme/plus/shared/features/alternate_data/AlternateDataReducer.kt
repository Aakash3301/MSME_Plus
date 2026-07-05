package com.msme.plus.shared.features.alternate_data

fun alternateDataReducer(state: AlternateDataState, result: AlternateDataResult): AlternateDataState {
    return when (result) {
        is AlternateDataResult.Loading -> {
            state.copy(isLoading = true, error = null)
        }
        is AlternateDataResult.Success -> {
            state.copy(isLoading = false, dataSources = result.dataSources, error = null)
        }
        is AlternateDataResult.Error -> {
            state.copy(isLoading = false, error = result.message)
        }
        is AlternateDataResult.SyncSuccess -> {
            // Optimistic update for syncing a specific source.
            // For a real app, this would refresh the list or update the specific item.
            val updatedSources = state.dataSources?.map { 
                if (it.id == result.sourceId) it.copy(lastSyncTime = "Just now") else it 
            }
            state.copy(dataSources = updatedSources)
        }
    }
}

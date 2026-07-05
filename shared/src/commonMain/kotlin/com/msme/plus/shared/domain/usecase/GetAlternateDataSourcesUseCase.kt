package com.msme.plus.shared.domain.usecase

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.data_source.AlternateDataSource
import com.msme.plus.shared.domain.repository.AlternateDataRepository
import kotlinx.coroutines.flow.Flow

class GetAlternateDataSourcesUseCase(
    private val repository: AlternateDataRepository
) {
    operator fun invoke(): Flow<Resource<List<AlternateDataSource>>> {
        return repository.getAlternateDataSources()
    }
}

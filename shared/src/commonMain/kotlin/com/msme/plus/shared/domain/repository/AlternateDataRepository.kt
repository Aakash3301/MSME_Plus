package com.msme.plus.shared.domain.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.data_source.AlternateDataSource
import kotlinx.coroutines.flow.Flow

interface AlternateDataRepository {
    fun getAlternateDataSources(): Flow<Resource<List<AlternateDataSource>>>
}

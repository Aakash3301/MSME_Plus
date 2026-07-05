package com.msme.plus.shared.data.repository

import com.msme.plus.shared.core.network.Resource
import com.msme.plus.shared.domain.model.data_source.AlternateDataSource
import com.msme.plus.shared.domain.model.data_source.DataSourceStatus
import com.msme.plus.shared.domain.repository.AlternateDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlternateDataRepositoryImpl : AlternateDataRepository {
    override fun getAlternateDataSources(): Flow<Resource<List<AlternateDataSource>>> = flow<Resource<List<AlternateDataSource>>> {
        emit(Resource.Loading)
        delay(1500) // Simulating network delay for shimmer effect
        emit(Resource.Success(
            listOf(
                AlternateDataSource(
                    id = "1",
                    name = "GST",
                    iconName = "receipt_long",
                    status = DataSourceStatus.CONNECTED,
                    lastSyncTime = "2h ago",
                    dataHealth = "Excellent",
                    dataHealthPercentage = 98,
                    actionText = "Sync"
                ),
                AlternateDataSource(
                    id = "2",
                    name = "UPI",
                    iconName = "payments",
                    status = DataSourceStatus.CONNECTED,
                    lastSyncTime = "10m ago",
                    dataHealth = "Good",
                    dataHealthPercentage = 91,
                    actionText = "Sync"
                ),
                AlternateDataSource(
                    id = "3",
                    name = "Account Aggregator",
                    iconName = "hub",
                    status = DataSourceStatus.CONNECTED,
                    lastSyncTime = "1d ago",
                    dataHealth = "Stable",
                    dataHealthPercentage = 88,
                    actionText = "Sync"
                ),
                AlternateDataSource(
                    id = "4",
                    name = "EPFO",
                    iconName = "assured_workload",
                    status = DataSourceStatus.CONNECTED,
                    lastSyncTime = "3d ago",
                    dataHealth = "Validated",
                    dataHealthPercentage = null,
                    actionText = "Sync"
                ),
                AlternateDataSource(
                    id = "5",
                    name = "Bank Statement",
                    iconName = "account_balance",
                    status = DataSourceStatus.CONNECTED,
                    lastSyncTime = "5h ago",
                    dataHealth = "Comprehensive",
                    dataHealthPercentage = null,
                    actionText = "Sync"
                ),
                AlternateDataSource(
                    id = "6",
                    name = "Utility Bills",
                    iconName = "bolt",
                    status = DataSourceStatus.PENDING,
                    lastSyncTime = "N/A",
                    dataHealth = "Action Required",
                    dataHealthPercentage = null,
                    actionText = "Connect Now"
                )
            )
        ))
    }
}

package com.msme.plus.shared.domain.model.data_source

data class AlternateDataSource(
    val id: String,
    val name: String,
    val iconName: String, // E.g., "receipt_long", "payments", "hub", "assured_workload", "account_balance", "bolt"
    val status: DataSourceStatus,
    val lastSyncTime: String?,
    val dataHealth: String?,
    val dataHealthPercentage: Int?,
    val actionText: String // E.g., "Sync", "Connect Now"
)

enum class DataSourceStatus {
    CONNECTED,
    PENDING
}

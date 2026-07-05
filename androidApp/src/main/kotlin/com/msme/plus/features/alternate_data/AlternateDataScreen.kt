package com.msme.plus.features.alternate_data

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.shared.domain.model.data_source.AlternateDataSource
import com.msme.plus.shared.domain.model.data_source.DataSourceStatus
import com.msme.plus.shared.features.alternate_data.AlternateDataEffect
import com.msme.plus.shared.features.alternate_data.AlternateDataIntent
import com.msme.plus.shared.features.alternate_data.AlternateDataState
import com.msme.plus.shared.features.alternate_data.AlternateDataViewModel
import com.msme.plus.ui.components.shimmerEffect
import com.msme.plus.ui.theme.*

@Composable
fun AlternateDataScreen(
    onNavigateBack: () -> Unit,
    viewModel: AlternateDataViewModel
) {
    val state by viewModel.stateFlow.collectAsState()
    val effectFlow = viewModel.effectFlow

    LaunchedEffect(Unit) {
        viewModel.sendIntent(AlternateDataIntent.LoadData)
    }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is AlternateDataEffect.NavigateBack -> onNavigateBack()
                is AlternateDataEffect.ShowToast -> {
                    // Show toast (simplified for MVP)
                }
            }
        }
    }

    AlternateDataContent(
        state = state,
        onIntent = { viewModel.sendIntent(it) }
    )
}

@Composable
fun AlternateDataContent(
    state: AlternateDataState,
    onIntent: (AlternateDataIntent) -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Data Sources", style = MaterialTheme.typography.titleLarge, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {
                        onIntent(AlternateDataIntent.NavigateBack)
                    }) {
                        Icon(
                            painter = painterResource(id = com.msme.plus.R.drawable.arrow_back_24),
                            contentDescription = "Back",
                            tint = Color.Unspecified)

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IdbiPrimary
                )
            )
        },
        containerColor = IdbiBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (state.isLoading) {
                AlternateDataShimmer()
            } else {
                state.dataSources?.let { dataSources ->
                    AlternateDataView(
                        dataSources = dataSources,
                        onSyncSource = { id -> onIntent(AlternateDataIntent.SyncSource(id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun AlternateDataView(
    dataSources: List<AlternateDataSource>,
    onSyncSource: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Section
        SummarySection()

        // Data Sources Grid/List
        dataSources.forEach { source ->
            DataSourceCard(
                source = source,
                onSyncSource = { onSyncSource(source.id) }
            )
        }

        // Security Information
        SecurityInformation()
    }
}

@Composable
fun AlternateDataShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(16.dp)).shimmerEffect())
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(16.dp)).shimmerEffect())
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(16.dp)).shimmerEffect())
    }
}

@Composable
fun SummarySection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiPrimaryContainer.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .border(1.dp, IdbiPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(IdbiPrimary, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📊", fontSize = 20.sp)
            }
            Column {
                Text("Empower Your Health Score", style = MaterialTheme.typography.titleMedium, color = IdbiPrimary, modifier = Modifier.padding(bottom = 4.dp))
                Text(
                    text = "These alternate data sources power our advanced AI Health Score. Integrating your GST, UPI, and bank statements allows us to provide more competitive loan offers and deeper financial insights.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IdbiOnSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun DataSourceCard(
    source: AlternateDataSource,
    onSyncSource: () -> Unit
) {
    val isPending = source.status == DataSourceStatus.PENDING
    val borderColor = if (isPending) IdbiError else IdbiOutlineVariant
    val borderModifier = Modifier
        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        .then(
            if (isPending) Modifier.padding(start = 4.dp).background(IdbiError, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            else Modifier
        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .then(borderModifier)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(if (isPending) IdbiErrorContainer else IdbiSecondaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(getIconForName(source.iconName), fontSize = 24.sp)
                }
                Column {
                    Text(source.name, style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(if (isPending) "!" else "✓", color = if (isPending) IdbiError else IdbiPrimary, fontWeight = FontWeight.Bold)
                        Text(
                            if (isPending) "Pending" else "Connected",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isPending) IdbiError else IdbiPrimary
                        )
                    }
                }
            }
            
            Button(
                onClick = onSyncSource,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPending) IdbiTertiary else IdbiPrimary,
                    contentColor = if (isPending) IdbiOnTertiary else Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = CircleShape
            ) {
                if (!isPending) {
                    Text("↻", modifier = Modifier.padding(end = 4.dp))
                }
                Text(source.actionText, style = MaterialTheme.typography.labelLarge)
            }
        }

        Divider(modifier = Modifier.padding(vertical = 12.dp), color = IdbiOutlineVariant.copy(alpha = 0.3f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text("LAST SYNC", style = MaterialTheme.typography.labelSmall, color = IdbiOnSurfaceVariant)
                Text(source.lastSyncTime ?: "N/A", style = MaterialTheme.typography.titleMedium, color = if (isPending) IdbiOutline else IdbiOnSurface)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("DATA HEALTH", style = MaterialTheme.typography.labelSmall, color = IdbiOnSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        source.dataHealth ?: "Action Required",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isPending) IdbiError else IdbiPrimary
                    )
                    if (source.dataHealthPercentage != null) {
                        Text(" (${source.dataHealthPercentage}%)", style = MaterialTheme.typography.titleMedium, color = IdbiPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityInformation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🔒", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
        Text(
            "256-bit AES Encryption Secure Data Tunnel",
            style = MaterialTheme.typography.labelMedium,
            color = IdbiOnSurfaceVariant.copy(alpha = 0.6f)
        )
    }
}

private fun getIconForName(name: String): String {
    return when(name) {
        "receipt_long" -> "🧾"
        "payments" -> "💸"
        "hub" -> "🔗"
        "assured_workload" -> "🏛️"
        "account_balance" -> "🏦"
        "bolt" -> "⚡"
        else -> "📄"
    }
}

class AlternateDataPreviewProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<AlternateDataState> {
    override val values = sequenceOf(
        AlternateDataState(
            isLoading = false,
            dataSources = listOf(
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
                    id = "6",
                    name = "Utility Bills",
                    iconName = "bolt",
                    status = DataSourceStatus.PENDING,
                    lastSyncTime = "N/A",
                    dataHealth = "Action Required",
                    dataHealthPercentage = null,
                    actionText = "Connect Now"
                )
            ),
            error = null
        ),
        AlternateDataState(
            isLoading = true,
            dataSources = null,
            error = null
        )
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun AlternateDataScreenPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(AlternateDataPreviewProvider::class) state: AlternateDataState
) {
    MaterialTheme {
        AlternateDataContent(
            state = state,
            onIntent = {}
        )
    }
}

package com.msme.plus.features.analytics

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
import com.msme.plus.shared.domain.model.analytics.RevenueAnalyticsData
import com.msme.plus.shared.features.analytics.RevenueAnalyticsEffect
import com.msme.plus.shared.features.analytics.RevenueAnalyticsIntent
import com.msme.plus.shared.features.analytics.RevenueAnalyticsState
import com.msme.plus.shared.features.analytics.RevenueAnalyticsViewModel
import com.msme.plus.ui.components.shimmerEffect
import com.msme.plus.ui.theme.*
@Composable
fun RevenueAnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: RevenueAnalyticsViewModel
) {
    val state by viewModel.stateFlow.collectAsState()
    val effectFlow = viewModel.effectFlow

    LaunchedEffect(Unit) {
        if (state.data == null && !state.isLoading) {
            viewModel.sendIntent(RevenueAnalyticsIntent.LoadData)
        } else if (state.data == null && state.isLoading) {
            viewModel.sendIntent(RevenueAnalyticsIntent.LoadData) // Send it anyway to kick off loading
        }
        
        effectFlow.collect { effect ->
            when (effect) {
                is RevenueAnalyticsEffect.NavigateBack -> onNavigateBack()
                is RevenueAnalyticsEffect.ShowToast -> {
                    // Show toast (simplified for MVP)
                }
            }
        }
    }

    RevenueAnalyticsContent(
        state = state,
        onIntent = { viewModel.sendIntent(it) }
    )
}

@Composable
fun RevenueAnalyticsContent(
    state: RevenueAnalyticsState,
    onIntent: (RevenueAnalyticsIntent) -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Revenue & Business Analytics", style = MaterialTheme.typography.titleLarge, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = {onIntent(RevenueAnalyticsIntent.NavigateBack)}) {
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
                RevenueAnalyticsShimmer()
            } else {
                state.data?.let { data ->
                    RevenueAnalyticsDataView(
                        data = data,
                        onGenerateReport = { onIntent(RevenueAnalyticsIntent.GenerateReport) }
                    )
                }
            }
        }
    }
}

@Composable
fun RevenueAnalyticsDataView(
    data: RevenueAnalyticsData,
    onGenerateReport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // AI Insight
        AiInsightCard(insights = data.aiInsights)

        // KPIs
        KpiRow(data = data)

        // Bento Grid
        RevenueTrendCard(data = data)
        CashFlowCard(data = data)
        GstCard(data = data)
        DigitalAdoptionCard(data = data)
        CostCentersCard(data = data)
        DsoCard(data = data)

        // CTA
        Button(
            onClick = onGenerateReport,
            colors = ButtonDefaults.buttonColors(containerColor = IdbiPrimary, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Generate Detailed PDF Report", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun RevenueAnalyticsShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f).height(100.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
            Box(modifier = Modifier.weight(1f).height(100.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
        }
        Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)).shimmerEffect())
    }
}

@Composable
fun AiInsightCard(insights: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEEFFF7), RoundedCornerShape(12.dp))
            .border(1.dp, IdbiPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
                Text("🧠", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                Text("AI Intelligence Insight", style = MaterialTheme.typography.titleMedium, color = IdbiPrimary)
            }
            insights.forEach { insight ->
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("✓", color = IdbiPrimary, modifier = Modifier.padding(end = 8.dp))
                    Text(insight, style = MaterialTheme.typography.bodyMedium, color = IdbiOnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun KpiRow(data: RevenueAnalyticsData) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        KpiCard(
            title = "Total Revenue",
            value = data.totalRevenue,
            trend = data.revenueGrowth,
            modifier = Modifier.weight(1f)
        )
        KpiCard(
            title = "Net Cash Flow",
            value = data.netCashFlow,
            modifier = Modifier.weight(1f)
        )
    }
    KpiCard(
        title = "GST Turnover",
        value = data.gstTurnover,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun KpiCard(title: String, value: String, trend: String? = null, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = IdbiSecondary)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.headlineSmall, color = IdbiOnSurface)
            if (trend != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(trend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IdbiPrimary)
            }
        }
    }
}

@Composable
fun RevenueTrendCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text("Revenue Trend (6M)", style = MaterialTheme.typography.titleMedium)
            Text("📈", fontSize = 20.sp)
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().height(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            data.revenueTrend.forEach { point ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                    val heightRatio = point.value / 150f
                    Box(modifier = Modifier.width(4.dp).height((100 * heightRatio).dp).background(IdbiPrimary))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(point.month, fontSize = 10.sp, color = IdbiSecondary)
                }
            }
        }
    }
}

@Composable
fun CashFlowCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text("Inflow vs Outflow", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(IdbiPrimary, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("In", fontSize = 10.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(IdbiSecondary, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Out", fontSize = 10.sp)
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth().height(140.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            data.cashFlows.forEach { flow ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
                        val inHeight = (flow.inflow / 120f) * 100
                        val outHeight = (flow.outflow / 120f) * 100
                        Box(modifier = Modifier.width(12.dp).height(inHeight.dp).background(IdbiPrimary, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                        Box(modifier = Modifier.width(12.dp).height(outHeight.dp).background(IdbiSecondary.copy(alpha = 0.5f), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(flow.month, fontSize = 10.sp, color = IdbiSecondary)
                }
            }
        }
    }
}

@Composable
fun GstCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("GST Taxable Value", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
        
        data.gstTaxableValues.forEach { gst ->
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                    Text(gst.month, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(gst.value, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(IdbiSurfaceVariant, CircleShape)) {
                    Box(modifier = Modifier.fillMaxWidth(gst.percentage).fillMaxHeight().background(IdbiPrimary, CircleShape))
                }
            }
        }
    }
}

@Composable
fun DigitalAdoptionCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Digital Adoption", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .border(10.dp, IdbiSurfaceContainerLow, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Simplistic representation of Pie
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${data.digitalAdoptionPercentage}%", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("UPI", fontSize = 8.sp)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(IdbiPrimary))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("UPI/IMPS", fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(IdbiSurfaceContainerLow))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cash/Cheque", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CostCentersCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Major Cost Centers", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
        
        data.costCenters.forEach { cost ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).background(IdbiSurfaceContainerLow, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📦", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                        Text(cost.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text("${cost.percentage}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(IdbiOutlineVariant, CircleShape)) {
                        Box(modifier = Modifier.fillMaxWidth(cost.percentage / 100f).fillMaxHeight().background(IdbiPrimary, CircleShape))
                    }
                }
            }
        }
    }
}

@Composable
fun DsoCard(data: RevenueAnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
            Text("Days to Pay (DSO)", style = MaterialTheme.typography.titleMedium)
            Box(modifier = Modifier.background(IdbiPrimaryContainer, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text("Target: 30d", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = IdbiOnPrimaryContainer)
            }
        }
        
        Box(modifier = Modifier.fillMaxWidth().height(128.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("${data.dsoDays}", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = IdbiOnSurface)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Days", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = IdbiSecondary, modifier = Modifier.padding(bottom = 6.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("↓", fontSize = 12.sp, color = IdbiPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(data.dsoTrend, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = IdbiPrimary)
                }
            }
        }
    }
}

class RevenueAnalyticsPreviewProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<RevenueAnalyticsState> {
    override val values = sequenceOf(
        RevenueAnalyticsState(
            isLoading = false,
            data = com.msme.plus.shared.data.model.analytics.RevenueAnalyticsMockData.analyticsData,
            error = null
        ),
        RevenueAnalyticsState(
            isLoading = true,
            data = null,
            error = null
        )
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun RevenueAnalyticsScreenPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(RevenueAnalyticsPreviewProvider::class) state: RevenueAnalyticsState
) {
    MaterialTheme {
        RevenueAnalyticsContent(
            state = state,
            onIntent = {}
        )
    }
}

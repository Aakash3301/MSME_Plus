package com.msme.plus.features.dashboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.msme.plus.shared.domain.model.dashboard.DashboardData
import com.msme.plus.shared.domain.model.dashboard.Kpi
import com.msme.plus.shared.domain.model.dashboard.KpiColorType
import com.msme.plus.shared.domain.model.dashboard.QuickAction
import com.msme.plus.shared.domain.model.dashboard.TrendDirection
import com.msme.plus.shared.features.dashboard.DashboardEffect
import com.msme.plus.shared.features.dashboard.DashboardIntent
import com.msme.plus.shared.features.dashboard.DashboardState
import com.msme.plus.shared.features.dashboard.DashboardViewModel
import com.msme.plus.shared.domain.model.dashboard.HealthScore
import com.msme.plus.shared.domain.model.dashboard.LoanEligibility
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.msme.plus.ui.components.BottomNavBar
import com.msme.plus.ui.components.BottomNavTab
import com.msme.plus.ui.theme.*
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import com.msme.plus.shared.features.advisor.AiAdvisorIntent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToFinancialHealth: () -> Unit,
    onNavigateToLoanAssessment: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToAlternateData: () -> Unit,
    onNavigateToAiAdvisor: () -> Unit,
    onNavigateToAiRecm :()-> Unit,
    viewModel: DashboardViewModel
) {
    val state by viewModel.stateFlow.collectAsState()
    var isDashboardDataLoaded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (state.data == null && !state.isLoading) {
            viewModel.sendIntent(DashboardIntent.LoadData)
        }
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is DashboardEffect.ShowToast -> {
                    println("Dashboard Toast: ${effect.message}")
                }
                is DashboardEffect.NavigateToLogin -> {
                    onNavigateToLogin()
                }
                is DashboardEffect.NavigateToFinancialHealth -> onNavigateToFinancialHealth()
                is DashboardEffect.NavigateToLoanAssessment -> onNavigateToLoanAssessment()
                is DashboardEffect.NavigateToAnalytics -> onNavigateToAnalytics()
                is DashboardEffect.NavigateToAlternateData -> onNavigateToAlternateData()
                is DashboardEffect.NavigateToAiAdvisor -> onNavigateToAiAdvisor()
            }
        }
    }

    Scaffold(

        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    if (isDashboardDataLoaded)
                        DashboardTopBar(data = state.data!!, onIntent = viewModel::sendIntent)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IdbiPrimary
                )
            )
        },

        bottomBar = {
            BottomNavBar(
                currentTab = BottomNavTab.DASHBOARD,
                onTabSelected = { tab ->
                    when (tab) {
                        BottomNavTab.PROFILE -> onNavigateToProfile()
                        BottomNavTab.AI_RECM -> onNavigateToAiRecm()
                        else -> {}
                    }
                }
            )
        },
        containerColor  = IdbiBackground
    ) { paddingValues ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = state.isLoading,
            onRefresh = { viewModel.sendIntent(DashboardIntent.LoadData) }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            if (state.isLoading && state.data == null) {
                DashboardShimmer()
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.data != null) {
                isDashboardDataLoaded = true
                DashboardContent(
                    data = state.data!!,
                    onIntent = viewModel::sendIntent
                )
            }

            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = IdbiPrimary
            )
        }
    }
}

@Composable
private fun DashboardContent(
    data: DashboardData,
    onIntent: (DashboardIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
      //  DashboardTopBar(data = data, onIntent = onIntent)


        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Health Score Card
            HealthScoreCard(data = data)
            // Business Toolkit (Quick Actions)
            BusinessToolkit(
                actions = data.quickActions,
                onActionClick = { onIntent(DashboardIntent.QuickActionClicked(it.id)) }
            )
            // KPI Grid
            KpiGrid(kpis = data.kpis)

            // Loan Eligibility
            LoanEligibilityCard(
                data = data,
                onApplyClick = { onIntent(DashboardIntent.ApplyLoanClicked) }
            )


        }
    }
}

@Composable
private fun DashboardTopBar(data: DashboardData, onIntent: (DashboardIntent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiPrimary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column {
                Text(
                    text = data.companyName,
                    color = IdbiOnPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = data.sector.uppercase(),
                    color = IdbiOnPrimary.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box {
                if (data.hasNotifications) {
//                    Box(
//                        modifier = Modifier
//                            .size(10.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFFBA1A1A))
//                            .border(1.dp, IdbiPrimary, CircleShape)
//                            .align(Alignment.TopEnd)
//                    )
                }
            }

            AsyncImage(
                model = data.profileImageUrl,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFF74D8BD), CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun HealthScoreCard(data: DashboardData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF006855), Color(0xFF00836C))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Financial Health Score",
                color = IdbiOnPrimary.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Circular Progress Score
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Track
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.White.copy(alpha = 0.2f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                // Foreground Progress
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val progressAngle = (data.healthScore.score.toFloat() / data.healthScore.maxScore) * 270f
                    drawArc(
                        color = Color.White,
                        startAngle = 135f,
                        sweepAngle = progressAngle,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = data.healthScore.score.toString(),
                            color = IdbiOnPrimary,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "/${data.healthScore.maxScore}",
                            color = IdbiOnPrimary.copy(alpha = 0.7f),
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status Chip
            Row(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "✓", color = IdbiOnPrimary, fontWeight = FontWeight.Bold)
                Text(
                    text = data.healthScore.statusText,
                    color = IdbiOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Tags
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                data.healthScore.tags.forEach { tag ->
                    val bgColor = if (tag == "LOW RISK") IdbiPrimaryContainer else Color(0xFF2D73CF) // Tertiary Container
                    val textColor = if (tag == "LOW RISK") IdbiOnPrimaryContainer else Color(0xFFFCFAFF) // On Tertiary Container
                    
                    Text(
                        text = tag,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(bgColor, CircleShape)
                            .border(1.dp, textColor.copy(alpha = 0.2f), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun KpiGrid(kpis: List<Kpi>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (i in kpis.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiCard(kpi = kpis[i], modifier = Modifier.weight(1f))
                if (i + 1 < kpis.size) {
                    KpiCard(kpi = kpis[i + 1], modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun KpiCard(kpi: Kpi, modifier: Modifier = Modifier) {
    val iconColor = when (kpi.colorType) {
        KpiColorType.PRIMARY -> IdbiPrimary
        KpiColorType.SECONDARY -> Color(0xFF5C5F5F)
        KpiColorType.TERTIARY -> Color(0xFF005AB1)
    }
    
    val trendIcon = when (kpi.trendDirection) {
        TrendDirection.UP -> "↑"
        TrendDirection.DOWN -> "↓"
        TrendDirection.NEUTRAL -> "→"
    }

    Column(
        modifier = modifier
            .background(IdbiSurfaceContainerLow, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFBDC9C4).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = "•", color = iconColor, fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = kpi.title, color = IdbiOnSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Text(text = kpi.value, color = IdbiOnSurface, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = trendIcon, color = IdbiPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = kpi.trend, color = IdbiPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LoanEligibilityCard(data: DashboardData, onApplyClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiPrimary.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
            .border(1.dp, IdbiPrimary.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "🛡", color = IdbiPrimary, fontSize = 18.sp)
                Text(text = data.loanEligibility.status, color = IdbiPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                text = data.loanEligibility.amountText,
                color = IdbiOnSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(96.dp)
                        .background(Color(0xFFBDC9C4), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .fillMaxWidth(data.loanEligibility.confidencePercentage / 100f)
                            .background(IdbiPrimary, CircleShape)
                    )
                }
                Text(
                    text = "Confidence: ${data.loanEligibility.confidencePercentage}%",
                    color = IdbiPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = onApplyClick,
            colors = ButtonDefaults.buttonColors(containerColor = IdbiPrimary),
            shape = CircleShape
        ) {
            Text(text = "Apply Now", color = IdbiOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BusinessToolkit(actions: List<QuickAction>, onActionClick: (QuickAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Business Toolkit", color = IdbiOnSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "View All", color = IdbiPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(actions) { action ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(IdbiSurfaceContainerHigh, RoundedCornerShape(16.dp))
                            .clickable { onActionClick(action) },
                        contentAlignment = Alignment.Center
                    ) {
                        // Use a placeholder emoji based on icon string for now
                        val emoji = when (action.icon) {
                            "health_metrics" -> "🩺"
                            "analytics" -> "📊"
                            "smart_toy" -> "🤖"
                            "account_balance" -> "🏦"
                            "description" -> "📄"
                            "hub" -> "🔗"
                            else -> "⚡"
                        }
                        Text(text = emoji, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = action.title,
                        color = IdbiOnSurfaceVariant,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

class DashboardPreviewProvider : PreviewParameterProvider<DashboardData> {
    override val values = sequenceOf(
        DashboardData(
            companyName = "ABC Manufacturing",
            sector = "Manufacturing Sector",
            hasNotifications = true,
            profileImageUrl = "",
            healthScore = HealthScore(
                score = 87,
                maxScore = 100,
                statusText = "Healthy Business",
                tags = listOf("LOW RISK", "LOAN READY")
            ),
            kpis = listOf(
                Kpi("revenue", "payments", "Monthly Revenue", "₹18.5L", "+12%", TrendDirection.UP, KpiColorType.PRIMARY),
                Kpi("cash_flow", "account_balance_wallet", "Cash Flow", "Excellent", "Stable Profile", TrendDirection.NEUTRAL, KpiColorType.TERTIARY),
                Kpi("gst_compliance", "gavel", "GST Compliance", "98%", "On-time Filing", TrendDirection.NEUTRAL, KpiColorType.SECONDARY),
                Kpi("upi_growth", "rocket_launch", "UPI Growth", "+18%", "Market expansion", TrendDirection.UP, KpiColorType.PRIMARY)
            ),
            loanEligibility = LoanEligibility(
                status = "INSTANT ELIGIBILITY",
                amountText = "₹25 Lakh",
                confidencePercentage = 92
            ),
            quickActions = listOf(
                QuickAction("health_card", "health_metrics", "Financial\nHealth Card"),
                QuickAction("ai_advisor", "smart_toy", "AI\nAdvisor"),
                QuickAction("revenue_analytics", "analytics", "Revenue\nAnalytics"),
                QuickAction("loan_assessment", "account_balance", "Loan\nAssessment"),
                QuickAction("business_documents", "description", "Business\nDocuments")
            )
        )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FD)
@Composable
fun DashboardScreenPreview(
    @PreviewParameter(DashboardPreviewProvider::class) data: DashboardData
) {
    MaterialTheme {
        DashboardContent(
            data = data,
            onIntent = {}
        )
    }
}

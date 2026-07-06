package com.msme.plus.features.health

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.msme.plus.shared.domain.model.health.BadgeType
import com.msme.plus.shared.domain.model.health.FinancialHealthData
import com.msme.plus.shared.domain.model.health.HealthBadge
import com.msme.plus.shared.domain.model.health.LoanOffer
import com.msme.plus.shared.domain.model.health.ScoreBreakdown
import com.msme.plus.shared.features.health.FinancialHealthEffect
import com.msme.plus.shared.features.health.FinancialHealthIntent
import com.msme.plus.shared.features.health.FinancialHealthViewModel
import com.msme.plus.ui.theme.*
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.msme.plus.shared.features.profile.BusinessProfileIntent
import com.msme.plus.ui.components.shimmerEffect


@Composable
fun FinancialHealthScreen(
    viewModel: FinancialHealthViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(FinancialHealthIntent.LoadData)
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is FinancialHealthEffect.ShowToast -> {
                    println("Toast: ${effect.message}")
                }
                is FinancialHealthEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    Scaffold(
        containerColor = IdbiSurface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                FinancialHealthShimmer()
            } else if (state.error != null) {
                Text(
                    text = state.error ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.data != null) {
                FinancialHealthContent(
                    data = state.data!!,
                    onIntent = viewModel::sendIntent
                )
            }
        }
    }
}

@Composable
private fun FinancialHealthContent(
    data: FinancialHealthData,
    onIntent: (FinancialHealthIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(data = data, onIntent = onIntent)

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ScoreVisualization(data = data)
            
            ScoreBreakdownList(breakdowns = data.scoreBreakdowns)
            
            StrengthsAndRisks(
                strengths = data.strengths,
                risks = data.risks,
                onViewMitigationPlan = { onIntent(FinancialHealthIntent.ViewMitigationPlanClicked) }
            )

            data.loanOffer?.let { offer ->
                LoanOfferBanner(
                    offer = offer,
                    onApply = { onIntent(FinancialHealthIntent.ApplyLoanClicked) }
                )
            }
        }
    }
}

@Composable
private fun TopAppBar(data: FinancialHealthData, onIntent: (FinancialHealthIntent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiPrimary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { onIntent(FinancialHealthIntent.BackClicked) }) {
                Icon(
                    painter = painterResource(id = com.msme.plus.R.drawable.arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Unspecified

                )
            }
            Text(
                text = "MSME Financial Health Card",
                color = IdbiOnPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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

@Composable
private fun ScoreVisualization(data: FinancialHealthData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

                // Circular Progress
                Box(
                    modifier = Modifier.size(192.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    val animatedProgress = remember { Animatable(0f) }
                    LaunchedEffect(data.overallScore) {
                        animatedProgress.animateTo(
                            targetValue = data.overallScore.toFloat() / data.maxScore.toFloat(),
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        )
                    }

                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = IdbiSurfaceContainerHigh,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = IdbiPrimaryContainer,
                            startAngle = -90f,
                            sweepAngle = 360f * animatedProgress.value,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${data.overallScore}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IdbiPrimary
                        )
                        Text(
                            text = "/ ${data.maxScore}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = IdbiOnSurfaceVariant
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Overall Health Score",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = IdbiOnSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = data.statusText,
                            fontSize = 14.sp,
                            color = IdbiOnSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        data.badges.forEach { badge ->
                            Badge(badge = badge)
                        }
                    }
                }
            }
        }

}

@Composable
private fun Badge(badge: HealthBadge) {
    val containerColor = when(badge.type) {
        BadgeType.PRIMARY -> IdbiPrimary.copy(alpha = 0.1f)
        BadgeType.TERTIARY -> IdbiTertiaryContainer.copy(alpha = 0.1f)
        BadgeType.SECONDARY -> IdbiSecondary.copy(alpha = 0.1f)
    }
    val contentColor = when(badge.type) {
        BadgeType.PRIMARY -> IdbiPrimary
        BadgeType.TERTIARY -> IdbiTertiaryContainer
        BadgeType.SECONDARY -> IdbiSecondary
    }
    val iconEmoji = when(badge.icon) {
        "verified" -> "✓"
        "trending_up" -> ""
        else -> "•"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(containerColor, CircleShape)
            .border(1.dp, contentColor.copy(alpha = 0.2f), CircleShape)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(text = iconEmoji, color = contentColor, fontSize = 18.sp)
        Text(text = badge.text, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ScoreBreakdownList(breakdowns: List<ScoreBreakdown>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Score Breakdown",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = IdbiOnSurface
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            breakdowns.forEach { breakdown ->
                BreakdownItem(breakdown = breakdown)
            }
        }
    }
}

@Composable
private fun BreakdownItem(breakdown: ScoreBreakdown) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = breakdown.label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = IdbiOnSurfaceVariant
            )
            Text(
                text = "${breakdown.value}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = IdbiPrimary
            )
        }
        
        val animatedProgress = remember { Animatable(0f) }
        LaunchedEffect(breakdown.value) {
            animatedProgress.animateTo(
                targetValue = breakdown.value.toFloat() / 100f,
                animationSpec = tween(1000, easing = FastOutSlowInEasing)
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(IdbiSurfaceContainerHigh, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animatedProgress.value)
                    .background(IdbiPrimaryContainer, CircleShape)
            )
        }
    }
}

@Composable
private fun StrengthsAndRisks(
    strengths: List<String>,
    risks: List<String>,
    onViewMitigationPlan: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Strengths
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(1.dp, IdbiOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "👍", fontSize = 20.sp)
                Text(text = "Strengths", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = IdbiPrimary)
            }
            strengths.forEach { strength ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "✓", color = IdbiPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = strength, fontSize = 14.sp, color = IdbiOnSurfaceVariant)
                }
            }
        }

        // Risks
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(1.dp, IdbiOutlineVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "⚠️", fontSize = 20.sp)
                Text(text = "Risks", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = IdbiError)
            }
            risks.forEach { risk ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "!", color = IdbiError, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = risk, fontSize = 14.sp, color = IdbiOnSurfaceVariant)
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(IdbiError.copy(alpha = 0.1f), CircleShape)
                    .border(1.dp, IdbiError.copy(alpha = 0.2f), CircleShape)
                    .clickable { onViewMitigationPlan() }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "View Mitigation Plan",
                    color = IdbiError,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFFFDF8FD)
@Composable
fun FinancialHealthContentPreview(
    @PreviewParameter(FinancialHealthPreviewProvider::class) data: FinancialHealthData
) {
    androidx.compose.material3.MaterialTheme {
        FinancialHealthContent(
            data = data,
            onIntent = {}
        )
    }
}

@Composable
private fun LoanOfferBanner(
    offer: LoanOffer,
    onApply: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiPrimaryContainer, RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = offer.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = offer.description,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Box(
            modifier = Modifier
                .background(Color.White, CircleShape)
                .clickable { onApply() }
                .padding(horizontal = 32.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = offer.buttonText,
                color = IdbiPrimaryContainer,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun FinancialHealthShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Top app bar equivalent shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        // Overall Score Card Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        // Score Breakdown Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        // Strengths Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        // Risks Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )
        
        // Loan Banner Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )
    }
}

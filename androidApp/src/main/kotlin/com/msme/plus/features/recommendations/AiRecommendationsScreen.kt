package com.msme.plus.features.recommendations

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.shared.domain.model.recommendations.AiRecommendation
import com.msme.plus.shared.domain.model.recommendations.RecommendationPriority
import com.msme.plus.shared.features.recommendations.AiRecommendationsAction
import com.msme.plus.shared.features.recommendations.AiRecommendationsEffect
import com.msme.plus.shared.features.recommendations.AiRecommendationsIntent
import com.msme.plus.shared.features.recommendations.AiRecommendationsState
import com.msme.plus.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun AiRecommendationsScreen(
    viewModel: com.msme.plus.shared.features.recommendations.AiRecommendationsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()
    val effectFlow = viewModel.effectFlow
    val onIntent = viewModel::sendIntent

    AiRecommendationsContent(
        state = state,
        effectFlow = effectFlow,
        onIntent = onIntent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun AiRecommendationsContent(
    state: AiRecommendationsState,
    effectFlow: Flow<AiRecommendationsEffect>,
    onIntent: (AiRecommendationsIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        onIntent(AiRecommendationsIntent.LoadRecommendations)
    }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is AiRecommendationsEffect.NavigateBack -> onNavigateBack()
                is AiRecommendationsEffect.ShowToast -> {
                    // Show toast here if we have a context or snackbar
                }
            }
        }
    }

    Scaffold(
        topBar = {
            RecommendationsTopBar(onNavigateBack = { onIntent(AiRecommendationsIntent.NavigateBack) })
        },
        containerColor = IdbiSurface
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    RecommendationsHeader()
                }

                item {
                    ProgressSummaryBento(potentialGrowth = state.potentialGrowth)
                }

                if (state.isLoading && state.recommendations.isEmpty()) {
                    items(4) {
                        RecommendationCardShimmer()
                    }
                } else {
                    items(state.recommendations) { recommendation ->
                        RecommendationCard(recommendation = recommendation)
                    }

                    item {
                        CallToAction(onDownloadClick = { onIntent(AiRecommendationsIntent.DownloadPlan) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendationsTopBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "IDBI MSME Health",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = IdbiOnPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = com.msme.plus.R.drawable.arrow_back_24),
                    contentDescription = "Back",
                    tint = Color.Unspecified)

            }
        },
        actions = {
            IconButton(onClick = { /* No-op for MVP */ }) {
//                Text(
//                    text = "🔔",
//                    fontSize = 20.sp
//                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = IdbiPrimary,
            titleContentColor = IdbiOnPrimary,
            actionIconContentColor = IdbiOnPrimary
        )
    )
}

@Composable
private fun RecommendationsHeader() {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "AI Recommendations",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = IdbiOnSurface
        )
        Text(
            text = "Actionable steps to optimize your credit health",
            fontSize = 14.sp,
            color = IdbiOnSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ProgressSummaryBento(potentialGrowth: Int) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Box: Potential Growth
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .background(
                    color = IdbiPrimaryContainer.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "POTENTIAL GROWTH",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IdbiPrimary,
                    letterSpacing = 0.5.sp
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "+$potentialGrowth",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiOnSurface
                    )
                    Text(
                        text = " Points",
                        fontSize = 16.sp,
                        color = IdbiOnSurface,
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(top = 16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(IdbiOutlineVariant, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(6.dp)
                            .background(IdbiPrimary, RoundedCornerShape(3.dp))
                    )
                }
                Text(
                    text = "Implement all recommendations to reach 'Excellent' health.",
                    fontSize = 12.sp,
                    color = IdbiOnSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .background(
                    color = IdbiTertiaryContainer.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "ANALYSIS STATUS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiTertiary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Real-time Optimized",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiOnSurface,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    text = "🧠",
                    fontSize = 24.sp
                )
            }
            Text(
                text = "Your business data is being analyzed against 14 risk parameters across the banking ecosystem.",
                fontSize = 14.sp,
                color = IdbiOnSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
private fun RecommendationCard(recommendation: AiRecommendation) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { /* No-op for MVP */ }),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = if (isPressed) 2.dp else 4.dp,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    val iconBgColor = when (recommendation.priority) {
                        RecommendationPriority.HIGH -> IdbiError.copy(alpha = 0.1f)
                        RecommendationPriority.MEDIUM -> IdbiSecondary.copy(alpha = 0.1f)
                        RecommendationPriority.LOW -> IdbiTertiary.copy(alpha = 0.1f)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(iconBgColor, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = recommendation.icon, fontSize = 20.sp)
                    }
                    Text(
                        text = recommendation.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiOnSurface
                    )
                }

                val (pillBgColor, pillTextColor, pillText) = when (recommendation.priority) {
                    RecommendationPriority.HIGH -> Triple<Color, Color, String>(IdbiErrorContainer, IdbiError, "High Priority")
                    RecommendationPriority.MEDIUM -> Triple<Color, Color, String>(IdbiSecondaryContainer, IdbiSecondary, "Medium Priority")
                    RecommendationPriority.LOW -> Triple<Color, Color, String>(IdbiTertiaryContainer, IdbiOnTertiary, "Low Priority")
                }

                Surface(
                    color = pillBgColor,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = pillText,
                        color = pillTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = recommendation.description,
                fontSize = 14.sp,
                color = IdbiOnSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Est. Score Improvement",
                        fontSize = 12.sp,
                        color = IdbiOnSurfaceVariant
                    )
                    Text(
                        text = recommendation.scoreImprovement,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiPrimary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Est. Timeline",
                        fontSize = 12.sp,
                        color = IdbiOnSurfaceVariant
                    )
                    Text(
                        text = recommendation.timeline,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiOnSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationCardShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.8f),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(40.dp).background(brush, RoundedCornerShape(8.dp)))
                    Spacer(modifier = Modifier.height(20.dp).width(150.dp).background(brush))
                }
                Spacer(modifier = Modifier.height(24.dp).width(80.dp).background(brush, RoundedCornerShape(50)))
            }
            Spacer(modifier = Modifier.height(60.dp).fillMaxWidth().padding(vertical = 16.dp).background(brush))
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.3f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.height(14.dp).width(100.dp).background(brush))
                    Spacer(modifier = Modifier.height(20.dp).width(60.dp).padding(top = 4.dp).background(brush))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(modifier = Modifier.height(14.dp).width(80.dp).background(brush))
                    Spacer(modifier = Modifier.height(20.dp).width(60.dp).padding(top = 4.dp).background(brush))
                }
            }
        }
    }
}

@Composable
private fun CallToAction(onDownloadClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onDownloadClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = IdbiPrimary,
                contentColor = IdbiOnPrimary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Download Detailed Plan (PDF)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "Connect with a relationship manager for personalized guidance.",
            fontSize = 14.sp,
            color = IdbiOnSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AiRecommendationsScreenPreview() {
    AiRecommendationsContent(
        state = AiRecommendationsState(
            isLoading = false,
            recommendations = listOf(
                AiRecommendation(
                    id = "1",
                    title = "Improve Cash Reserve by 15%",
                    description = "Increasing liquid reserves provides a buffer against volatility, significantly improving debt serviceability perception.",
                    priority = RecommendationPriority.HIGH,
                    icon = "💰",
                    scoreImprovement = "+12 pts",
                    timeline = "90 Days"
                )
            ),
            potentialGrowth = 42
        ),
        effectFlow = emptyFlow(),
        onIntent = {},
        onNavigateBack = {}
    )
}

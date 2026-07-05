package com.msme.plus.features.loan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.shared.domain.model.loan.AssessmentResult
import com.msme.plus.shared.domain.model.loan.LoanAssessmentData
import com.msme.plus.shared.features.loan.LoanAssessmentEffect
import com.msme.plus.shared.features.loan.LoanAssessmentIntent
import com.msme.plus.shared.features.loan.LoanAssessmentViewModel
import com.msme.plus.ui.theme.*

@Composable
fun LoanAssessmentScreen(
    viewModel: LoanAssessmentViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAiAdvisor: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is LoanAssessmentEffect.NavigateBack -> onNavigateBack()
                is LoanAssessmentEffect.NavigateToAiAdvisor -> onNavigateToAiAdvisor()
            }
        }
    }

    LoanAssessmentContent(
        state = state.data,
        isLoading = state.isLoading,
        onIntent = viewModel::sendIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanAssessmentContent(
    state: LoanAssessmentData,
    isLoading: Boolean,
    onIntent: (LoanAssessmentIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Loan Assessment",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(LoanAssessmentIntent.BackClicked) }) {
                        Text("←", fontSize = 24.sp, color = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle Profile */ }) {
                        Text("👤", fontSize = 24.sp, color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IdbiPrimary,
                )
            )
        },
        containerColor = IdbiBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            InputSection(state, isLoading, onIntent)

            AnimatedVisibility(
                visible = state.assessmentResult != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 20 })
            ) {
                state.assessmentResult?.let { result ->
                    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                        ResultSection(result)
                        AiInsightsSection(result, onIntent)
                    }
                }
            }

            VisualContextSection()
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputSection(
    state: LoanAssessmentData,
    isLoading: Boolean,
    onIntent: (LoanAssessmentIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiSurfaceContainerLow, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiOutlineVariant, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Requested Loan
        Column {
            Text(
                text = "Requested Loan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = IdbiOnSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = state.requestedLoan,
                onValueChange = { onIntent(LoanAssessmentIntent.UpdateRequestedLoan(it)) },
                placeholder = { Text("Enter amount") },
                leadingIcon = { Text("₹", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = IdbiOnSurface) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IdbiPrimary,
                    unfocusedBorderColor = IdbiOutline
                )
            )
        }

        // Loan Purpose
        Column {
            Text(
                text = "Loan Purpose",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = IdbiOnSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            var expanded by remember { mutableStateOf(false) }
            val options = listOf("Working Capital", "Expansion", "Equipment purchase", "Inventory refill")
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.loanPurpose,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IdbiPrimary,
                        unfocusedBorderColor = IdbiOutline
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onIntent(LoanAssessmentIntent.UpdateLoanPurpose(option))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Business Age
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Business Age",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = IdbiOnSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var expanded by remember { mutableStateOf(false) }
                val options = listOf("<1 Year", "1-3 Years", "3-5 Years", "5+ Years")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = state.businessAge,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IdbiPrimary,
                            unfocusedBorderColor = IdbiOutline
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    onIntent(LoanAssessmentIntent.UpdateBusinessAge(option))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // W.C. Requirement
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "W.C. Requirement",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = IdbiOnSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = state.wcRequirement,
                    onValueChange = { onIntent(LoanAssessmentIntent.UpdateWcRequirement(it)) },
                    placeholder = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IdbiPrimary,
                        unfocusedBorderColor = IdbiOutline
                    )
                )
            }
        }

        Button(
            onClick = { onIntent(LoanAssessmentIntent.CheckEligibilityClicked) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            enabled = !isLoading && state.requestedLoan.isNotBlank() && state.wcRequirement.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = IdbiPrimary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Processing...", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            } else {
                Text("Check Eligibility", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun ResultSection(result: AssessmentResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IdbiSurfaceContainerLow, RoundedCornerShape(12.dp))
            .border(1.dp, IdbiPrimaryFixedDim.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .background(IdbiPrimary.copy(alpha = 0.05f))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Eligibility Result",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IdbiOnSurface
                )
                if (result.isEligible) {
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFE0F2F1), RoundedCornerShape(16.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓ Eligible",
                            color = IdbiPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Recommended Loan",
                        fontSize = 14.sp,
                        color = IdbiOnSurfaceVariant
                    )
                    Text(
                        text = result.recommendedLoan,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = IdbiPrimary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Risk: ${result.riskLevel}",
                            modifier = Modifier
                                .background(Color(0xFF90F5D9), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            color = Color(0xFF005142),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(IdbiPrimary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = result.healthStatus,
                                color = IdbiPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { result.confidencePercentage / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = IdbiPrimary,
                            trackColor = IdbiSurfaceVariant,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = "${result.confidencePercentage}%",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            color = IdbiPrimary
                        )
                    }
                    Text(
                        text = "Confidence",
                        fontSize = 12.sp,
                        color = IdbiOnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AiInsightsSection(result: AssessmentResult, onIntent: (LoanAssessmentIntent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✨",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "AI Insights",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IdbiOnSurface
                )
            }
            Text(
                text = "Ask Advisor >",
                color = IdbiPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onIntent(LoanAssessmentIntent.AskAdvisorClicked) }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1ECF2), RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = IdbiOutlineVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { onIntent(LoanAssessmentIntent.AskAdvisorClicked) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = IdbiPrimary,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
                    .width(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = result.aiInsightsText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = IdbiOnSurface
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        result.aiInsightsList.forEach { insight ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("•", color = IdbiPrimary, modifier = Modifier.padding(end = 8.dp))
                                Text(
                                    text = insight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = IdbiOnSurface
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        HorizontalDivider(color = IdbiOutlineVariant, modifier = Modifier.fillMaxWidth())
                        Text(
                            text = "Tap to discuss these insights with AI Advisor",
                            color = IdbiPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VisualContextSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.Gray, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Using a simple gray box with text as a placeholder since we don't have the exact image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6E7A75))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Growing Together",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(IdbiPrimaryContainer, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🛡️",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Secured by IDBI",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Bank Grade Encryption",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDF8FD)
@Composable
fun LoanAssessmentScreenPreview(
    @PreviewParameter(LoanAssessmentPreviewProvider::class) data: LoanAssessmentData
) {
    MaterialTheme {
        LoanAssessmentContent(
            state = data,
            isLoading = false,
            onIntent = {}
        )
    }
}

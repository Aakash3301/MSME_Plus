package com.msme.plus.features.advisor

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.shared.domain.model.advisor.ChatMessage
import com.msme.plus.shared.domain.model.advisor.MessageSender
import com.msme.plus.shared.features.advisor.AiAdvisorEffect
import com.msme.plus.shared.features.advisor.AiAdvisorIntent
import com.msme.plus.shared.features.advisor.AiAdvisorState
import com.msme.plus.shared.features.advisor.AiAdvisorViewModel
import com.msme.plus.ui.components.shimmerEffect
import com.msme.plus.ui.theme.*

@Composable
fun AiAdvisorScreen(
    onNavigateBack: () -> Unit,
    viewModel: AiAdvisorViewModel
) {
    val state by viewModel.stateFlow.collectAsState()
    val effectFlow = viewModel.effectFlow
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.sendIntent(AiAdvisorIntent.LoadHistory)
    }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is AiAdvisorEffect.NavigateBack -> onNavigateBack()
                is AiAdvisorEffect.ScrollToBottom -> {
                    val count = state.messages.size
                    if (count > 0) {
                        listState.animateScrollToItem(count - 1)
                    }
                }
                is AiAdvisorEffect.ShowToast -> {
                    // Toast simulation
                }
            }
        }
    }

    // Scroll to bottom on new messages
    LaunchedEffect(state.messages.size, state.isSending) {
        val count = state.messages.size
        if (count > 0) {
            listState.animateScrollToItem(count - 1)
        }
    }

    AiAdvisorContent(
        state = state,
        listState = listState,
        onIntent = { viewModel.sendIntent(it) }
    )
}

@Composable
fun AiAdvisorContent(
    state: AiAdvisorState,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onIntent: (AiAdvisorIntent) -> Unit
) {
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Column {
                        Text("AI Business Advisor", style = MaterialTheme.typography.titleLarge, color = Color.White)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Pulsing active dot
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val alpha by infiniteTransition.animateFloat(
                                initialValue = 0.3f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "pulse"
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(IdbiPrimaryFixedDim.copy(alpha = alpha))
                            )
                            Text(
                                "Active Now", 
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), 
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(AiAdvisorIntent.NavigateBack) }) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Chat History Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading) {
                    AiAdvisorShimmer()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.messages) { message ->
                            ChatMessageBubble(message = message)
                        }
                        
                        if (state.isSending) {
                            item {
                                TypingIndicatorBubble()
                            }
                        }
                    }
                }
            }

            // Bottom controls: suggestions and input field
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IdbiBackground)
                    .padding(bottom = 16.dp)
            ) {
                // Suggestions
                if (!state.isLoading) {
                    val suggestions = listOf(
                        "How can I increase my credit eligibility?",
                        "Which financial metric is weakest?",
                        "Can I apply for working capital?",
                        "How can I improve cash flow?"
                    )
                    
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(suggestions) { suggestion ->
                            Box(
                                modifier = Modifier
                                    .background(Color.White, CircleShape)
                                    .border(1.dp, IdbiOutlineVariant, CircleShape)
                                    .clickable {
                                        onIntent(AiAdvisorIntent.SendMessage(suggestion))
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = suggestion,
                                    color = IdbiPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Input field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chat Input Bar
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(IdbiSurfaceContainerLow, CircleShape)
                            .border(1.dp, IdbiOutlineVariant, CircleShape)
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .clickable { /* Attach no-op for MVP */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 20.sp, color = IdbiOnSurfaceVariant)
                        }

                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Ask your advisor...", color = IdbiOnSurfaceVariant.copy(alpha = 0.6f), fontSize = 14.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = IdbiOnSurface,
                                unfocusedTextColor = IdbiOnSurface
                            ),
                            maxLines = 3,
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            shape = CircleShape
                        )

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .clickable { /* Mic no-op for MVP */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painter = painterResource(id = com.msme.plus.R.drawable.outline_mic_24),
                            contentDescription = "Back",
                            tint = Color.Unspecified)
                        }
                    }

                    // Send Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(IdbiPrimary)
                            .clickable {
                                if (textInput.isNotBlank()) {
                                    onIntent(AiAdvisorIntent.SendMessage(textInput))
                                    textInput = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("➔", color = Color.White, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(IdbiPrimaryContainer)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🧠", fontSize = 20.sp)
            }
        }
        
        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            val bg = if (isUser) IdbiPrimary else IdbiSurfaceContainerHigh
            val textColor = if (isUser) Color.White else IdbiOnSurface
            val shape = if (isUser) {
                RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
            } else {
                RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
            }
            
            Box(
                modifier = Modifier
                    .background(bg, shape)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
            
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = IdbiOnSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 2.dp, start = if (isUser) 0.dp else 4.dp, end = if (isUser) 4.dp else 0.dp)
            )
        }

        if (isUser) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(IdbiSecondaryContainer)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun TypingIndicatorBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(IdbiPrimaryContainer)
                .border(1.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🧠", fontSize = 20.sp)
        }

        Box(
            modifier = Modifier
                .background(IdbiSurfaceContainerHigh, RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "dots")
                
                val dot1 by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot1"
                )
                val dot2 by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = 150, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot2"
                )
                val dot3 by infiniteTransition.animateFloat(
                    initialValue = 0.2f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = 300, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot3"
                )

                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(IdbiOnSurface.copy(alpha = dot1)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(IdbiOnSurface.copy(alpha = dot2)))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(IdbiOnSurface.copy(alpha = dot3)))
            }
        }
    }
}

@Composable
fun AiAdvisorShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI message shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).shimmerEffect())
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.width(200.dp).height(60.dp).clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)).shimmerEffect())
        }

        // User message shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.width(150.dp).height(44.dp).clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)).shimmerEffect())
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).shimmerEffect())
        }

        // AI message shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).shimmerEffect())
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.width(240.dp).height(80.dp).clip(RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)).shimmerEffect())
        }
    }
}

class AiAdvisorPreviewProvider : androidx.compose.ui.tooling.preview.PreviewParameterProvider<AiAdvisorState> {
    override val values = sequenceOf(
        AiAdvisorState(
            isLoading = false,
            messages = listOf(
                ChatMessage("1", "Hello! How can I help you?", MessageSender.AI, "10:30 AM"),
                ChatMessage("2", "Why is my score 87?", MessageSender.USER, "10:31 AM")
            ),
            isSending = true,
            error = null
        ),
        AiAdvisorState(
            isLoading = true,
            messages = emptyList(),
            error = null
        )
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun AiAdvisorScreenPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(AiAdvisorPreviewProvider::class) state: AiAdvisorState
) {
    MaterialTheme {
        AiAdvisorContent(
            state = state,
            listState = rememberLazyListState(),
            onIntent = {}
        )
    }
}

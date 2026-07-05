package com.msme.plus.features.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.msme.plus.shared.features.splash.SplashEffect
import com.msme.plus.shared.features.splash.SplashIntent
import com.msme.plus.shared.features.splash.SplashViewModel
import com.msme.plus.shared.features.splash.SplashState
import com.msme.plus.ui.theme.*

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is SplashEffect.NavigateToDashboard -> onNavigateToDashboard()
                is SplashEffect.NavigateToLogin -> onNavigateToLogin()
                is SplashEffect.ShowError -> {
                    // In a real app we'd show a snackbar. Here we just navigate to login
                    onNavigateToLogin()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        // Automatically check token after a short visual delay
        kotlinx.coroutines.delay(2000)
        viewModel.sendIntent(SplashIntent.CheckAuthStatus)
    }

    SplashScreenContent(
        state = state,
        onIntent = viewModel::sendIntent
    )
}

@Composable
fun SplashScreenContent(
    state: SplashState,
    onIntent: (SplashIntent) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IdbiBackground)
            .clickable(enabled = !state.isLoading) {
                // Click anywhere to navigate immediately (triggers auth check)
                // store.sendIntent(SplashIntent.CheckAuthStatus) // Commented out to prevent double-calls, handled by timeout or button
            }
    ) {
        // Main Content Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Hero Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Glow effect simulation behind image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(IdbiPrimary.copy(alpha = 0.05f))
                )

                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAL0NKjtST6pVic2jV0tObVX4OTQ3WO3jW1bUS3wi7WzqksQ1WZ6tmqYq_zKrFdawtWMl0Q4tk6TdbX9nsnYBVGXUCMFu6loo7SjmKi1E4XptVZen4cdIz8lVg352w300Jx7WgsWWL6zUDR7G2hyTnoG7RZfHfbsh41_NOGCcjfstEdwVT1zl8FKvoP3fH4J2OgiXRFjyzII2cNaF4aO5n3o_7kTFm5IOk1UgUYGG9i4gZ_Q0Te9aDs",
                    contentDescription = "Hero Illustration",
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(elevation = 16.dp, shape = CircleShape, clip = false),
                    contentScale = ContentScale.Fit
                )
            }

            // Brand Text Block
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "IDBI MSME",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = IdbiOnSurface,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Financial Health Card",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = IdbiPrimary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "AI Powered Business Credit Intelligence",
                fontSize = 14.sp,
                color = IdbiOnSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                letterSpacing = 0.1.sp
            )

            // Continue Button
            Spacer(modifier = Modifier.height(48.dp))
            
            if (!state.isLoading) {
                Button(
                    onClick = { onIntent(SplashIntent.CheckAuthStatus) },
                    colors = ButtonDefaults.buttonColors(containerColor = IdbiPrimary),
                    shape = CircleShape,
                    modifier = Modifier.padding(horizontal = 32.dp).height(48.dp)
                ) {
                    Text("Continue", color = IdbiOnPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = IdbiPrimary,
                        trackColor = IdbiPrimaryFixedDim.copy(alpha = 0.3f),
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Bottom Brand Lockup
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(IdbiPrimaryContainer, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "B", color = IdbiOnPrimaryContainer, fontWeight = FontWeight.Bold) // Fallback icon
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column {
                        Text(
                            text = "POWERED BY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = IdbiOnSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "IDBI BANK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = IdbiPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Security Disclaimer
                Text(
                    text = "Secure Bank Grade Encryption",
                    fontSize = 12.sp,
                    color = IdbiOnSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun SplashScreenPreview() {
    MaterialTheme {
        SplashScreenContent(
            state = SplashState(isLoading = false),
            onIntent = {}
        )
    }
}

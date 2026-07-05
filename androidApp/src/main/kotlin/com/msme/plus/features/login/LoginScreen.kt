package com.msme.plus.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.msme.plus.shared.features.login.LoginViewModel
import com.msme.plus.shared.features.login.LoginState
import com.msme.plus.shared.features.login.LoginIntent
import com.msme.plus.shared.features.login.LoginEffect
import com.msme.plus.ui.theme.*

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effectFlow.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToDashboard -> onNavigateToDashboard()
                is LoginEffect.ShowToast -> {
                    // For now, simple console log or you'd use a SnackbarHostState
                    println("Login Toast: ${effect.message}")
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        onIntent = viewModel::sendIntent
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // "bg-surface-container-lowest"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Header & Illustration Section
            Box(
                modifier = Modifier
                    .size(192.dp)
                    .padding(bottom = 8.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLsuby9HRiQovCOu5OXu8F2HOloN5iAF5-PkYv-83w_goYKpGHVsYofhft-hDwMghzcJVQYCzTKmklhNELrWAv5_3u1kgejac9wsSwvGn9zuTjh1jFv2uZbC_o7DEhwx1Cigst8wFnxJj8YMBssrNRjN1ylfb_vxq7LpjoNBz-WSi333I3B3ZUOQXH5gBzyxa0HLe6r4S7JKFBsYYU_Ln42JK3-rLzbK6iLK02-3w62H2LXD-V9rbJNU0Y8",
                    contentDescription = "Secure biometric authentication",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = IdbiOnSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Secure access to your business credit insights",
                fontSize = 14.sp,
                color = IdbiOnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Form Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IdbiBackground, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFBDC9C4), RoundedCornerShape(16.dp)) // outline-variant
                    .padding(24.dp)
            ) {
                // Business Mobile Number
                Text(
                    text = "Business Mobile Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = IdbiOnSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = state.mobileNumber,
                    onValueChange = { newValue ->
                        if (newValue.length <= 10 && newValue.all { it.isDigit() }) {
                            onIntent(LoginIntent.MobileNumberChanged(newValue))
                        }
                    },
                    placeholder = { Text("Enter mobile number", color = IdbiOnSurfaceVariant.copy(alpha = 0.5f)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "+91",
                                fontSize = 16.sp,
                                color = IdbiOnSurfaceVariant,
                                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(1.dp)
                                    .background(Color(0xFFBDC9C4)) // outline-variant border right
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = IdbiBackground,
                        unfocusedContainerColor = IdbiBackground,
                        focusedBorderColor = IdbiPrimary,
                        unfocusedBorderColor = Color(0xFF6E7A75) // outline
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // GSTIN (Optional)
                Text(
                    text = "GSTIN (Optional)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = IdbiOnSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )

                OutlinedTextField(
                    value = state.gstin,
                    onValueChange = { onIntent(LoginIntent.GstinChanged(it)) },
                    placeholder = { Text("Enter 15-digit GSTIN", color = IdbiOnSurfaceVariant.copy(alpha = 0.5f)) },
                    singleLine = true,
                    leadingIcon = {
                        Text(
                            text = "R", // Fallback for receipt_long icon
                            fontWeight = FontWeight.Bold,
                            color = IdbiOnSurfaceVariant,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = IdbiBackground,
                        unfocusedContainerColor = IdbiBackground,
                        focusedBorderColor = IdbiPrimary,
                        unfocusedBorderColor = Color(0xFF6E7A75) // outline
                    )
                )

                if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = Color(0xFFBA1A1A), // error color
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Action
                Button(
                    onClick = { onIntent(LoginIntent.SubmitClicked) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IdbiPrimary),
                    shape = CircleShape
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = IdbiOnPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Continue", color = IdbiOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Secondary Options
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Login with OTP", color = IdbiPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Forgot PIN/Password?", color = IdbiOnSurfaceVariant, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Trust & Security Badge
            Row(
                modifier = Modifier
                    .background(Color(0x4DDEE0E0), CircleShape) // secondary-container 30% alpha
                    .border(1.dp, Color(0x4DBDC9C4), CircleShape) // outline-variant 30% alpha
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔒", // fallback for lock icon
                    color = IdbiPrimary,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bank-grade 256-bit encryption",
                    color = IdbiOnSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .alpha(0.8f)
            ) {
                Text(
                    text = "Powered by",
                    color = IdbiOnSurfaceVariant,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "IDBI BANK",
                    color = IdbiPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            state = LoginState(
                mobileNumber = "9876543210",
                gstin = "",
                isLoading = false,
                error = null
            ),
            onIntent = {}
        )
    }
}

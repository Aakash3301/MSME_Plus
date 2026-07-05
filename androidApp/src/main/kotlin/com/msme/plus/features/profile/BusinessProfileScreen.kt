package com.msme.plus.features.profile

import android.graphics.drawable.Icon
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.shared.domain.model.profile.BusinessProfileData
import com.msme.plus.shared.features.profile.BusinessProfileAction
import com.msme.plus.shared.features.profile.BusinessProfileEffect
import com.msme.plus.shared.features.profile.BusinessProfileIntent
import com.msme.plus.shared.features.profile.BusinessProfileState
import com.msme.plus.shared.features.profile.BusinessProfileViewModel
import com.msme.plus.ui.components.BottomNavBar
import com.msme.plus.ui.components.BottomNavTab
import com.msme.plus.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

private val MaterialIcons = androidx.compose.ui.text.font.FontFamily.Default

@Composable
fun BusinessProfileScreen(
    viewModel: BusinessProfileViewModel,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.stateFlow.collectAsState()
    val effectFlow = viewModel.effectFlow
    val onIntent = viewModel::sendIntent

    BusinessProfileContent(
        state = state,
        effectFlow = effectFlow,
        onIntent = onIntent,
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToLogin = onNavigateToLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessProfileContent(
    state: BusinessProfileState,
    effectFlow: Flow<BusinessProfileEffect>,
    onIntent: (BusinessProfileIntent) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    LaunchedEffect(Unit) {
        onIntent(BusinessProfileIntent.LoadProfile)
    }

    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is BusinessProfileEffect.NavigateBack -> onNavigateToDashboard()
                is BusinessProfileEffect.NavigateToDashboard -> onNavigateToDashboard()
                is BusinessProfileEffect.NavigateToLogin -> onNavigateToLogin()
                is BusinessProfileEffect.ShowToast -> {
                    println("Profile Toast: ${effect.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "IDBI MSME Financial Health Card",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                    /* Drawer Open (MVP No-op) */ }
                    ) {
                        Icon(
                            painter = painterResource(id = com.msme.plus.R.drawable.arrow_back_24),
                            contentDescription = "Back",
                            tint = Color.Unspecified

                        )
                    }
//                    Image(
//                        contentDescription = "chevr",
//                        modifier = Modifier.size(30.dp).padding(5.dp),
//                        painter = painterResource(com.msme.plus.R.drawable.arrow_back_24)
//                        )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00836C))
                            .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "person",
                            fontFamily = MaterialIcons,
                            color = Color.White,
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = IdbiPrimary
                )
            )
        },
        containerColor = IdbiSurface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading || state.profileData == null) {
                item { BusinessProfileShimmer() }
            } else {
                val data = state.profileData!!

                item {
                    ProfileHeaderSection(data = data)
                }

                item {
                    ProfileDetailsBento(data = data)
                }

                item {
                    ActionableListItems(
                        isNotificationsEnabled = data.isNotificationsEnabled,
                        onIntent = onIntent
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(data: BusinessProfileData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = IdbiSurfaceContainerLow),
        border = androidx.compose.foundation.BorderStroke(1.dp, IdbiOutlineVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(IdbiPrimary.copy(alpha = 0.1f))
            )

            Box(
                modifier = Modifier
                    .offset(y = (-48).dp)
                    .size(96.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, IdbiOutlineVariant.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ABC",
                    fontFamily = MaterialIcons,
                    color = IdbiPrimary,
                    fontSize = 48.sp
                )
            }

            Column(
                modifier = Modifier
                    .offset(y = (-32).dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = data.companyName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IdbiOnSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp),
                    lineHeight = 36.sp
                )
                Text(
                    text = data.companyType,
                    fontSize = 14.sp,
                    color = IdbiOnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = IdbiPrimaryContainer.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, IdbiPrimaryContainer.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "star",
                            fontFamily = MaterialIcons,
                            color = IdbiPrimaryContainer,
                            fontSize = 18.sp
                        )
                        Text(
                            text = data.tier,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = IdbiPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDetailsBento(data: BusinessProfileData) {
    Column (
        modifier = Modifier.fillMaxWidth() ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Registration Details
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, IdbiOutlineVariant.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "info",
                        fontFamily = MaterialIcons,
                        color = IdbiPrimary,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Registration Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IdbiPrimary
                    )
                }

                ProfileField(label = "GSTIN", value = data.gstin)
                ProfileField(label = "PAN Card", value = data.panCard)
                ProfileField(label = "Business Age", value = data.businessAge)
            }
        }

        // Contact Information
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, IdbiOutlineVariant.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "location_on",
                        fontFamily = MaterialIcons,
                        color = IdbiPrimary,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Contact Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = IdbiPrimary
                    )
                }

                ProfileField(label = "Registered Address", value = data.registeredAddress)
                ProfileField(label = "Authorized Director", value = data.directorName, showVerified = true)
                ProfileField(label = "Email", value = data.email)
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String, showVerified: Boolean = false) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = IdbiOnSurfaceVariant,
            letterSpacing = 0.5.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = IdbiOnSurface,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
            if (showVerified) {
                Text(
                    text = "verified",
                    fontFamily = MaterialIcons,
                    color = IdbiPrimary,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ActionableListItems(
    isNotificationsEnabled: Boolean,
    onIntent: (BusinessProfileIntent) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, IdbiOutlineVariant.copy(alpha = 0.2f))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(IdbiSurface)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Account & Preferences",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IdbiOnSurfaceVariant
                )
            }
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionItem(
                icon =  painterResource(id = com.msme.plus.R.drawable.outline_docs_24),
                title = "Documents",
                subtitle = "GST, PAN, Bank Statements",
                onClick = { onIntent(BusinessProfileIntent.PerformAction("Documents")) }
            )
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionItem(
                icon =  painterResource(id = com.msme.plus.R.drawable.outline_settings_24),
                title = "Settings",
                subtitle = "App configuration & preferences",
                onClick = { onIntent(BusinessProfileIntent.PerformAction("Settings")) }
            )
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionToggleItem(
                icon =  painterResource (id = com.msme.plus.R.drawable.outline_notifications_24),
                title = "Notification Preferences",
                subtitle = "Push alerts & SMS updates",
                isChecked = isNotificationsEnabled,
                onCheckedChange = { onIntent(BusinessProfileIntent.ToggleNotifications(it)) }
            )
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionItem(
                icon =  painterResource (id = com.msme.plus.R.drawable.outline_security_24),
                title = "Security",
                subtitle = "Biometrics, PIN change",
                onClick = { onIntent(BusinessProfileIntent.PerformAction("Security")) }
            )
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionItem(
                icon =  painterResource (id = com.msme.plus.R.drawable.outline_settings_24),
                title = "About",
                subtitle = "App v2.4.1, Terms & Conditions",
                onClick = { onIntent(BusinessProfileIntent.PerformAction("About")) }
            )
            HorizontalDivider(color = IdbiOutlineVariant.copy(alpha = 0.1f))

            ActionItem(
                icon =  painterResource (id = com.msme.plus.R.drawable.outline_settings_24),
                title = "Logout",
                subtitle = "Securely sign out of your account",
                iconColor = IdbiError,
                titleColor = IdbiError,
                onClick = { onIntent(BusinessProfileIntent.Logout) }
            )
        }
    }
}

@Composable
fun ActionItem(
    icon: Painter,
    title: String,
    subtitle: String,
    iconColor: Color = IdbiPrimary,
    titleColor: Color = IdbiOnSurface,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = title,
                    tint = Color.Unspecified

                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (iconColor == IdbiError) IdbiError.copy(alpha = 0.6f) else IdbiOnSurfaceVariant
                )
            }

            Image(
                painter = painterResource(com.msme.plus.R.drawable.checvron_right_24),
                contentDescription = "chevr",
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ActionToggleItem(
    icon: Painter,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(IdbiPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = Color.Unspecified

            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = IdbiOnSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = IdbiOnSurfaceVariant
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = IdbiPrimary
            )
        )
    }
}

@Composable
fun BusinessProfileShimmer() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f)
        ),
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(brush)
        )

        // Bento Grid Shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(brush)
            )
        }

        // List Items Shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(brush)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessProfileScreenPreview() {
    MaterialTheme {
        BusinessProfileContent(
            state = BusinessProfileState(
                isLoading = false,
                profileData = BusinessProfileData(
                    companyName = "ABC Manufacturing Pvt Ltd",
                    companyType = "Precision Engineering & Manufacturing",
                    tier = "Gold Tier Merchant",
                    gstin = "29AAAAA0000A1Z5",
                    panCard = "ABCDE1234F",
                    businessAge = "8 Years (Est. 2016)",
                    registeredAddress = "123 Industrial Estate, Phase 2, Bangalore, KA - 560058",
                    directorName = "Rajesh Kumar",
                    email = "rajesh.kumar@abcmanf.in",
                    isNotificationsEnabled = true
                )
            ),
            effectFlow = emptyFlow(),
            onIntent = {},
            onNavigateToDashboard = {},
            onNavigateToLogin = {}
        )
    }
}

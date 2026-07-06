package com.msme.plus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msme.plus.ui.theme.IdbiPrimaryContainer
import com.msme.plus.ui.theme.IdbiOnPrimaryContainer
import com.msme.plus.ui.theme.IdbiOnSurfaceVariant
import com.msme.plus.ui.theme.IdbiSurface
import com.msme.plus.ui.theme.IdbiSurfaceContainerHigh

enum class BottomNavTab {
    DASHBOARD, ANALYTICS, AI, LOANS, PROFILE
}

@Composable
fun BottomNavBar(
    currentTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(IdbiSurface, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            iconRes = "dashboard",
            label = "Dashboard",
            isSelected = currentTab == BottomNavTab.DASHBOARD,
            onClick = { onTabSelected(BottomNavTab.DASHBOARD) }
        )
//        BottomNavItem(
//            iconRes = "analytics",
//            label = "Analytics",
//            isSelected = currentTab == BottomNavTab.ANALYTICS,
//            onClick = { onTabSelected(BottomNavTab.ANALYTICS) }
//        )
        BottomNavItem(
            iconRes = "psychology",
            label = "AI",
            isSelected = currentTab == BottomNavTab.AI,
            onClick = { onTabSelected(BottomNavTab.AI) }
        )
//        BottomNavItem(
//            iconRes = "account_balance_wallet",
//            label = "Loans",
//            isSelected = currentTab == BottomNavTab.LOANS,
//            onClick = { onTabSelected(BottomNavTab.LOANS) }
//        )
        BottomNavItem(
            iconRes = "person",
            label = "Profile",
            isSelected = currentTab == BottomNavTab.PROFILE,
            onClick = { onTabSelected(BottomNavTab.PROFILE) }
        )
    }
}

@Composable
private fun BottomNavItem(
    iconRes: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    val containerColor = if (isSelected) IdbiPrimaryContainer else Color.Transparent
    val contentColor = if (isSelected) IdbiOnPrimaryContainer else IdbiOnSurfaceVariant
    val shape = if (isSelected) CircleShape else RoundedCornerShape(12.dp)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        // Fallback since we don't have all icons, using a simple Text representation for icons in prototype
        Text(
            text = when(iconRes) {
                "dashboard" -> "⊞"
                "analytics" -> "📈"
                "psychology" -> "🤖"
                "account_balance_wallet" -> "💳"
                "person" -> "👤"
                else -> "•"
            },
            fontSize = 24.sp,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

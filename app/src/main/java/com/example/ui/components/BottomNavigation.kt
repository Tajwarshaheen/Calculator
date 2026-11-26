package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.navigation.AppRoutes
import com.example.ui.theme.LocalTajuColors
import com.example.ui.theme.OutfitFontFamily

data class TajuNavItem(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

val tajuBottomNavItems = listOf(
    TajuNavItem(
        route = AppRoutes.CALCULATOR,
        labelRes = R.string.nav_calculator,
        selectedIcon = Icons.Filled.Calculate,
        unselectedIcon = Icons.Outlined.Calculate,
        testTag = "nav_calculator"
    ),
    TajuNavItem(
        route = AppRoutes.SCIENTIFIC,
        labelRes = R.string.nav_scientific,
        selectedIcon = Icons.Filled.Functions,
        unselectedIcon = Icons.Outlined.Functions,
        testTag = "nav_scientific"
    ),
    TajuNavItem(
        route = AppRoutes.HISTORY,
        labelRes = R.string.nav_history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        testTag = "nav_history"
    ),
    TajuNavItem(
        route = AppRoutes.SETTINGS,
        labelRes = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        testTag = "nav_settings"
    )
)

@Composable
fun TajuBottomNavigation(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = taju.glassSurfaceElevated,
        border = BorderStroke(1.dp, taju.glassBorder),
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tajuBottomNavItems.forEach { item ->
                val selected = currentRoute == item.route ||
                    (item.route == AppRoutes.SETTINGS && currentRoute == AppRoutes.ABOUT)
                val label = stringResource(item.labelRes)

                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    modifier = Modifier.testTag(item.testTag),
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = label,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontFamily = OutfitFontFamily,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = taju.goldAccent,
                        selectedTextColor = taju.goldAccent,
                        indicatorColor = taju.goldAccent.copy(alpha = 0.16f),
                        unselectedIconColor = taju.mutedText,
                        unselectedTextColor = taju.mutedText
                    )
                )
            }
        }
    }
}

@Composable
fun TajuNavigationRail(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val taju = LocalTajuColors.current

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(24.dp),
        color = taju.glassSurfaceElevated,
        border = BorderStroke(1.dp, taju.glassBorder)
    ) {
        NavigationRail(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                tajuBottomNavItems.forEach { item ->
                    val selected = currentRoute == item.route ||
                        (item.route == AppRoutes.SETTINGS && currentRoute == AppRoutes.ABOUT)
                    val label = stringResource(item.labelRes)

                    NavigationRailItem(
                        selected = selected,
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier.testTag("${item.testTag}_rail"),
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontFamily = OutfitFontFamily,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationRailItemDefaults.colors(
                            selectedIconColor = taju.goldAccent,
                            selectedTextColor = taju.goldAccent,
                            indicatorColor = taju.goldAccent.copy(alpha = 0.16f),
                            unselectedIconColor = taju.mutedText,
                            unselectedTextColor = taju.mutedText
                        )
                    )
                }
            }
        }
    }
}

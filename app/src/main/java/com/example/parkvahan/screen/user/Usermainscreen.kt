package com.example.parkvahan.screen.user

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

// ═══════════════════════════════════════════════════════════════════════════════
// BOTTOM NAV ITEMS
// ═══════════════════════════════════════════════════════════════════════════════
private enum class UserBottomNav(
    val route       : String,
    val label       : String,
    val activeIcon  : androidx.compose.ui.graphics.vector.ImageVector,
    val inactiveIcon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    HOME(
        route        = "user_home_tab",
        label        = "Home",
        activeIcon   = Icons.Filled.Home,
        inactiveIcon = Icons.Outlined.Home
    ),
    BOOKINGS(
        route        = "user_bookings_tab",
        label        = "Bookings",
        activeIcon   = Icons.Filled.BookOnline,
        inactiveIcon = Icons.Outlined.BookOnline
    ),
}

private object NBC {
    val Primary       = Color(0xFFFFC107)
    val PrimaryDark   = Color(0xFFFF8F00)
    val Surface       = Color(0xFFFFFFFF)
    val Background    = Color(0xFFF8F9FA)
    val TextSecondary = Color(0xFF6B7280)
    val TextTertiary  = Color(0xFFBDBDBD)
    val Border        = Color(0xFFE5E7EB)
    val OnPrimary     = Color(0xFF1A1A1A)
}

// ═══════════════════════════════════════════════════════════════════════════════
// USER MAIN SCREEN — Wrapper with persistent bottom nav
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun UserMainScreen(
    onNavigateToProfile : () -> Unit,
    onLogout            : () -> Unit,
) {
    val innerNavController = rememberNavController()
    val navBackStackEntry  by innerNavController.currentBackStackEntryAsState()
    val currentRoute       = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            UserBottomNavBar(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    innerNavController.navigate(route) {
                        popUpTo(innerNavController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            )
        },
        containerColor = NBC.Background
    ) { paddingValues ->
        NavHost(
            navController    = innerNavController,
            startDestination = UserBottomNav.HOME.route,
            modifier         = Modifier.padding(paddingValues),
            enterTransition  = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(280)) +
                        fadeIn(tween(280))
            },
            exitTransition   = {
                slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(280)) +
                        fadeOut(tween(280))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(280)) +
                        fadeIn(tween(280))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(280)) +
                        fadeOut(tween(280))
            }
        ) {
            composable(UserBottomNav.HOME.route) {
                UserHomeScreen(
                    onNavigateToDetail   = { },
                    onNavigateToSearch   = { },
                    onNavigateToProfile  = onNavigateToProfile,
                    onNavigateToBookings = {
                        innerNavController.navigate(UserBottomNav.BOOKINGS.route) {
                            popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    },
                    onLogout = onLogout,
                )
            }
            composable(UserBottomNav.BOOKINGS.route) {
                UserBookingsScreen(
                    onNavigateBack = {
                        innerNavController.navigate(UserBottomNav.HOME.route) {
                            popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// BOTTOM NAV BAR — persistent, 2 tabs only
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun UserBottomNavBar(
    currentRoute  : String?,
    onTabSelected : (String) -> Unit,
) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color           = NBC.Surface,
        tonalElevation  = 0.dp
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            UserBottomNav.entries.forEach { tab ->
                val isActive = currentRoute == tab.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier            = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab.route) }
                        .padding(vertical = 4.dp)
                ) {
                    // Animated icon container
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier         = Modifier
                            .size(if (isActive) 40.dp else 36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isActive) NBC.Primary.copy(alpha = 0.15f)
                                else Color.Transparent
                            )
                    ) {
                        Icon(
                            imageVector = if (isActive) tab.activeIcon else tab.inactiveIcon,
                            contentDescription = tab.label,
                            tint     = if (isActive) NBC.PrimaryDark else NBC.TextTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        tab.label,
                        fontSize   = 10.sp,
                        color      = if (isActive) NBC.PrimaryDark else NBC.TextTertiary,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                    )
                    // Active dot indicator
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .size(width = if (isActive) 16.dp else 0.dp, height = 3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isActive) NBC.Primary else Color.Transparent)
                    )
                }
            }
        }
    }
}
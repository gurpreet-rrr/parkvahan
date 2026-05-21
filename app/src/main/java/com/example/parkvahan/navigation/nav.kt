package com.example.parkvahan.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.parkvahan.screen.auth.SplashScreen
import com.example.parkvahan.screen.auth.OnboardingScreen
import com.example.parkvahan.screen.auth.LoginScreen
import com.example.parkvahan.screen.auth.RegisterScreen
import com.example.parkvahan.screen.owner.OwnerDashboardScreen

import com.example.parkvahan.screen.user.UserMainScreen
import com.example.parkvahan.screen.user.UserProfileScreen

private fun enterTransition() = slideInHorizontally(
    initialOffsetX = { it }, animationSpec = tween(280)
) + fadeIn(animationSpec = tween(280))

private fun exitTransition() = slideOutHorizontally(
    targetOffsetX = { -it / 3 }, animationSpec = tween(280)
) + fadeOut(animationSpec = tween(280))

private fun popEnterTransition() = slideInHorizontally(
    initialOffsetX = { -it / 3 }, animationSpec = tween(280)
) + fadeIn(animationSpec = tween(280))

private fun popExitTransition() = slideOutHorizontally(
    targetOffsetX = { it }, animationSpec = tween(280)
) + fadeOut(animationSpec = tween(280))

@Composable
fun ParkVahanNavGraph(navController: NavHostController) {

    NavHost(


        navController      = navController,
        startDestination   = NavRoutes.Splash.route,
        enterTransition    = { enterTransition() },
        exitTransition     = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition  = { popExitTransition() },
    ) {

        // ── AUTH ──────────────────────────────────────────────────────────────

        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToOwnerDashboard = {
                    navController.navigate(NavRoutes.OwnerDashboard.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToUserHome = {
                    navController.navigate(NavRoutes.UserHome.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onLoginAsOwner = {
                    navController.navigate(NavRoutes.OwnerDashboard.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onLoginAsUser = {
                    navController.navigate(NavRoutes.UserHome.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.Register.route)
                }
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onRegisteredAsOwner = {
                    navController.navigate(NavRoutes.OwnerDashboard.route) {
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                    }
                },
                onRegisteredAsUser = {
                    navController.navigate(NavRoutes.UserHome.route) {
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── OWNER ─────────────────────────────────────────────────────────────

        composable(NavRoutes.OwnerDashboard.route) {
            OwnerDashboardScreen(
                onNavigateToLive    = { navController.navigate(NavRoutes.OwnerLive.route) },
                onNavigateToSlots   = { navController.navigate(NavRoutes.OwnerSlots.route) },
                onNavigateToRevenue = { navController.navigate(NavRoutes.OwnerRevenue.route) },
                onNavigateToProfile = { navController.navigate(NavRoutes.OwnerProfile.route) },
                onLogout            = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }



        // Stub owner screens — replaced as each is built
        composable(NavRoutes.OwnerSlots.route)    { }
        composable(NavRoutes.OwnerRevenue.route)  { }
        composable(NavRoutes.OwnerBookings.route) { }
        composable(NavRoutes.OwnerPricing.route)  { }
        composable(NavRoutes.OwnerProfile.route)  { }

        // ── USER ──────────────────────────────────────────────────────────────

        composable(NavRoutes.UserHome.route) {
            UserMainScreen(
                onNavigateToProfile = { navController.navigate(NavRoutes.UserProfile.route) },
                onLogout            = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // UserBookings is now inside UserMainScreen inner nav

        composable(NavRoutes.UserProfile.route) {
            UserProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout       = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route     = NavRoutes.ParkingDetail.route,
            arguments = listOf(navArgument("lotId") { type = NavType.StringType })
        ) { }

        composable(
            route     = NavRoutes.Booking.route,
            arguments = listOf(navArgument("lotId") { type = NavType.StringType })
        ) { }

        composable(
            route     = NavRoutes.Payment.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { }

        composable(
            route     = NavRoutes.BookingSuccess.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { }

        // Stub user screens — replaced as each is built
        composable(NavRoutes.UserSearch.route)    { }
        composable(NavRoutes.Notifications.route) { }
    }
}
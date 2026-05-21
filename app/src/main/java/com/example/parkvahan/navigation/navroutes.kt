package com.example.parkvahan.navigation

// ─────────────────────────────────────────────────────────────────────────────
// ALL NAVIGATION ROUTES — single source of truth
// Never use raw strings for navigation anywhere in the app
// ─────────────────────────────────────────────────────────────────────────────
sealed class NavRoutes(val route: String) {

    // ── Auth ──────────────────────────────────────────────────────────────────
    object Splash      : NavRoutes("splash")
    object Onboarding  : NavRoutes("onboarding")
    object Login       : NavRoutes("login")
    object Register    : NavRoutes("register")

    // ── Owner ─────────────────────────────────────────────────────────────────
    object OwnerDashboard : NavRoutes("owner/dashboard")
    object OwnerLive      : NavRoutes("owner/live")
    object OwnerSlots     : NavRoutes("owner/slots")
    object OwnerRevenue   : NavRoutes("owner/revenue")
    object OwnerBookings  : NavRoutes("owner/bookings")
    object OwnerPricing   : NavRoutes("owner/pricing")
    object OwnerProfile   : NavRoutes("owner/profile")

    // ── User ──────────────────────────────────────────────────────────────────
    object UserHome      : NavRoutes("user/home")
    object UserSearch    : NavRoutes("user/search")
    object UserBookings  : NavRoutes("user/mybookings")
    object UserProfile   : NavRoutes("user/profile")
    object Notifications : NavRoutes("notifications")

    object ParkingDetail : NavRoutes("user/parking/{lotId}") {
        fun createRoute(lotId: String) = "user/parking/$lotId"
    }

    object Booking : NavRoutes("user/booking/{lotId}") {
        fun createRoute(lotId: String) = "user/booking/$lotId"
    }

    object Payment : NavRoutes("user/payment/{bookingId}") {
        fun createRoute(bookingId: String) = "user/payment/$bookingId"
    }

    object BookingSuccess : NavRoutes("user/booking/success/{bookingId}") {
        fun createRoute(bookingId: String) = "user/booking/success/$bookingId"
    }
}
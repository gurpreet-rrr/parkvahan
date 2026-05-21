package com.example.parkvahan.screen.user

import androidx.compose.runtime.Composable

// Stub screens — replaced one by one as each is built

@Composable
fun UserSearchScreen(
    onNavigateBack: () -> Unit,
) { }

@Composable
fun ParkingDetailScreen(
    lotId              : String,
    onNavigateBack     : () -> Unit,
    onNavigateToBooking: (String) -> Unit,
) { }

@Composable
fun BookingScreen(
    lotId              : String,
    onNavigateBack     : () -> Unit,
    onNavigateToPayment: (String) -> Unit,
) { }

@Composable
fun PaymentScreen(
    bookingId          : String,
    onNavigateBack     : () -> Unit,
    onNavigateToSuccess: (String) -> Unit,
) { }

@Composable
fun BookingSuccessScreen(
    bookingId     : String,
    onNavigateHome: () -> Unit,
) { }
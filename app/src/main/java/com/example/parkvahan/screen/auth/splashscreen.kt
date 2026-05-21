package com.example.parkvahan.screen.auth
import com.example.parkvahan.ui.theme.ParkVahanColors

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding     : () -> Unit,
    onNavigateToOwnerDashboard : () -> Unit,
    onNavigateToUserHome       : () -> Unit,
) {
    // ── Animation states ──────────────────────────────────────────────────
    val logoScale by animateFloatAsState(
        targetValue   = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.4f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    var textAlpha by remember { mutableStateOf(0f) }
    val animatedTextAlpha by animateFloatAsState(
        targetValue   = textAlpha,
        animationSpec = tween(800),
        label         = "textAlpha"
    )

    var taglineAlpha by remember { mutableStateOf(0f) }
    val animatedTaglineAlpha by animateFloatAsState(
        targetValue   = taglineAlpha,
        animationSpec = tween(800),
        label         = "taglineAlpha"
    )

    // ── Auth check & navigation ───────────────────────────────────────────
    LaunchedEffect(Unit) {
        // Stagger text animations
        delay(300)
        textAlpha = 1f
        delay(400)
        taglineAlpha = 1f
        delay(1200)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // No session — go to onboarding
            onNavigateToOnboarding()
        } else {
            // Session exists — check role from Firebase
            // For now route to user home — role check added when auth is built
            onNavigateToUserHome()
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────
    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(


                        ParkVahanColors.Primary,
                        ParkVahanColors.PrimaryDark,
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Background decorative circles
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = 120.dp, y = (-180).dp)
                .alpha(0.15f)
                .clip(CircleShape)
                .background(ParkVahanColors.OnPrimary)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-100).dp, y = 200.dp)
                .alpha(0.10f)
                .clip(CircleShape)
                .background(ParkVahanColors.OnPrimary)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo ───────────────────────────────────────────────────────
            Box(
                modifier         = Modifier
                    .size(100.dp)
                    .scale(logoScale)
                    .clip(RoundedCornerShape(28.dp))
                    .background(ParkVahanColors.OnPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "P",
                    fontSize   = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ParkVahanColors.OnPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── App name ───────────────────────────────────────────────────
            Text(
                text       = "ParkVahan",
                fontSize   = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = ParkVahanColors.OnPrimary,
                modifier   = Modifier.alpha(animatedTextAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tagline ────────────────────────────────────────────────────
            Text(
                text       = "Smart Parking. Simplified.",
                fontSize   = 16.sp,
                fontWeight = FontWeight.Medium,
                color      = ParkVahanColors.OnPrimary.copy(alpha = 0.85f),
                modifier   = Modifier.alpha(animatedTaglineAlpha)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // ── Loading dots ───────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.alpha(animatedTaglineAlpha)
            ) {
                repeat(3) { index ->
                    val dotAlpha by rememberInfiniteTransition(label = "dot$index").animateFloat(
                        initialValue  = 0.3f,
                        targetValue   = 1f,
                        animationSpec = infiniteRepeatable(
                            animation   = tween(600, delayMillis = index * 200),
                            repeatMode  = RepeatMode.Reverse
                        ),
                        label = "dot${index}alpha"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dotAlpha)
                            .clip(CircleShape)
                            .background(ParkVahanColors.OnPrimary)
                    )
                }
            }
        }

        // ── Bottom version text ────────────────────────────────────────────
        Text(
            text     = "v1.0 · Beta",
            fontSize = 12.sp,
            color    = ParkVahanColors.OnPrimary.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(animatedTaglineAlpha)
        )
    }
}
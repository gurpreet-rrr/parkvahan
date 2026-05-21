package com.example.parkvahan.screen.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkvahan.ui.theme.ParkVahanColors
import kotlinx.coroutines.delay

// ─────────────────────────────────────────────────────────────────────────────
// DATA
// ─────────────────────────────────────────────────────────────────────────────
private data class OnboardingPage(
    val emoji      : String,
    val title      : String,
    val description: String,
    val bgStart    : Color,
    val bgEnd      : Color,
)

private val pages = listOf(
    OnboardingPage(
        emoji       = "🗺️",
        title       = "Find Parking Nearby",
        description = "Discover available parking lots around you in real time. No more circling the block.",
        bgStart     = ParkVahanColors.Primary,
        bgEnd       = ParkVahanColors.PrimaryDark,
    ),
    OnboardingPage(
        emoji       = "📱",
        title       = "Book in Seconds",
        description = "Reserve your slot instantly from your phone. Choose time, vehicle type and pay securely.",
        bgStart     = ParkVahanColors.Info,
        bgEnd       = Color(0xFF0D47A1),
    ),
    OnboardingPage(
        emoji       = "🅿️",
        title       = "Park Smart",
        description = "Get notified when your slot is ready, when time is running out, and when you need to move.",
        bgStart     = ParkVahanColors.Success,
        bgEnd       = Color(0xFF1B5E20),
    ),
)

// ─────────────────────────────────────────────────────────────────────────────
// SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
) {
    var currentPage by remember { mutableStateOf(0) }
    val page = pages[currentPage]

    // Auto advance every 4 seconds
    LaunchedEffect(currentPage) {
        delay(4000)
        if (currentPage < pages.size - 1) {
            currentPage++
        }
    }

    // Animate background color transition
    val animatedBgStart by animateColorAsState(
        targetValue   = page.bgStart,
        animationSpec = tween(600),
        label         = "bgStart"
    )
    val animatedBgEnd by animateColorAsState(
        targetValue   = page.bgEnd,
        animationSpec = tween(600),
        label         = "bgEnd"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(animatedBgStart, animatedBgEnd)
                )
            )
    ) {

        // ── Skip button ────────────────────────────────────────────────────
        Text(
            text       = "Skip",
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color      = ParkVahanColors.OnPrimary.copy(alpha = 0.8f),
            modifier   = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 52.dp, end = 24.dp)
                .clickable { onNavigateToLogin() }
        )

        // ── Main content ───────────────────────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Emoji illustration
            AnimatedContent(
                targetState   = currentPage,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "emoji"
            ) { pageIndex ->
                Box(
                    modifier         = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(ParkVahanColors.OnPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = pages[pageIndex].emoji,
                        fontSize = 72.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Title
            AnimatedContent(
                targetState   = currentPage,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "title"
            ) { pageIndex ->
                Text(
                    text       = pages[pageIndex].title,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = ParkVahanColors.OnPrimary,
                    textAlign  = TextAlign.Center,
                    lineHeight = 34.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            AnimatedContent(
                targetState   = currentPage,
                transitionSpec = {
                    fadeIn(tween(400)) togetherWith fadeOut(tween(400))
                },
                label = "desc"
            ) { pageIndex ->
                Text(
                    text      = pages[pageIndex].description,
                    fontSize  = 16.sp,
                    color     = ParkVahanColors.OnPrimary.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // ── Page indicators ────────────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                pages.forEachIndexed { index, _ ->
                    val isActive = index == currentPage
                    val width by animateDpAsState(
                        targetValue   = if (isActive) 28.dp else 8.dp,
                        animationSpec = tween(300),
                        label         = "dotWidth"
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isActive) ParkVahanColors.OnPrimary
                                else ParkVahanColors.OnPrimary.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Next / Get Started button ──────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ParkVahanColors.OnPrimary.copy(alpha = 0.2f))
                    .clickable {
                        if (currentPage < pages.size - 1) {
                            currentPage++
                        } else {
                            onNavigateToLogin()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = if (currentPage < pages.size - 1) "Next →" else "Get Started",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = ParkVahanColors.OnPrimary
                )
            }
        }

        // ── Bottom brand ───────────────────────────────────────────────────
        Text(
            text     = "ParkVahan",
            fontSize = 13.sp,
            color    = ParkVahanColors.OnPrimary.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}
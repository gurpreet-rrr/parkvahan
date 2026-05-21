package com.example.parkvahan.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────────────────────
// COLOR TOKENS — single source of truth, never use hardcoded colors anywhere
// ─────────────────────────────────────────────────────────────────────────────
object ParkVahanColors {

    // Brand
    val Primary          = Color(0xFFFFB200)
    val PrimaryDark      = Color(0xFFE65100)
    val PrimaryLight     = Color(0xFFFFF8E1)
    val PrimaryContainer = Color(0xFFFFECB3)
    val OnPrimary        = Color(0xFF1A1A1A)

    // Status
    val Success          = Color(0xFF1DBF73)
    val SuccessLight     = Color(0xFFE6F9F0)
    val SuccessDark      = Color(0xFF0A3D24)
    val Error            = Color(0xFFEB5757)
    val ErrorLight       = Color(0xFFFFEEEE)
    val ErrorDark        = Color(0xFF3D0A0A)
    val Warning          = Color(0xFFFFB200)
    val WarningLight     = Color(0xFFFFF8E1)
    val WarningDark      = Color(0xFF3D2E00)
    val Info             = Color(0xFF1A73E8)
    val InfoLight        = Color(0xFFE8F0FE)
    val InfoDark         = Color(0xFF0A1F3D)
    val Purple           = Color(0xFF7C3AED)
    val PurpleLight      = Color(0xFFEDE9FE)
    val PurpleDark       = Color(0xFF2D0F6B)

    // Light mode surfaces
    val BgLight          = Color(0xFFF7F9FC)
    val SurfaceLight     = Color(0xFFFFFFFF)
    val SurfaceVarLight  = Color(0xFFF0F2F5)
    val SurfaceDimLight  = Color(0xFFE8EAED)
    val DividerLight     = Color(0xFFF0F2F5)
    val BorderLight      = Color(0xFFE8EAED)

    // Dark mode surfaces
    val BgDark           = Color(0xFF0F1117)
    val SurfaceDark      = Color(0xFF1A1D27)
    val SurfaceVarDark   = Color(0xFF252836)
    val SurfaceDimDark   = Color(0xFF2E3147)
    val DividerDark      = Color(0xFF252836)
    val BorderDark       = Color(0xFF2E3147)

    // Text
    val TextPrimaryLight   = Color(0xFF1A1A2E)
    val TextSecondaryLight = Color(0xFF6C757D)
    val TextTertiaryLight  = Color(0xFFADB5BD)
    val TextPrimaryDark    = Color(0xFFF0F2F5)
    val TextSecondaryDark  = Color(0xFF9AA0AC)
    val TextTertiaryDark   = Color(0xFF6B7280)
}

// ─────────────────────────────────────────────────────────────────────────────
// THEME DATA — access anywhere with: val t = LocalParkVahanTheme.current
// ─────────────────────────────────────────────────────────────────────────────
data class ParkVahanThemeData(
    val isDark        : Boolean,
    val bg            : Color,
    val surface       : Color,
    val surfaceVar    : Color,
    val surfaceDim    : Color,
    val divider       : Color,
    val border        : Color,
    val textPrimary   : Color,
    val textSecondary : Color,
    val textTertiary  : Color,
    val successBg     : Color,
    val errorBg       : Color,
    val warningBg     : Color,
    val infoBg        : Color,
)

val lightThemeData = ParkVahanThemeData(
    isDark        = false,
    bg            = ParkVahanColors.BgLight,
    surface       = ParkVahanColors.SurfaceLight,
    surfaceVar    = ParkVahanColors.SurfaceVarLight,
    surfaceDim    = ParkVahanColors.SurfaceDimLight,
    divider       = ParkVahanColors.DividerLight,
    border        = ParkVahanColors.BorderLight,
    textPrimary   = ParkVahanColors.TextPrimaryLight,
    textSecondary = ParkVahanColors.TextSecondaryLight,
    textTertiary  = ParkVahanColors.TextTertiaryLight,
    successBg     = ParkVahanColors.SuccessLight,
    errorBg       = ParkVahanColors.ErrorLight,
    warningBg     = ParkVahanColors.WarningLight,
    infoBg        = ParkVahanColors.InfoLight,
)

val darkThemeData = ParkVahanThemeData(
    isDark        = true,
    bg            = ParkVahanColors.BgDark,
    surface       = ParkVahanColors.SurfaceDark,
    surfaceVar    = ParkVahanColors.SurfaceVarDark,
    surfaceDim    = ParkVahanColors.SurfaceDimDark,
    divider       = ParkVahanColors.DividerDark,
    border        = ParkVahanColors.BorderDark,
    textPrimary   = ParkVahanColors.TextPrimaryDark,
    textSecondary = ParkVahanColors.TextSecondaryDark,
    textTertiary  = ParkVahanColors.TextTertiaryDark,
    successBg     = ParkVahanColors.SuccessDark,
    errorBg       = ParkVahanColors.ErrorDark,
    warningBg     = ParkVahanColors.WarningDark,
    infoBg        = ParkVahanColors.InfoDark,
)

val LocalParkVahanTheme = staticCompositionLocalOf { lightThemeData }

// ─────────────────────────────────────────────────────────────────────────────
// TYPOGRAPHY
// ─────────────────────────────────────────────────────────────────────────────
val ParkVahanTypography = Typography(
    displayLarge   = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp),
    displayMedium  = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 28.sp, lineHeight = 34.sp),
    headlineLarge  = TextStyle(fontWeight = FontWeight.Bold,      fontSize = 24.sp, lineHeight = 30.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge     = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium    = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall     = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge      = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium     = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall      = TextStyle(fontWeight = FontWeight.Normal,    fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge     = TextStyle(fontWeight = FontWeight.SemiBold,  fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium    = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall     = TextStyle(fontWeight = FontWeight.Medium,    fontSize = 11.sp, lineHeight = 16.sp),
)

// ─────────────────────────────────────────────────────────────────────────────
// MATERIAL3 COLOR SCHEMES
// ─────────────────────────────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = ParkVahanColors.Primary,
    onPrimary        = ParkVahanColors.OnPrimary,
    primaryContainer = ParkVahanColors.PrimaryContainer,
    secondary        = ParkVahanColors.PrimaryDark,
    background       = ParkVahanColors.BgLight,
    surface          = ParkVahanColors.SurfaceLight,
    onBackground     = ParkVahanColors.TextPrimaryLight,
    onSurface        = ParkVahanColors.TextPrimaryLight,
    error            = ParkVahanColors.Error,
    outline          = ParkVahanColors.BorderLight,
)

private val DarkColorScheme = darkColorScheme(
    primary          = ParkVahanColors.Primary,
    onPrimary        = ParkVahanColors.OnPrimary,
    primaryContainer = ParkVahanColors.PrimaryContainer,
    secondary        = ParkVahanColors.PrimaryDark,
    background       = ParkVahanColors.BgDark,
    surface          = ParkVahanColors.SurfaceDark,
    onBackground     = ParkVahanColors.TextPrimaryDark,
    onSurface        = ParkVahanColors.TextPrimaryDark,
    error            = ParkVahanColors.Error,
    outline          = ParkVahanColors.BorderDark,
)

// ─────────────────────────────────────────────────────────────────────────────
// MAIN THEME COMPOSABLE
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ParkVahanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content  : @Composable () -> Unit
) {
    val themeData = if (darkTheme) darkThemeData else lightThemeData

    CompositionLocalProvider(LocalParkVahanTheme provides themeData) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography  = ParkVahanTypography,
            content     = content
        )
    }
}
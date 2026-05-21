package com.example.parkvahan.screen.owner

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

// ═══════════════════════════════════════════════════════════════════════════════
// DESIGN TOKENS
// ═══════════════════════════════════════════════════════════════════════════════
 object UPV {
    val Primary          = Color(0xFFFFB200)
    val PrimaryDark      = Color(0xFFB45309)
    val PrimaryLight     = Color(0xFFFFF8E1)
    val PrimaryContainer = Color(0xFFFFECB3)
    val OnPrimary        = Color(0xFF1A1A1A)

    // Backgrounds
    val BgDark           = Color(0xFF080C14)
    val SurfaceDark      = Color(0xFF111827)
    val SurfaceVarDark   = Color(0xFF1C2333)
    val SurfaceDimDark   = Color(0xFF252836)
    val BgLight          = Color(0xFFF7F9FC)
    val SurfaceLight     = Color(0xFFFFFFFF)
    val SurfaceVarLight  = Color(0xFFF0F2F5)
    val SurfaceDimLight  = Color(0xFFE8EAED)

    // Text
    val TextPrimDark     = Color(0xFFF9FAFB)
    val TextSecDark      = Color(0xFF9CA3AF)
    val TextTertDark     = Color(0xFF4B5563)
    val TextPrimLight    = Color(0xFF1A1A2E)
    val TextSecLight     = Color(0xFF6C757D)
    val TextTertLight    = Color(0xFFADB5BD)

    // Semantic
    val Success          = Color(0xFF10B981)
    val SuccessLight     = Color(0xFFD1FAE5)
    val SuccessDark      = Color(0xFF064E3B)
    val Error            = Color(0xFFF43F5E)
    val ErrorLight       = Color(0xFFFFE4E6)
    val ErrorDark        = Color(0xFF4C0519)
    val Warning          = Color(0xFFF59E0B)
    val WarningLight     = Color(0xFFFEF3C7)
    val WarningDark      = Color(0xFF451A03)
    val Info             = Color(0xFF38BDF8)
    val InfoLight        = Color(0xFFE0F2FE)
    val InfoDark         = Color(0xFF082F49)
    val Purple           = Color(0xFF8B5CF6)
    val PurpleLight      = Color(0xFFEDE9FE)
    val PurpleDark       = Color(0xFF2E1065)

    // Charts
    val ChartAmber       = Color(0xFFFFB200)
    val ChartGreen       = Color(0xFF10B981)
    val ChartRed         = Color(0xFFF43F5E)
    val ChartBlue        = Color(0xFF38BDF8)
    val ChartPurple      = Color(0xFF8B5CF6)

    // Borders / Dividers
    val DividerDark      = Color(0x0FFFFFFF)
    val BorderDark       = Color(0x1AFFFFFF)
    val DividerLight     = Color(0xFFF0F2F5)
    val BorderLight      = Color(0xFFE8EAED)
}

// ═══════════════════════════════════════════════════════════════════════════════
// THEME
// ═══════════════════════════════════════════════════════════════════════════════
 data class AppTheme(
    val isDark      : Boolean,
    val bg          : Color,
    val surface     : Color,
    val surfaceVar  : Color,
    val surfaceDim  : Color,
    val textPrim    : Color,
    val textSec     : Color,
    val textTert    : Color,
    val divider     : Color,
    val border      : Color,
    val successBg   : Color,
    val errorBg     : Color,
    val warningBg   : Color,
    val infoBg      : Color,
    val purpleBg    : Color,
)

 fun darkTheme() = AppTheme(
    isDark = true,
    bg = UPV.BgDark, surface = UPV.SurfaceDark,
    surfaceVar = UPV.SurfaceVarDark, surfaceDim = UPV.SurfaceDimDark,
    textPrim = UPV.TextPrimDark, textSec = UPV.TextSecDark, textTert = UPV.TextTertDark,
    divider = UPV.DividerDark, border = UPV.BorderDark,
    successBg = UPV.SuccessDark, errorBg = UPV.ErrorDark,
    warningBg = UPV.WarningDark, infoBg = UPV.InfoDark, purpleBg = UPV.PurpleDark,
)

 fun lightTheme() = AppTheme(
    isDark = false,
    bg = UPV.BgLight, surface = UPV.SurfaceLight,
    surfaceVar = UPV.SurfaceVarLight, surfaceDim = UPV.SurfaceDimLight,
    textPrim = UPV.TextPrimLight, textSec = UPV.TextSecLight, textTert = UPV.TextTertLight,
    divider = UPV.DividerLight, border = UPV.BorderLight,
    successBg = UPV.SuccessLight, errorBg = UPV.ErrorLight,
    warningBg = UPV.WarningLight, infoBg = UPV.InfoLight, purpleBg = UPV.PurpleLight,
)
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
import androidx.compose.foundation.layout.Row


// ═══════════════════════════════════════════════════════════════════════════════
// MAIN SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(
    onNavigateToLive: () -> Unit,
    onNavigateToSlots: () -> Unit,
    onNavigateToRevenue: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit




) {
    var isDark      by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(0) }
    val t           = if (isDark) darkTheme() else lightTheme()
    val tabs        = listOf("Overview","Slots","Bookings","Revenue","Lot Analysis","Insights","Alerts","Pricing")

    Surface(modifier = Modifier.fillMaxSize(), color = t.bg) {
        Scaffold(
            topBar         = { DashTopBar(t = t, isDark = isDark, onToggleDark = { isDark = !isDark }) },
            containerColor = t.bg,
        ) { pad ->
            Column(modifier = Modifier.fillMaxSize().padding(pad)) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor   = t.surface,
                    contentColor     = UPV.Primary,
                    edgePadding      = 12.dp,
                    indicator        = { tp ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tp[selectedTab]),
                            color    = UPV.Primary,
                            height   = 3.dp,
                        )
                    },
                    divider = { HorizontalDivider(color = t.divider) }
                ) {
                    tabs.forEachIndexed { i, title ->
                        Tab(
                            selected = selectedTab == i,
                            onClick  = { selectedTab = i },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        title,
                                        fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                                        fontSize   = 13.sp,
                                        color      = if (selectedTab == i) UPV.Primary else t.textSec,
                                    )
                                    if (title == "Alerts") {
                                        Spacer(Modifier.width(4.dp))
                                        Box(
                                            modifier         = Modifier.size(16.dp).background(UPV.Error, CircleShape),
                                            contentAlignment = Alignment.Center,
                                        ) { Text("${alerts.size}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                                    }
                                }
                            }
                        )
                    }
                }
                when (selectedTab) {
                    0 -> OverviewTab(t)
                    1 -> SlotsTab(t)
                    2 -> BookingsTab(t)
                    3 -> RevenueTab(t)
                    4 -> LotAnalysisTab(t)
                    5 -> InsightsTab(t)
                    6 -> AlertsTab(t)
                    7 -> PricingTab(t)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TOP BAR
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
       fun DashTopBar(t: AppTheme, isDark: Boolean, onToggleDark: () -> Unit) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier.size(36.dp).background(
                        Brush.linearGradient(listOf(UPV.Primary, UPV.PrimaryDark)), CircleShape
                    ),
                    contentAlignment = Alignment.Center,
                ) { Text("P", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.Black) }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("ParkVahan", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = t.textPrim)
                    Text("Owner Dashboard", fontSize = 10.sp, color = t.textSec)
                }
            }
        },
        actions = {
            // Live badge
            val pulse by rememberInfiniteTransition(label = "live").animateFloat(
                initialValue = 1f,
                targetValue = 0.3f,
                animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                label = "p"
            )   // ✅ THIS WAS MISSING

            Row(
                modifier = Modifier
                    .background(UPV.Success.copy(if (isDark) 0.12f else 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(7.dp)
                        .background(UPV.Success.copy(alpha = pulse), CircleShape)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    "Live",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = UPV.Success,
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://nonenvironmental-osmotically-petronila.ngrok-free.dev/video"))
                        )
                    }
                )
            }
            Spacer(Modifier.width(6.dp))
            IconButton(onClick = onToggleDark) {
                Icon(
                    if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = "Theme", tint = t.textPrim
                )
            }
            IconButton(onClick = {}) {
                BadgedBox(badge = { Badge(containerColor = UPV.Error) { Text("${alerts.size}", fontSize = 9.sp) } }) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = t.textPrim)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = t.surface),
    )
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 1 — OVERVIEW
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun OverviewTab(t: AppTheme) {
    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { RevenueHeroBanner(t) }
        item { StatsGrid(t) }
        item { OccupancyDonutCard(t) }
        item { CarFlowCard(t) }
        item { SlotHeatmapCard(t) }
    }
}

// ── Revenue Hero Banner ───────────────────────────────────────────────────────
@Composable
       fun RevenueHeroBanner(t: AppTheme) {
    val animRevenue by animateFloatAsState(
        targetValue   = stats.todayRevenue.toFloat(),
        animationSpec = tween(1600, easing = EaseOutCubic), label = "rev"
    )
    val trend = stats.occupancyRate - stats.yesterdayRate

    UPVCard(
        t          = t,
        borderColor = UPV.Primary.copy(0.4f),
        modifier    = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(
                            UPV.Primary.copy(0.18f),
                            UPV.PrimaryDark.copy(0.06f),
                            Color.Transparent,
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Live earnings label
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val pulse2 by rememberInfiniteTransition(label = "lp").animateFloat(
                        1f, 0.3f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "lp2"
                    )
                    Box(Modifier.size(7.dp).background(UPV.Success.copy(alpha = pulse2), CircleShape))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "LIVE EARNINGS",
                        fontSize     = 11.sp, fontWeight = FontWeight.ExtraBold,
                        color        = UPV.Success, letterSpacing = 1.5.sp,
                    )
                }
                Spacer(Modifier.height(8.dp))

                // Big today revenue
                Text(
                    "TODAY'S REVENUE",
                    fontSize = 11.sp, color = t.textSec, letterSpacing = 0.8.sp,
                )
                Text(
                    fmtInr(animRevenue.toDouble()),
                    fontSize   = 48.sp, fontWeight = FontWeight.ExtraBold,
                    color      = UPV.Primary, letterSpacing = (-1).sp, lineHeight = 52.sp,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (trend >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                        contentDescription = null,
                        tint               = if (trend >= 0) UPV.Success else UPV.Error,
                        modifier           = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${if (trend >= 0) "+" else ""}${String.format("%.1f", trend)}% vs yesterday",
                        fontSize   = 13.sp, fontWeight = FontWeight.SemiBold,
                        color      = if (trend >= 0) UPV.Success else UPV.Error,
                    )
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = UPV.Primary.copy(0.15f))
                Spacer(Modifier.height(14.dp))

                // Month + Total
                Row(
                    modifier                = Modifier.fillMaxWidth(),
                    horizontalArrangement   = Arrangement.spacedBy(0.dp),
                ) {
                    listOf(
                        Triple("THIS MONTH",  stats.monthRevenue,  UPV.Info),
                        Triple("ALL TIME",    stats.totalRevenue,  UPV.Purple),
                    ).forEach { (label, value, color) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(label, fontSize = 10.sp, color = t.textTert, letterSpacing = 0.8.sp)
                            val anim by animateFloatAsState(
                                value.toFloat(), tween(1800, 200, EaseOutCubic), label = "anim"
                            )
                            Text(
                                fmtInr(anim.toDouble()),
                                fontSize   = 22.sp, fontWeight = FontWeight.ExtraBold, color = color,
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Stats Grid ────────────────────────────────────────────────────────────────
@Composable
       fun StatsGrid(t: AppTheme) {
    val items = listOf(
        listOf(Icons.Filled.DirectionsCar, UPV.Error,   "${stats.occupiedSlots}",    "Occupied",  "Right now"),
        listOf(Icons.Filled.CheckCircle,   UPV.Success, "${stats.availableSlots}",   "Available", "Free slots"),
        listOf(Icons.Filled.CurrencyRupee, UPV.Primary, fmtInr(stats.todayRevenue),  "Revenue",   "Today"),
        listOf(Icons.Filled.BookOnline,    UPV.Info,    "${stats.activeBookings}",   "Bookings",  "Active"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { item ->
                    @Suppress("UNCHECKED_CAST")
                    val icon  = item[0] as ImageVector
                    val color = item[1] as Color
                    val value = item[2] as String
                    val label = item[3] as String
                    val sub   = item[4] as String
                    UPVCard(t = t, modifier = Modifier.weight(1f)) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier         = Modifier.size(44.dp)
                                    .background(color.copy(if (t.isDark) 0.15f else 0.1f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center,
                            ) { Icon(icon, null, tint = color, modifier = Modifier.size(22.dp)) }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = t.textPrim, maxLines = 1)
                                Text(label, fontSize = 12.sp, color = t.textSec)
                                Text(sub,   fontSize = 11.sp, color = t.textTert)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Donut Occupancy Card ──────────────────────────────────────────────────────
@Composable
       fun OccupancyDonutCard(t: AppTheme) {
    val occ      = stats.occupancyRate
    val trend    = occ - stats.yesterdayRate
    val animFrac by animateFloatAsState(occ / 100f, tween(1200, easing = EaseOutCubic), label = "donut")

    UPVCard(t = t) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(136.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke    = 20.dp.toPx()
                    val r         = (size.minDimension - stroke) / 2
                    val cx        = size.width / 2f
                    val cy        = size.height / 2f
                    val start     = -90f

                    // Track
                    drawCircle(color = Color(0x1AFFFFFF), radius = r, style = Stroke(stroke))

                    // Occupied arc
                    val occSweep = animFrac * 360f
                    drawArc(
                        color      = UPV.Error,
                        startAngle = start, sweepAngle = occSweep, useCenter = false,
                        topLeft    = Offset(cx - r, cy - r), size = Size(r * 2, r * 2),
                        style      = Stroke(stroke, cap = StrokeCap.Round),
                    )
                    // Reserved arc
                    val resSweep = (stats.reservedSlots.toFloat() / stats.totalSlots) * 360f
                    drawArc(
                        color      = UPV.Warning,
                        startAngle = start + occSweep, sweepAngle = resSweep, useCenter = false,
                        topLeft    = Offset(cx - r, cy - r), size = Size(r * 2, r * 2),
                        style      = Stroke(stroke, cap = StrokeCap.Round),
                    )
                    // Free arc
                    val freeSweep = (stats.availableSlots.toFloat() / stats.totalSlots) * 360f
                    drawArc(
                        color      = UPV.Success,
                        startAngle = start + occSweep + resSweep, sweepAngle = freeSweep, useCenter = false,
                        topLeft    = Offset(cx - r, cy - r), size = Size(r * 2, r * 2),
                        style      = Stroke(stroke, cap = StrokeCap.Round),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${occ.toInt()}%", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = t.textPrim)
                    Text("Full", fontSize = 10.sp, color = t.textSec)
                }
            }

            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Lot Occupancy", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = t.textPrim)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (trend >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                        null,
                        tint     = if (trend >= 0) UPV.Success else UPV.Error,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${if (trend >= 0) "+" else ""}${String.format("%.1f", trend)}% vs yesterday",
                        fontSize   = 12.sp,
                        color      = if (trend >= 0) UPV.Success else UPV.Error,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                listOf(
                    Triple(UPV.Error,   "Occupied",    "${stats.occupiedSlots}"),
                    Triple(UPV.Success, "Available",   "${stats.availableSlots}"),
                    Triple(UPV.Warning, "Reserved",    "${stats.reservedSlots}"),
                    Triple(t.textTert,  "Maintenance", "${stats.maintenanceSlots}"),
                ).forEach { (color, label, count) ->
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).background(color, CircleShape))
                            Spacer(Modifier.width(6.dp))
                            Text(label, fontSize = 12.sp, color = t.textSec)
                        }
                        Text(count, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = t.textPrim)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Refresh, null, tint = t.textTert, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Updated ${stats.lastUpdated}", fontSize = 11.sp, color = t.textTert)
                }
            }
        }
    }
}

// ── Car Flow Chart ────────────────────────────────────────────────────────────
@Composable
       fun CarFlowCard(t: AppTheme) {
    val maxVal = flowData.maxOf { maxOf(it.inflow, it.outflow) }.toFloat()
    UPVCard(t = t) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text("Car Flow Analytics", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    listOf(UPV.Success to "Entry", UPV.Error to "Exit").forEach { (c, l) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).background(c, CircleShape))
                            Spacer(Modifier.width(4.dp))
                            Text(l, fontSize = 11.sp, color = t.textSec)
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                listOf(
                    Triple("🟢","Entered","${stats.carsEnteredToday}"),
                    Triple("🔴","Exited","${stats.carsExitedToday}"),
                    Triple("🟡","Inside","${stats.carsEnteredToday - stats.carsExitedToday}"),
                ).forEach { (e, l, v) ->
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(e, fontSize = 12.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(v, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = t.textPrim)
                        }
                        Text(l, fontSize = 11.sp, color = t.textSec)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(
                modifier              = Modifier.fillMaxWidth().height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment     = Alignment.Bottom,
            ) {
                flowData.forEach { pt ->
                    val inH  by animateFloatAsState(pt.inflow  / maxVal, tween(800, easing = EaseOutCubic), label = "in")
                    val outH by animateFloatAsState(pt.outflow / maxVal, tween(800, easing = EaseOutCubic), label = "out")
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            verticalAlignment     = Alignment.Bottom,
                            modifier              = Modifier.height(80.dp),
                        ) {
                            Box(Modifier.width(5.dp).fillMaxHeight(inH).background(UPV.Success, RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)))
                            Box(Modifier.width(5.dp).fillMaxHeight(outH).background(UPV.Error,   RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)))
                        }
                        Text(pt.hour.replace("AM","").replace("PM",""), fontSize = 7.sp, color = t.textTert)
                    }
                }
            }
        }
    }
}
// ── Slot Heatmap Card ─────────────────────────────────────────────────────────
@Composable
       fun SlotHeatmapCard(t: AppTheme) {
    UPVCard(t = t) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text("Live Slot Map", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                val pulse by rememberInfiniteTransition(label = "sp").animateFloat(
                    1f, 0.3f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "sp2"
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(7.dp).background(UPV.Success.copy(pulse), CircleShape))
                    Spacer(Modifier.width(4.dp))
                    Text("Live", fontSize = 11.sp, color = UPV.Success, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(14.dp))
            SlotGrid(t)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                listOf(
                    UPV.Success to "Free", UPV.Error to "Occupied",
                    UPV.Warning to "Reserved", t.textTert to "Blocked",
                ).forEach { (c, l) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(c, CircleShape))
                        Spacer(Modifier.width(4.dp))
                        Text(l, fontSize = 11.sp, color = t.textSec)
                    }
                }
            }
        }
    }
}

@Composable
       fun SlotGrid(t: AppTheme) {
    val cols   = 5
    val chunks = slots.chunked(cols)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        chunks.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { s ->
                    val (bg, border) = when (s.status) {
                        SlotStatus.AVAILABLE    -> UPV.Success.copy(0.12f) to UPV.Success.copy(0.35f)
                        SlotStatus.OCCUPIED     -> UPV.Error.copy(0.12f)   to UPV.Error.copy(0.35f)
                        SlotStatus.RESERVED     -> UPV.Warning.copy(0.12f) to UPV.Warning.copy(0.35f)
                        SlotStatus.MAINTENANCE  -> t.surfaceVar             to t.border
                    }
                    val dotColor = when (s.status) {
                        SlotStatus.AVAILABLE    -> UPV.Success
                        SlotStatus.OCCUPIED     -> UPV.Error
                        SlotStatus.RESERVED     -> UPV.Warning
                        SlotStatus.MAINTENANCE  -> t.textTert
                    }
                    Box(
                        modifier         = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(bg, RoundedCornerShape(10.dp))
                            .border(1.dp, border, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                if (s.status == SlotStatus.OCCUPIED) "🚗" else s.type.emoji,
                                fontSize = 14.sp,
                            )
                            Text(s.number, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = dotColor)
                        }
                    }
                }
                // fill remainder
                repeat(cols - row.size) {
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 2 — SLOTS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun SlotsTab(t: AppTheme) {
    var filter by remember { mutableStateOf<SlotStatus?>(null) }
    val filtered = if (filter == null) slots else slots.filter { it.status == filter }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { UPVChip(t, "All (${slots.size})", filter == null) { filter = null } }
            items(SlotStatus.entries.toTypedArray()) { s ->
                UPVChip(t, "${s.label} (${slots.count { it.status == s }})", filter == s) {
                    filter = if (filter == s) null else s
                }
            }
        }
        LazyColumn(
            contentPadding      = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(filtered) { slot -> SlotCard(t, slot) }
        }
    }
}

@Composable
       fun SlotCard(t: AppTheme, slot: ParkingSlot) {
    val (sc, sb) = when (slot.status) {
        SlotStatus.AVAILABLE   -> UPV.Success to UPV.Success.copy(if (t.isDark) 0.12f else 0.1f)
        SlotStatus.OCCUPIED    -> UPV.Error   to UPV.Error.copy(if (t.isDark) 0.12f else 0.1f)
        SlotStatus.RESERVED    -> UPV.Warning to UPV.Warning.copy(if (t.isDark) 0.12f else 0.1f)
        SlotStatus.MAINTENANCE -> t.textTert  to t.surfaceVar
    }
    UPVCard(t = t) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier         = Modifier.size(54.dp).background(sb, RoundedCornerShape(13.dp))
                    .border(1.dp, sc.copy(0.4f), RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (slot.status == SlotStatus.OCCUPIED) "🚗" else slot.type.emoji, fontSize = 18.sp)
                    Text(slot.number, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = sc)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(slot.type.label, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.width(7.dp))
                    StatusPill(slot.status.label, sc, sb)
                }
                if (slot.driver != null) {
                    Text(slot.driver, fontSize = 13.sp, color = t.textSec)
                    Text("In: ${slot.entry}  ·  ${slot.dur}", fontSize = 12.sp, color = t.textTert)
                } else {
                    Text(
                        if (slot.status == SlotStatus.MAINTENANCE) "Under maintenance" else "No vehicle",
                        fontSize = 13.sp, color = t.textTert,
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("₹${slot.price.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = UPV.Primary)
                Text("/hr", fontSize = 11.sp, color = t.textSec)
                if (slot.vehicle != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        slot.vehicle, fontSize = 10.sp, color = t.textSec,
                        modifier = Modifier.background(t.surfaceVar, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 3 — BOOKINGS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun BookingsTab(t: AppTheme) {
    var tab by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SegBtn(t, "Active (${activeBookings.size})",   tab == 0, Modifier.weight(1f)) { tab = 0 }
            SegBtn(t, "Upcoming (${upcomingBookings.size})", tab == 1, Modifier.weight(1f)) { tab = 1 }
        }
        LazyColumn(
            contentPadding      = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(if (tab == 0) activeBookings else upcomingBookings) { b -> BookingCard(t, b) }
        }
    }
}

@Composable
       fun BookingCard(t: AppTheme, b: Booking) {
    val (sc, sb) = when (b.status) {
        BookingStatus.ACTIVE    -> UPV.Success to UPV.Success.copy(if (t.isDark) 0.12f else 0.1f)
        BookingStatus.UPCOMING  -> UPV.Info    to UPV.Info.copy(if (t.isDark) 0.12f else 0.1f)
        BookingStatus.COMPLETED -> t.textSec   to t.surfaceVar
        BookingStatus.CANCELLED -> UPV.Error   to UPV.Error.copy(if (t.isDark) 0.12f else 0.1f)
    }
    val (pc, pb) = when (b.payStatus) {
        PayStatus.PAID     -> UPV.Success to UPV.Success.copy(if (t.isDark) 0.12f else 0.1f)
        PayStatus.PENDING  -> UPV.Warning to UPV.Warning.copy(if (t.isDark) 0.12f else 0.1f)
        PayStatus.FAILED   -> UPV.Error   to UPV.Error.copy(if (t.isDark) 0.12f else 0.1f)
        PayStatus.REFUNDED -> UPV.Info    to UPV.Info.copy(if (t.isDark) 0.12f else 0.1f)
    }
    UPVCard(t = t) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(b.driver, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                StatusPill(b.status.label, sc, sb)
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier              = Modifier.horizontalScroll(rememberScrollState()),
            ) {
                listOf("🅿 ${b.slot}", "🚗 ${b.vehicle}", "⏰ ${b.entry}", b.dur?.let { "⏱ $it" })
                    .filterNotNull()
                    .forEach { InfoTag(t, it) }
            }
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = t.divider)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text("₹${b.amount}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = UPV.Primary)
                StatusPill(b.payStatus.label, pc, pb)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 4 — REVENUE (DETAILED)
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun RevenueTab(t: AppTheme) {
    var period  by remember { mutableStateOf(0) } // 0=Week 1=Month
    val data    = if (period == 0) weekRevenue else monthRevenue
    val maxVal  = data.maxOf { maxOf(it.actual, it.projected) }
    val bestIdx = data.indexOfMax { it.actual }

    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Hero revenue banner
        item {
            UPVCard(t = t, borderColor = UPV.Primary.copy(0.4f)) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.linearGradient(listOf(UPV.Primary.copy(0.18f), Color.Transparent)))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("TOTAL REVENUE", fontSize = 11.sp, color = t.textSec, letterSpacing = 1.sp)
                        val totalAnim by animateFloatAsState(
                            data.sumOf { it.actual }.toFloat(), tween(1500, easing = EaseOutCubic), label = "tr"
                        )
                        Text(
                            fmtInr(totalAnim.toDouble()),
                            fontSize = 44.sp, fontWeight = FontWeight.ExtraBold,
                            color    = UPV.Primary, letterSpacing = (-1).sp,
                        )
                        val proj = data.sumOf { it.projected }
                        val diff = ((data.sumOf { it.actual } - proj) / proj * 100)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (diff >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                                null, tint = if (diff >= 0) UPV.Success else UPV.Error, modifier = Modifier.size(15.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "${if (diff >= 0) "+" else ""}${String.format("%.1f", diff)}% vs forecast",
                                fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                color    = if (diff >= 0) UPV.Success else UPV.Error,
                            )
                        }
                    }
                }
            }
        }

        // Today / Month cards
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("TODAY",      stats.todayRevenue,  UPV.Primary),
                    Triple("THIS MONTH", stats.monthRevenue,  UPV.Info),
                ).forEach { (l, v, c) ->
                    UPVCard(t = t, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(l, fontSize = 10.sp, color = t.textTert, letterSpacing = 0.8.sp)
                            val a by animateFloatAsState(v.toFloat(), tween(1600, easing = EaseOutCubic), label = "a")
                            Text(fmtInr(a.toDouble()), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = c)
                        }
                    }
                }
            }
        }

        // Revenue chart
        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically,
                    ) {
                        Text("Revenue Chart", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                        Row(modifier = Modifier.background(t.surfaceVar, RoundedCornerShape(8.dp)).padding(2.dp)) {
                            listOf("Week","Month").forEachIndexed { i, lbl ->
                                Box(
                                    modifier         = Modifier
                                        .background(if (period == i) UPV.Primary else Color.Transparent, RoundedCornerShape(6.dp))
                                        .clickable { period = i }
                                        .padding(horizontal = 12.dp, vertical = 5.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        lbl, fontSize = 11.sp,
                                        fontWeight = if (period == i) FontWeight.Bold else FontWeight.Normal,
                                        color      = if (period == i) Color.Black else t.textSec,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        listOf(UPV.Primary to "Actual", t.textTert to "Projected").forEach { (c, l) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).background(c, CircleShape))
                                Spacer(Modifier.width(4.dp))
                                Text(l, fontSize = 11.sp, color = t.textSec)
                            }
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth().height(130.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment     = Alignment.Bottom,
                    ) {
                        data.forEachIndexed { idx, pt ->
                            val isBest = idx == bestIdx
                            val actH   by animateFloatAsState((pt.actual / maxVal).toFloat(), tween(900, easing = EaseOutCubic), label = "actH")
                            val projH  by animateFloatAsState((pt.projected / maxVal).toFloat(), tween(900, easing = EaseOutCubic), label = "projH")
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                if (isBest) Text("★", fontSize = 10.sp, color = UPV.Warning)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    verticalAlignment     = Alignment.Bottom,
                                    modifier              = Modifier.height(100.dp),
                                ) {
                                    Box(
                                        modifier = Modifier.width(12.dp).fillMaxHeight(actH).background(
                                            if (isBest) Brush.verticalGradient(listOf(UPV.Primary, UPV.PrimaryDark))
                                            else Brush.verticalGradient(listOf(UPV.Primary.copy(0.7f), UPV.Primary)),
                                            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
                                        )
                                    )
                                    Box(
                                        modifier = Modifier.width(5.dp).fillMaxHeight(projH)
                                            .background(t.textTert.copy(0.4f), RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(pt.label, fontSize = 9.sp, color = if (isBest) UPV.Primary else t.textTert)
                            }
                        }
                    }
                    val best = data.getOrNull(bestIdx)
                    if (best != null) {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier  = Modifier.fillMaxWidth()
                                .background(UPV.Warning.copy(if (t.isDark) 0.1f else 0.08f), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("⭐", fontSize = 14.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Best: ${best.label} · ${fmtInr(best.actual)}",
                                fontSize = 12.sp, color = t.textPrim, fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }

        // Revenue by slot type
        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Revenue by Slot Type", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(14.dp))
                    val totalRev = slotTypeRevenue.sumOf { it.second }
                    slotTypeRevenue.forEachIndexed { i, (type, rev, color) ->
                        val pct = (rev / totalRev * 100).toFloat()
                        val anim by animateFloatAsState(pct / 100f, tween(1000, i * 100, EaseOutCubic), label = "stype$i")
                        Column(modifier = Modifier.padding(vertical = 5.dp)) {
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(type, fontSize = 12.sp, color = t.textSec, fontWeight = FontWeight.SemiBold)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("${pct.toInt()}%", fontSize = 11.sp, color = t.textTert)
                                    Text(fmtInr(rev), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(t.surfaceDim)) {
                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(anim).background(
                                    Brush.horizontalGradient(listOf(color, color.copy(0.6f))),
                                    RoundedCornerShape(4.dp),
                                ))
                            }
                        }
                    }
                }
            }
        }

        // Payment history
        item { Text("Payment History", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim) }
        items(payments) { p ->
            val (c, b) = when (p.status) {
                PayStatus.PAID     -> UPV.Success to UPV.Success.copy(if (t.isDark) 0.12f else 0.1f)
                PayStatus.PENDING  -> UPV.Warning to UPV.Warning.copy(if (t.isDark) 0.12f else 0.1f)
                PayStatus.FAILED   -> UPV.Error   to UPV.Error.copy(if (t.isDark) 0.12f else 0.1f)
                PayStatus.REFUNDED -> UPV.Info    to UPV.Info.copy(if (t.isDark) 0.12f else 0.1f)
            }
            UPVCard(t = t) {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier         = Modifier.size(44.dp).background(b, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                if (p.status == PayStatus.PAID) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                                null, tint = c, modifier = Modifier.size(22.dp),
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(p.driver, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = t.textPrim)
                            Text("Slot ${p.slot} · ${p.method} · ${p.time}", fontSize = 12.sp, color = t.textSec, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${p.amount}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = t.textPrim)
                        StatusPill(p.status.label, c, b)
                    }
                }
            }
        }
    }
}

// TAB 5 — LOT ANALYSIS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun LotAnalysisTab(t: AppTheme) {
    val evTotal = slots.count { it.type == SlotType.EV }
    val evOcc   = slots.count { it.type == SlotType.EV && it.status == SlotStatus.OCCUPIED }
    val evUtil  = if (evTotal > 0) evOcc * 100 / evTotal else 0

    val typeStats = SlotType.entries.map { type ->
        val all = slots.filter { it.type == type }
        val occ = all.count { it.status == SlotStatus.OCCUPIED }
        Triple(type, all.size, if (all.isNotEmpty()) occ * 100 / all.size else 0)
    }

    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { Text("Lot Analysis", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = t.textPrim) }

        // KPI cards
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("Occupancy",  "${stats.occupancyRate.toInt()}%", UPV.Primary),
                    Triple("EV Util.",   "$evUtil%",                        UPV.Success),
                ).forEach { (l, v, c) ->
                    UPVCard(t = t, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(l, fontSize = 11.sp, color = t.textSec)
                            Text(v, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = c)
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    Triple("Avg Stay",    stats.avgDuration,               UPV.Info),
                    Triple("Return Rate","${stats.customerReturnRate}%",   UPV.Purple),
                ).forEach { (l, v, c) ->
                    UPVCard(t = t, modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(l, fontSize = 11.sp, color = t.textSec)
                            Text(v, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = c)
                        }
                    }
                }
            }
        }

        // Live slot map
        item { SlotHeatmapCard(t) }

        // Utilization by slot type
        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Utilization by Slot Type", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(14.dp))
                    typeStats.forEachIndexed { i, (type, total, util) ->
                        val barColor = when {
                            util >= 80 -> UPV.Error
                            util >= 60 -> UPV.Warning
                            else       -> UPV.Success
                        }
                        val anim by animateFloatAsState(util / 100f, tween(1000, i * 80, EaseOutCubic), label = "util$i")
                        Row(
                            modifier          = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(type.emoji, fontSize = 16.sp, modifier = Modifier.width(26.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(type.label, fontSize = 12.sp, color = t.textSec, modifier = Modifier.width(70.dp))
                            Box(
                                modifier = Modifier.weight(1f).height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)).background(t.surfaceDim)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxHeight().fillMaxWidth(anim)
                                        .background(Brush.horizontalGradient(listOf(barColor, barColor.copy(0.6f))), RoundedCornerShape(5.dp))
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("$util%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = barColor, modifier = Modifier.width(36.dp))
                            Text("${slots.count { it.type == type && it.status == SlotStatus.OCCUPIED }}/$total", fontSize = 11.sp, color = t.textTert)
                        }
                    }
                }
            }
        }

        // Daily occupancy bar chart
        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Occupancy Pattern", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(14.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth().height(160.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment     = Alignment.Bottom,
                    ) {
                        dayOccupancy.forEachIndexed { i, (day, pct) ->
                            val barColor = when {
                                pct >= 80 -> UPV.Error
                                pct >= 65 -> UPV.Warning
                                else      -> UPV.Success
                            }
                            val anim by animateFloatAsState(pct / 100f, tween(1000, i * 80, EaseOutCubic), label = "day$i")
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier            = Modifier.weight(1f),
                            ) {
                                Text("$pct%", fontSize = 9.sp, color = barColor, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(3.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth(0.7f).height(110.dp)
                                        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                        .background(barColor.copy(0.1f))
                                ) {
                                    Box(
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                            .fillMaxWidth().fillMaxHeight(anim)
                                            .background(Brush.verticalGradient(listOf(barColor, barColor.copy(0.6f))))
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(day, fontSize = 10.sp, color = t.textSec, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(UPV.Success to "< 65%", UPV.Warning to "65–80%", UPV.Error to "> 80%").forEach { (c, l) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(8.dp).background(c, RoundedCornerShape(2.dp)))
                                Spacer(Modifier.width(4.dp))
                                Text(l, fontSize = 10.sp, color = t.textTert)
                            }
                        }
                    }
                }
            }
        }

        // Peak hour analysis
        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Peak Hour Analysis", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(14.dp))
                    hourlyPeaks.forEachIndexed { i, (hour, pct, type) ->
                        val peakColor = when (type) {
                            "ENTRY" -> UPV.Success
                            "EXIT"  -> UPV.Error
                            "MIXED" -> UPV.Warning
                            else    -> t.textTert
                        }
                        val anim by animateFloatAsState(pct / 100f, tween(1000, i * 100, EaseOutCubic), label = "peak$i")
                        Row(
                            modifier          = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(hour, fontSize = 12.sp, color = t.textSec, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(72.dp))
                            Box(
                                modifier = Modifier.weight(1f).height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)).background(t.surfaceDim)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxHeight().fillMaxWidth(anim)
                                        .background(Brush.horizontalGradient(listOf(peakColor, peakColor.copy(0.6f))), RoundedCornerShape(5.dp))
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("$pct%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = peakColor, modifier = Modifier.width(38.dp))
                            StatusPill(type, peakColor, peakColor.copy(if (t.isDark) 0.15f else 0.1f))
                        }
                    }
                }
            }
        }

        // AI Suggestions
        item {
            UPVCard(t = t, borderColor = UPV.Info.copy(0.3f)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Smart Suggestions", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(10.dp))
                    listOf(
                        "EV slots have 95% utilisation — consider adding 2 more" to UPV.Success,
                        "Fri & Sat are peak — surge pricing could add ₹12,000/mo" to UPV.Warning,
                        "Tue 2–4 PM is lowest — promotional discount fills 5 extra slots" to UPV.Info,
                        "68% return rate — loyalty rewards could push it to 80%+" to UPV.Purple,
                    ).forEach { (tip, color) ->
                        Row(modifier = Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(6.dp).offset(y = 5.dp).background(color, CircleShape))
                            Spacer(Modifier.width(9.dp))
                            Text(tip, fontSize = 12.sp, color = t.textSec, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 6 — INSIGHTS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun InsightsTab(t: AppTheme) {
    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text("Owner Insights", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = t.textPrim) }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    listOf("📊","Avg Occupancy","${stats.occupancyRate.toInt()}% / day", UPV.Info),
                    listOf("💰","Revenue Proj.", fmtInr(stats.monthRevenue * 1.08),       UPV.Success),
                    listOf("⭐","Best Slot",      stats.bestSlot,                          UPV.Warning),
                    listOf("🔄","Return Rate",   "${stats.customerReturnRate}% customers", UPV.Purple),
                ).chunked(2).forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        row.forEach { item ->
                            val emoji = item[0] as String; val label = item[1] as String
                            val value = item[2] as String; val color = item[3] as Color
                            UPVCard(t = t, modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(emoji, fontSize = 24.sp)
                                    Spacer(Modifier.height(8.dp))
                                    Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(label, fontSize = 11.sp, color = t.textSec)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Low Performing Hours", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(12.dp))
                    listOf("2–4 AM" to 8, "3–5 PM" to 22, "11 PM–1 AM" to 5).forEachIndexed { i, (hour, pct) ->
                        val anim by animateFloatAsState(pct / 100f, tween(1000, i * 100, EaseOutCubic), label = "low$i")
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(hour, fontSize = 13.sp, color = t.textSec, modifier = Modifier.width(90.dp))
                            Box(modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(t.surfaceDim)) {
                                Box(Modifier.fillMaxHeight().fillMaxWidth(anim).background(UPV.Error, RoundedCornerShape(4.dp)))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("$pct%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = UPV.Error)
                        }
                    }
                }
            }
        }

        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Avg Occupancy by Day", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(12.dp))
                    dayOccupancy.forEachIndexed { i, (day, pct) ->
                        val barColor = when { pct >= 80 -> UPV.Error; pct >= 60 -> UPV.Warning; else -> UPV.Success }
                        val anim by animateFloatAsState(pct / 100f, tween(1000, i * 80, EaseOutCubic), label = "occ$i")
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(day, fontSize = 13.sp, color = t.textSec, modifier = Modifier.width(40.dp))
                            Box(modifier = Modifier.weight(1f).height(10.dp).clip(RoundedCornerShape(5.dp)).background(t.surfaceDim)) {
                                Box(Modifier.fillMaxHeight().fillMaxWidth(anim).background(barColor, RoundedCornerShape(5.dp)))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("$pct%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = barColor)
                        }
                    }
                }
            }
        }

        item {
            UPVCard(t = t) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💡 Smart Suggestions", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim)
                    Spacer(Modifier.height(10.dp))
                    listOf(
                        "Consider discounts on Tue 2–4 PM (lowest occupancy)" to UPV.Info,
                        "Sat & Fri are peak days — consider surge pricing"     to UPV.Warning,
                        "EV slots have 95% utilisation — add 2 more"           to UPV.Success,
                        "68% return — loyalty program could boost to 80%"      to UPV.Purple,
                    ).forEach { (tip, color) ->
                        Row(modifier = Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(6.dp).offset(y = 5.dp).background(color, CircleShape))
                            Spacer(Modifier.width(8.dp))
                            Text(tip, fontSize = 12.sp, color = t.textSec, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB 7 — ALERTS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
 fun AlertsTab(t: AppTheme) {
    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text("Real-Time Alerts", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = t.textPrim)
                Text("${alerts.size} active", fontSize = 13.sp, color = UPV.Error, fontWeight = FontWeight.Bold)
            }
        }
        items(alerts) { alert -> AlertCard(t, alert) }
    }
}

@Composable
   fun AlertCard(t: AppTheme, alert: Alert) {
    data class AlertStyle(val icon: ImageVector, val color: Color, val title: String)
    val style = when (alert.type) {
        AlertType.OVERSTAY   -> AlertStyle(Icons.Filled.Timer,        UPV.Warning, "Overstay Alert")
        AlertType.PAYMENT    -> AlertStyle(Icons.Filled.CreditCardOff, UPV.Error,  "Payment Issue")
        AlertType.CAMERA     -> AlertStyle(Icons.Filled.VideocamOff,   UPV.Error,  "Camera Offline")
        AlertType.BARRIER    -> AlertStyle(Icons.Filled.Block,         UPV.Warning,"Barrier Issue")
        AlertType.SUSPICIOUS -> AlertStyle(Icons.Filled.WarningAmber,  UPV.Error,  "Suspicious Activity")
    }
    UPVCard(t = t, borderColor = style.color.copy(0.3f)) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier         = Modifier.size(44.dp)
                    .background(style.color.copy(if (t.isDark) 0.15f else 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) { Icon(style.icon, null, tint = style.color, modifier = Modifier.size(22.dp)) }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(style.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = t.textPrim)
                Text(alert.message, fontSize = 13.sp, color = t.textSec, lineHeight = 18.sp)
                Text(alert.time, fontSize = 11.sp, color = t.textTert)
            }
            TextButton(onClick = {}) {
                Text("Resolve", fontSize = 12.sp, color = style.color, fontWeight = FontWeight.Bold)
            }
        }
    }
}
// ═══════════════════════════════════════════════════════════════════════════════
// TAB 8 — PRICING
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun PricingTab(t: AppTheme) {
    var pricingState by remember { mutableStateOf(pricing) }
    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            UPVCard(t = t, borderColor = UPV.Warning.copy(0.4f)) {
                Row(
                    modifier  = Modifier.padding(14.dp).background(
                        Brush.linearGradient(listOf(UPV.Warning.copy(0.1f), Color.Transparent))
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("⚡", fontSize = 20.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Auto Billing charges customers automatically on exit based on duration × rate.",
                        fontSize = 13.sp, color = t.textPrim, lineHeight = 18.sp,
                    )
                }
            }
        }
        items(pricingState) { p ->
            PricingCard(
                t               = t,
                p               = p,
                onPriceChange   = { np -> pricingState = pricingState.map { if (it.type == p.type) it.copy(perHour = np) else it } },
                onBillingToggle = { e  -> pricingState = pricingState.map { if (it.type == p.type) it.copy(autoBilling = e) else it } },
            )
        }
    }
}

@Composable
       fun PricingCard(t: AppTheme, p: SlotPricing, onPriceChange: (Double) -> Unit, onBillingToggle: (Boolean) -> Unit) {
    var expanded  by remember { mutableStateOf(false) }
    var tempPrice by remember { mutableStateOf(p.perHour.toString()) }

    UPVCard(t = t) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(p.type.emoji, fontSize = 28.sp)
                    Spacer(Modifier.width(10.dp))
                    Text(p.type.label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = t.textPrim)
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Filled.ExpandLess else Icons.Filled.Edit,
                        null, tint = UPV.Primary,
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PriceTag(t, "₹${p.perHour.toInt()}/hr",  UPV.Primary.copy(if (t.isDark) 0.15f else 0.12f), UPV.Primary)
                PriceTag(t, "₹${p.perDay.toInt()}/day",  UPV.Info.copy(if (t.isDark) 0.15f else 0.12f),    UPV.Info)
                PriceTag(t, "₹${p.perMonth.toInt()}/mo", UPV.Success.copy(if (t.isDark) 0.15f else 0.12f), UPV.Success)
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Auto Billing", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = t.textPrim)
                    Text("Grace ${p.graceMins}min · Overtime ₹${p.overtime.toInt()}/hr", fontSize = 12.sp, color = t.textSec)
                }
                Switch(
                    checked          = p.autoBilling,
                    onCheckedChange  = onBillingToggle,
                    colors           = SwitchDefaults.colors(
                        checkedThumbColor  = Color.White,
                        checkedTrackColor  = UPV.Primary,
                        uncheckedTrackColor = t.surfaceDim,
                    ),
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = t.divider)
                    Spacer(Modifier.height(12.dp))
                    Text("Update Hourly Rate", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = t.textPrim)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value         = tempPrice,
                            onValueChange = { tempPrice = it },
                            modifier      = Modifier.weight(1f),
                            singleLine    = true,
                            shape         = RoundedCornerShape(10.dp),
                            label         = { Text("₹ per hour") },
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = UPV.Primary,
                                focusedLabelColor    = UPV.Primary,
                                unfocusedBorderColor = t.border,
                                unfocusedTextColor   = t.textPrim,
                                focusedTextColor     = t.textPrim,
                            ),
                        )
                        Button(
                            onClick = { tempPrice.toDoubleOrNull()?.let { onPriceChange(it); expanded = false } },
                            shape   = RoundedCornerShape(10.dp),
                            colors  = ButtonDefaults.buttonColors(containerColor = UPV.Primary, contentColor = Color.Black),
                        ) { Text("Save", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHARED COMPONENTS
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
       fun UPVCard(
    t           : AppTheme,
    modifier    : Modifier  = Modifier.fillMaxWidth(),
    bgOverride  : Color?    = null,
    borderColor : Color?    = null,
    content     : @Composable () -> Unit,
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = bgOverride ?: t.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (t.isDark) 0.dp else 2.dp),
        border    = BorderStroke(1.dp, borderColor ?: t.border),
        content   = { content() },
    )
}

@Composable
       fun StatusPill(text: String, color: Color, bg: Color) {
    Text(
        text,
        modifier   = Modifier.background(bg, RoundedCornerShape(20.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
        fontSize   = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color      = color,
    )
}

@Composable
       fun UPVChip(t: AppTheme, label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick  = onClick,
        label    = { Text(label, fontSize = 12.sp) },
        colors   = FilterChipDefaults.filterChipColors(
            selectedContainerColor = UPV.Primary,
            selectedLabelColor     = Color.Black,
            containerColor         = t.surface,
            labelColor             = t.textSec,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled              = true,
            selected             = selected,
            borderColor          = t.border,
            selectedBorderColor  = UPV.PrimaryDark,
        ),
    )
}

@Composable
       fun SegBtn(t: AppTheme, label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick   = onClick,
        modifier  = modifier.height(44.dp),
        shape     = RoundedCornerShape(11.dp),
        colors    = ButtonDefaults.buttonColors(
            containerColor = if (selected) UPV.Primary else t.surfaceVar,
            contentColor   = if (selected) Color.Black else t.textSec,
        ),
        elevation = ButtonDefaults.buttonElevation(if (selected) 2.dp else 0.dp),
    ) { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp) }
}

@Composable
       fun InfoTag(t: AppTheme, text: String) {
    Text(
        text,
        modifier   = Modifier.background(t.surfaceVar, RoundedCornerShape(6.dp)).padding(horizontal = 9.dp, vertical = 5.dp),
        fontSize   = 12.sp,
        color      = t.textSec,
    )
}

@Composable
       fun PriceTag(t: AppTheme, text: String, bg: Color, color: Color) {
    Text(
        text,
        modifier   = Modifier.background(bg, RoundedCornerShape(6.dp)).padding(horizontal = 10.dp, vertical = 5.dp),
        fontSize   = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color      = color,
    )
}
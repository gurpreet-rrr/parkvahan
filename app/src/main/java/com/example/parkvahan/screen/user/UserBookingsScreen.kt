package com.example.parkvahan.screen.user

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

// ═══════════════════════════════════════════════════════════════════════════════
// DESIGN TOKENS
// ═══════════════════════════════════════════════════════════════════════════════
private object BK {
    val Primary       = Color(0xFFFFC107)
    val PrimaryDark   = Color(0xFFFF8F00)
    val PrimaryLight  = Color(0xFFFFF8E1)
    val OnPrimary     = Color(0xFF1A1A1A)
    val Background    = Color(0xFFF8F9FA)
    val Surface       = Color(0xFFFFFFFF)
    val SurfaceVar    = Color(0xFFF5F5F5)
    val TextPrimary   = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF6B7280)
    val TextTertiary  = Color(0xFFBDBDBD)
    val Success       = Color(0xFF16A34A)
    val SuccessLight  = Color(0xFFDCFCE7)
    val Error         = Color(0xFFDC2626)
    val ErrorLight    = Color(0xFFFEE2E2)
    val Warning       = Color(0xFFD97706)
    val WarningLight  = Color(0xFFFEF3C7)
    val Info          = Color(0xFF2563EB)
    val InfoLight     = Color(0xFFDBEAFE)
    val Divider       = Color(0xFFF3F4F6)
    val Border        = Color(0xFFE5E7EB)
}

// ═══════════════════════════════════════════════════════════════════════════════
// DATA MODELS
// ═══════════════════════════════════════════════════════════════════════════════
private enum class BookingStatus(val label: String) {
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

private enum class BookingTab(val label: String) {
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

private data class BookingItem(
    val id            : String,
    val lotName       : String,
    val address       : String,
    val slotNumber    : String,
    val date          : String,
    val entryTime     : String,
    val exitTime      : String,
    val vehicleType   : String,
    val vehicleNumber : String,
    val price         : Double,
    val duration      : String,
    val status        : BookingStatus,
    val paymentMethod : String,
    val bookingDate   : String,
)

// ═══════════════════════════════════════════════════════════════════════════════
// SAMPLE DATA
// ═══════════════════════════════════════════════════════════════════════════════
private val sampleBookings = listOf(
    BookingItem("BK001","Phoenix Mall Parking","Nagar Road, Pune","A-12","Today","10:30 AM","12:30 PM","Standard","MH12AB1234",120.0,"2h", BookingStatus.ONGOING,"UPI","23 Mar 2026"),
    BookingItem("BK002","City Center Park","MG Road, Pune","B-05","Today","09:00 AM","10:00 AM","Compact","MH12AB1234",40.0,"1h", BookingStatus.ONGOING,"Card","23 Mar 2026"),
    BookingItem("BK003","Green Plaza Parking","FC Road, Pune","C-08","22 Mar 2026","02:00 PM","05:00 PM","SUV","MH12AB1234",150.0,"3h", BookingStatus.COMPLETED,"UPI","22 Mar 2026"),
    BookingItem("BK004","Metro Station P&R","Shivajinagar","D-02","21 Mar 2026","11:00 AM","02:00 PM","Standard","MH12AB1234",90.0,"3h", BookingStatus.COMPLETED,"Cash","21 Mar 2026"),
    BookingItem("BK005","The Grand Hotel","Koregaon Park","VIP-01","20 Mar 2026","07:00 PM","10:00 PM","SUV","MH12AB1234",240.0,"3h", BookingStatus.COMPLETED,"Card","20 Mar 2026"),
    BookingItem("BK006","Street Smart Park","Camp, Pune","E-11","19 Mar 2026","08:00 AM","09:00 AM","Bike","MH12AB1234",25.0,"1h", BookingStatus.COMPLETED,"UPI","19 Mar 2026"),
    BookingItem("BK007","Phoenix Mall Parking","Nagar Road, Pune","A-07","18 Mar 2026","03:00 PM","05:00 PM","Standard","MH12AB1234",120.0,"2h", BookingStatus.CANCELLED,"UPI","18 Mar 2026"),
    BookingItem("BK008","City Center Park","MG Road, Pune","B-03","17 Mar 2026","10:00 AM","11:00 AM","Compact","MH12AB1234",40.0,"1h", BookingStatus.CANCELLED,"Card","17 Mar 2026"),
)

// ═══════════════════════════════════════════════════════════════════════════════
// MAIN SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
fun UserBookingsScreen(
    onNavigateBack: () -> Unit,
) {
    var selectedTab   by remember { mutableStateOf(BookingTab.ONGOING) }
    var searchQuery   by remember { mutableStateOf("") }
    var showSearch    by remember { mutableStateOf(false) }
    var selectedItem  by remember { mutableStateOf<BookingItem?>(null) }

    val filteredBookings = remember(selectedTab, searchQuery) {
        sampleBookings
            .filter { booking ->
                when (selectedTab) {
                    BookingTab.ONGOING    -> booking.status == BookingStatus.ONGOING
                    BookingTab.COMPLETED  -> booking.status == BookingStatus.COMPLETED
                    BookingTab.CANCELLED  -> booking.status == BookingStatus.CANCELLED
                }
            }
            .filter {
                searchQuery.isEmpty() ||
                        it.lotName.contains(searchQuery, ignoreCase = true) ||
                        it.slotNumber.contains(searchQuery, ignoreCase = true) ||
                        it.vehicleNumber.contains(searchQuery, ignoreCase = true)
            }
    }

    // Summary counts
    val ongoingCount   = sampleBookings.count { it.status == BookingStatus.ONGOING }
    val completedCount = sampleBookings.count { it.status == BookingStatus.COMPLETED }
    val cancelledCount = sampleBookings.count { it.status == BookingStatus.CANCELLED }

    Box(modifier = Modifier.fillMaxSize().background(BK.Background)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Header ────────────────────────────────────────────────────────
            BookingsHeader(
                showSearch  = showSearch,
                searchQuery = searchQuery,
                onSearch    = { showSearch = !showSearch; if (!showSearch) searchQuery = "" },
                onQuery     = { searchQuery = it },
                onBack      = onNavigateBack
            )

            // ── Summary bar ───────────────────────────────────────────────────
            SummaryBar(
                ongoing   = ongoingCount,
                completed = completedCount,
                cancelled = cancelledCount
            )

            // ── Tab row ───────────────────────────────────────────────────────
            BookingTabRow(
                selectedTab    = selectedTab,
                ongoingCount   = ongoingCount,
                completedCount = completedCount,
                cancelledCount = cancelledCount,
                onTabSelected  = { selectedTab = it }
            )

            // ── Content ───────────────────────────────────────────────────────
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(tween(220)) togetherWith fadeOut(tween(150))
                },
                label = "tab_content"
            ) { tab ->
                if (filteredBookings.isEmpty()) {
                    EmptyBookingsView(tab = tab)
                } else {
                    LazyColumn(
                        contentPadding      = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier            = Modifier.fillMaxSize()
                    ) {
                        items(filteredBookings, key = { it.id }) { booking ->
                            BookingCard(
                                booking = booking,
                                onClick = { selectedItem = booking }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }

        // ── Detail bottom sheet ───────────────────────────────────────────────
        selectedItem?.let { booking ->
            BookingDetailSheet(
                booking   = booking,
                onDismiss = { selectedItem = null }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// HEADER
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingsHeader(
    showSearch  : Boolean,
    searchQuery : String,
    onSearch    : () -> Unit,
    onQuery     : (String) -> Unit,
    onBack      : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BK.Surface)
            .statusBarsPadding()
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = BK.TextPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "My Bookings",
                    fontSize      = 22.sp,
                    fontWeight    = FontWeight.ExtraBold,
                    color         = BK.TextPrimary,
                    letterSpacing = (-0.5).sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Search toggle
                IconButton(onClick = onSearch) {
                    Icon(
                        if (showSearch) Icons.Default.Close else Icons.Outlined.Search,
                        "Search",
                        tint = if (showSearch) BK.PrimaryDark else BK.TextSecondary
                    )
                }
                // Filter icon
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.FilterList, "Filter", tint = BK.TextSecondary)
                }
            }
        }

        // Search bar — animated
        AnimatedVisibility(
            visible = showSearch,
            enter   = expandVertically() + fadeIn(),
            exit    = shrinkVertically() + fadeOut()
        ) {
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = onQuery,
                modifier      = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp),
                placeholder   = { Text("Search by lot, slot or vehicle...", fontSize = 14.sp, color = BK.TextTertiary) },
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                leadingIcon   = { Icon(Icons.Outlined.Search, null, tint = BK.TextSecondary, modifier = Modifier.size(18.dp)) },
                trailingIcon  = if (searchQuery.isNotEmpty()) {{
                    IconButton(onClick = { onQuery("") }) {
                        Icon(Icons.Default.Close, null, tint = BK.TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }} else null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = BK.Primary,
                    unfocusedBorderColor = BK.Border,
                    focusedTextColor     = BK.TextPrimary,
                    unfocusedTextColor   = BK.TextPrimary,
                )
            )
        }

        Divider(color = BK.Divider)
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SUMMARY BAR
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun SummaryBar(ongoing: Int, completed: Int, cancelled: Int) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .background(BK.Surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Triple(ongoing,   "Ongoing",   BK.Warning),
            Triple(completed, "Completed", BK.Success),
            Triple(cancelled, "Cancelled", BK.Error),
        ).forEach { (count, label, color) ->
            Row(
                modifier          = Modifier
                    .weight(1f)
                    .background(color.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$count",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = color
                    )
                    Text(
                        label,
                        fontSize = 11.sp,
                        color    = color.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// TAB ROW
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingTabRow(
    selectedTab    : BookingTab,
    ongoingCount   : Int,
    completedCount : Int,
    cancelledCount : Int,
    onTabSelected  : (BookingTab) -> Unit,
) {
    val counts = mapOf(
        BookingTab.ONGOING   to ongoingCount,
        BookingTab.COMPLETED to completedCount,
        BookingTab.CANCELLED to cancelledCount,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BK.Surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BookingTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val tabColor   = when (tab) {
                BookingTab.ONGOING   -> BK.Warning
                BookingTab.COMPLETED -> BK.Success
                BookingTab.CANCELLED -> BK.Error
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) tabColor
                        else BK.SurfaceVar
                    )
                    .clickable { onTabSelected(tab) }
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        tab.label,
                        fontSize   = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color      = if (isSelected) Color.White else BK.TextSecondary
                    )
                    // Count badge
                    val count = counts[tab] ?: 0
                    if (count > 0) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier         = Modifier
                                .size(18.dp)
                                .background(
                                    if (isSelected) Color.White.copy(alpha = 0.3f)
                                    else tabColor.copy(alpha = 0.15f),
                                    CircleShape
                                )
                        ) {
                            Text(
                                "$count",
                                fontSize   = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = if (isSelected) Color.White else tabColor
                            )
                        }
                    }
                }
            }
        }
    }
    Divider(color = BK.Divider)
}

// ═══════════════════════════════════════════════════════════════════════════════
// BOOKING CARD
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingCard(booking: BookingItem, onClick: () -> Unit) {
    val (statusColor, statusBg) = when (booking.status) {
        BookingStatus.ONGOING   -> BK.Warning to BK.WarningLight
        BookingStatus.COMPLETED -> BK.Success to BK.SuccessLight
        BookingStatus.CANCELLED -> BK.Error   to BK.ErrorLight
    }

    // Animate card entrance
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(booking.id) { visible = true }

    Card(
        modifier  = Modifier.fillMaxWidth().clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = BK.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border    = BorderStroke(1.dp, BK.Border)
    ) {
        Column {
            // ── Top colored strip based on status ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(listOf(statusColor, statusColor.copy(alpha = 0.4f)))
                    )
            )

            Column(modifier = Modifier.padding(14.dp)) {

                // ── Row 1: Lot name + Status badge ────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            booking.lotName,
                            fontSize      = 15.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = BK.TextPrimary,
                            maxLines      = 1,
                            overflow      = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.LocationOn, null,
                                tint     = BK.TextTertiary,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                booking.address,
                                fontSize  = 11.sp,
                                color     = BK.TextSecondary,
                                maxLines  = 1,
                                overflow  = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // Status badge
                    Row(
                        modifier          = Modifier
                            .background(statusBg, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(statusColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            booking.status.label,
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color      = statusColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = BK.Divider)
                Spacer(modifier = Modifier.height(12.dp))

                // ── Row 2: Slot + Time + Vehicle ──────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Slot
                    BookingInfoChip(
                        modifier = Modifier.weight(1f),
                        icon     = "🅿",
                        label    = "Slot",
                        value    = booking.slotNumber
                    )
                    // Time
                    BookingInfoChip(
                        modifier = Modifier.weight(1.4f),
                        icon     = "⏰",
                        label    = "Time",
                        value    = "${booking.entryTime} – ${booking.exitTime}"
                    )
                    // Vehicle
                    BookingInfoChip(
                        modifier = Modifier.weight(1f),
                        icon     = "🚗",
                        label    = "Type",
                        value    = booking.vehicleType
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Row 3: Date + Duration + Price ────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.CalendarMonth, null,
                                tint     = BK.TextTertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(booking.date, fontSize = 12.sp, color = BK.TextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Timer, null,
                                tint     = BK.TextTertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(booking.duration, fontSize = 12.sp, color = BK.TextSecondary)
                        }
                    }
                    // Price
                    Text(
                        "₹${booking.price.toInt()}",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = BK.PrimaryDark
                    )
                }

                // ── Ongoing: active timer indicator ───────────────────────────
                if (booking.status == BookingStatus.ONGOING) {
                    Spacer(modifier = Modifier.height(10.dp))
                    val pulse by rememberInfiniteTransition(label = "pulse")
                        .animateFloat(
                            0.6f, 1f,
                            infiniteRepeatable(tween(900), RepeatMode.Reverse),
                            label = "p"
                        )
                    Row(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .background(BK.WarningLight, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(BK.Warning.copy(alpha = pulse), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Session active — ends at ${booking.exitTime}",
                            fontSize   = 12.sp,
                            color      = BK.Warning,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // ── Cancelled: reason chip ─────────────────────────────────
                if (booking.status == BookingStatus.CANCELLED) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier          = Modifier
                            .fillMaxWidth()
                            .background(BK.ErrorLight, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Cancel, null,
                            tint     = BK.Error,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Booking cancelled · Refund processed",
                            fontSize = 12.sp,
                            color    = BK.Error
                        )
                    }
                }

                // ── Completed: rebook button ───────────────────────────────
                if (booking.status == BookingStatus.COMPLETED) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Download receipt
                        OutlinedButton(
                            onClick  = {},
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape    = RoundedCornerShape(10.dp),
                            border   = BorderStroke(1.dp, BK.Border),
                            colors   = ButtonDefaults.outlinedButtonColors(contentColor = BK.TextSecondary)
                        ) {
                            Icon(Icons.Outlined.Receipt, null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Receipt", fontSize = 12.sp)
                        }
                        // Rebook
                        Button(
                            onClick  = {},
                            modifier = Modifier.weight(1f).height(38.dp),
                            shape    = RoundedCornerShape(10.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor = BK.Primary,
                                contentColor   = BK.OnPrimary
                            )
                        ) {
                            Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text("Rebook", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// BOOKING INFO CHIP
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingInfoChip(
    modifier : Modifier,
    icon     : String,
    label    : String,
    value    : String,
) {
    Column(modifier = modifier) {
        Text(label, fontSize = 10.sp, color = BK.TextTertiary)
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                value,
                fontSize   = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color      = BK.TextPrimary,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// EMPTY STATE
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun EmptyBookingsView(tab: BookingTab) {
    val (emoji, title, subtitle) = when (tab) {
        BookingTab.ONGOING   -> Triple("🅿️", "No active bookings", "You don't have any ongoing parking sessions")
        BookingTab.COMPLETED -> Triple("✅", "No completed bookings", "Your completed bookings will appear here")
        BookingTab.CANCELLED -> Triple("❌", "No cancelled bookings", "You haven't cancelled any bookings")
    }

    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(32.dp)
        ) {
            Box(
                modifier         = Modifier
                    .size(100.dp)
                    .background(BK.SurfaceVar, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 44.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                title,
                fontSize   = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = BK.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                subtitle,
                fontSize  = 14.sp,
                color     = BK.TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick  = {},
                shape    = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp).fillMaxWidth(0.6f),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = BK.Primary,
                    contentColor   = BK.OnPrimary
                )
            ) {
                Icon(Icons.Filled.Add, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Book Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// BOOKING DETAIL BOTTOM SHEET
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingDetailSheet(booking: BookingItem, onDismiss: () -> Unit) {
    val (statusColor, statusBg) = when (booking.status) {
        BookingStatus.ONGOING   -> BK.Warning to BK.WarningLight
        BookingStatus.COMPLETED -> BK.Success to BK.SuccessLight
        BookingStatus.CANCELLED -> BK.Error   to BK.ErrorLight
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = BK.Surface,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Title row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Booking Details",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = BK.TextPrimary
                )
                Box(
                    modifier = Modifier
                        .background(statusBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(booking.status.label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booking ID card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BK.PrimaryLight, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Booking ID", fontSize = 11.sp, color = BK.TextSecondary)
                        Text(
                            "#PV${booking.id}",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color      = BK.PrimaryDark
                        )
                    }
                    Icon(Icons.Outlined.QrCode, null, tint = BK.PrimaryDark, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details list
            listOf(
                Triple(Icons.Outlined.LocalParking,   "Parking Lot",    booking.lotName),
                Triple(Icons.Outlined.LocationOn,     "Address",        booking.address),
                Triple(Icons.Outlined.GridView,       "Slot Number",    booking.slotNumber),
                Triple(Icons.Outlined.CalendarMonth,  "Date",           booking.date),
                Triple(Icons.Outlined.Schedule,       "Entry Time",     booking.entryTime),
                Triple(Icons.Outlined.Schedule,       "Exit Time",      booking.exitTime),
                Triple(Icons.Outlined.Timer,          "Duration",       booking.duration),
                Triple(Icons.Outlined.DirectionsCar,  "Vehicle",        "${booking.vehicleType} · ${booking.vehicleNumber}"),
                Triple(Icons.Outlined.Payment,        "Payment",        "${booking.paymentMethod} · ₹${booking.price.toInt()}"),
            ).forEach { (icon, label, value) ->
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(icon, null, tint = BK.TextTertiary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(label, fontSize = 13.sp, color = BK.TextSecondary, modifier = Modifier.width(100.dp))
                    Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BK.TextPrimary)
                }
                Divider(color = BK.Divider)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            when (booking.status) {
                BookingStatus.ONGOING -> {
                    Button(
                        onClick  = onDismiss,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(13.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = BK.Error, contentColor = Color.White)
                    ) {
                        Icon(Icons.Outlined.Cancel, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cancel Booking", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
                BookingStatus.COMPLETED -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick  = {},
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape    = RoundedCornerShape(13.dp),
                            border   = BorderStroke(1.5.dp, BK.Primary)
                        ) {
                            Icon(Icons.Outlined.Receipt, null, tint = BK.PrimaryDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Receipt", fontWeight = FontWeight.Bold, color = BK.PrimaryDark)
                        }
                        Button(
                            onClick  = {},
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape    = RoundedCornerShape(13.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = BK.Primary, contentColor = BK.OnPrimary)
                        ) {
                            Icon(Icons.Filled.Refresh, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Rebook", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
                BookingStatus.CANCELLED -> {
                    Button(
                        onClick  = {},
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(13.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = BK.Primary, contentColor = BK.OnPrimary)
                    ) {
                        Text("Book Again", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}
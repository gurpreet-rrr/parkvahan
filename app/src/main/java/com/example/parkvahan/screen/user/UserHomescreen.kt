package com.example.parkvahan.screen.user

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

// ═══════════════════════════════════════════════════════════════════════════════
// DESIGN TOKENS
// ═══════════════════════════════════════════════════════════════════════════════
private object PV {
    val Primary          = Color(0xFFFFC107)
    val PrimaryDark      = Color(0xFFFF8F00)
    val PrimaryLight     = Color(0xFFFFF8E1)
    val PrimaryContainer = Color(0xFFFFECB3)
    val OnPrimary        = Color(0xFF1A1A1A)
    val Background       = Color(0xFFF8F9FA)
    val Surface          = Color(0xFFFFFFFF)
    val SurfaceVariant   = Color(0xFFF5F5F5)
    val SurfaceDim       = Color(0xFFEEEEEE)
    val TextPrimary      = Color(0xFF1A1A1A)
    val TextSecondary    = Color(0xFF6B7280)
    val TextTertiary     = Color(0xFFBDBDBD)
    val Success          = Color(0xFF16A34A)
    val SuccessLight     = Color(0xFFDCFCE7)
    val Error            = Color(0xFFDC2626)
    val ErrorLight       = Color(0xFFFEE2E2)
    val Warning          = Color(0xFFD97706)
    val WarningLight     = Color(0xFFFEF3C7)
    val Info             = Color(0xFF2563EB)
    val InfoLight        = Color(0xFFDBEAFE)
    val Purple           = Color(0xFF7C3AED)
    val PurpleLight      = Color(0xFFEDE9FE)
    val MapBlue          = Color(0xFF3B82F6)
    val MapBlueDark      = Color(0xFF1D4ED8)
    val MapRoad          = Color(0xFFE5E7EB)
    val MapBg            = Color(0xFFEFF6FF)
    val Divider          = Color(0xFFF3F4F6)
    val Border           = Color(0xFFE5E7EB)
    val Overlay          = Color(0x80000000)
}

// ═══════════════════════════════════════════════════════════════════════════════
// DATA MODELS
// ═══════════════════════════════════════════════════════════════════════════════
private enum class VehicleSize(val label: String, val emoji: String) {
    COMPACT("Compact","🚗"), STANDARD("Standard","🚙"), SUV("SUV","🚐"),
    TRUCK("Truck","🚛"), BIKE("Bike","🏍"), EV("EV","⚡"),
}

private enum class SlotFilter(val label: String) {
    ALL("All"), NEARBY("Nearby"), CHEAPEST("Cheapest"), PREMIUM("Premium"),
    COVERED("Covered"), EV("EV Charging"), OPEN24H("24/7 Open"),
}

private enum class SortOption(val label: String) {
    DISTANCE("Distance"), PRICE_LOW("Price: Low"), PRICE_HIGH("Price: High"),
    RATING("Rating"), AVAILABILITY("Availability"),
}

private data class ParkingLot(
    val id           : String,
    val name         : String,
    val address      : String,
    val distance     : String,
    val walkTime     : String,
    val rating       : Float,
    val reviews      : Int,
    val pricePerHour : Double,
    val totalSlots   : Int,
    val available    : Int,
    val isPremium    : Boolean      = false,
    val isCovered    : Boolean      = false,
    val hasEV        : Boolean      = false,
    val is24Hours    : Boolean      = false,
    val hasValet     : Boolean      = false,
    val hasCCTV      : Boolean      = false,
    val amenities    : List<String> = emptyList(),
    val mapX         : Float        = 0f,
    val mapY         : Float        = 0f,
    val occupancyPct : Int          = 0,
)

private data class TimeSlot(val startTime: String, val endTime: String, val available: Boolean)

private data class BookingState(
    val lot           : ParkingLot? = null,
    val date          : String      = "",
    val startTime     : String      = "",
    val endTime       : String      = "",
    val vehicleSize   : VehicleSize = VehicleSize.STANDARD,
    val vehicleModel  : String      = "",
    val vehicleNumber : String      = "",
    val isPremium     : Boolean     = false,
    val totalAmount   : Double      = 0.0,
)

// ═══════════════════════════════════════════════════════════════════════════════
// SAMPLE DATA
// ═══════════════════════════════════════════════════════════════════════════════
private val sampleLots = listOf(
    ParkingLot("1","Phoenix Mall Parking","Nagar Road, Pune","0.3 km","4 min",4.5f,1240,60.0,350,42,true,true,true,true,true,true,listOf("Valet","EV Charging","Car Wash","CCTV","WiFi"),0.25f,0.35f,28),
    ParkingLot("2","City Center Park","MG Road, Pune","0.7 km","9 min",4.1f,856,40.0,120,18,false,false,false,false,false,true,listOf("CCTV","24/7 Security"),0.55f,0.25f,85),
    ParkingLot("3","Green Plaza Parking","FC Road, Pune","1.1 km","14 min",4.3f,432,50.0,200,67,true,true,false,true,false,true,listOf("CCTV","Covered","Disabled Access"),0.70f,0.45f,33),
    ParkingLot("4","Metro Station P&R","Shivajinagar","1.4 km","18 min",3.9f,2100,30.0,500,210,false,false,true,true,false,true,listOf("EV Charging","CCTV","24/7"),0.40f,0.65f,58),
    ParkingLot("5","The Grand Hotel","Koregaon Park","1.8 km","22 min",4.7f,318,80.0,80,12,true,true,true,false,true,true,listOf("Valet","EV","Car Wash","Lounge Access","WiFi"),0.80f,0.70f,85),
    ParkingLot("6","Street Smart Park","Camp, Pune","2.0 km","25 min",3.7f,145,25.0,60,35,false,false,false,false,false,false,listOf("Open Air"),0.15f,0.60f,42),
)

private val timeSlots = listOf(
    TimeSlot("6:00 AM","7:00 AM",true),   TimeSlot("7:00 AM","8:00 AM",false),
    TimeSlot("8:00 AM","9:00 AM",false),  TimeSlot("9:00 AM","10:00 AM",true),
    TimeSlot("10:00 AM","11:00 AM",true), TimeSlot("11:00 AM","12:00 PM",true),
    TimeSlot("12:00 PM","1:00 PM",false), TimeSlot("1:00 PM","2:00 PM",true),
    TimeSlot("2:00 PM","3:00 PM",true),   TimeSlot("3:00 PM","4:00 PM",true),
    TimeSlot("4:00 PM","5:00 PM",false),  TimeSlot("5:00 PM","6:00 PM",true),
    TimeSlot("6:00 PM","7:00 PM",true),   TimeSlot("7:00 PM","8:00 PM",true),
)

// ═══════════════════════════════════════════════════════════════════════════════
// ✅ MAIN SCREEN — signature matches NavGraph exactly
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(


    onNavigateToDetail   : (lotId: String) -> Unit,
    onNavigateToSearch   : () -> Unit,
    onNavigateToProfile  : () -> Unit,
    onNavigateToBookings : () -> Unit,
    onLogout             : () -> Unit,
) {
    var selectedLot    by remember { mutableStateOf<ParkingLot?>(null) }
    var showBooking    by remember { mutableStateOf(false) }
    var showDetail     by remember { mutableStateOf(false) }
    var showSearch     by remember { mutableStateOf(false) }
    var bookingState   by remember { mutableStateOf(BookingState()) }
    var bookingStep    by remember { mutableStateOf(0) }
    var selectedFilter by remember { mutableStateOf(SlotFilter.ALL) }
    var selectedSort   by remember { mutableStateOf(SortOption.DISTANCE) }
    var searchQuery    by remember { mutableStateOf("") }
    var mapExpanded    by remember { mutableStateOf(false) }
    var isDark by remember { mutableStateOf(false) }

    val filteredLots = remember(selectedFilter, selectedSort, searchQuery) {
        sampleLots
            .filter { lot ->
                when (selectedFilter) {
                    SlotFilter.ALL      -> true
                    SlotFilter.NEARBY   -> lot.distance.replace(" km", "").toFloat() < 1.0f
                    SlotFilter.CHEAPEST -> lot.pricePerHour < 40.0
                    SlotFilter.PREMIUM  -> lot.isPremium
                    SlotFilter.COVERED  -> lot.isCovered
                    SlotFilter.EV       -> lot.hasEV
                    SlotFilter.OPEN24H  -> lot.is24Hours
                }
            }
            .filter {
                searchQuery.isEmpty() ||
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.address.contains(searchQuery, ignoreCase = true)
            }
            .sortedWith(
                when (selectedSort) {
                    SortOption.DISTANCE   -> compareBy { it.distance.replace(" km", "").toFloat() }
                    SortOption.PRICE_LOW  -> compareBy { it.pricePerHour }
                    SortOption.PRICE_HIGH -> compareByDescending { it.pricePerHour }
                    SortOption.RATING     -> compareByDescending { it.rating }
                    SortOption.AVAILABILITY -> compareByDescending { it.available }
                }
            )
    }

    Box(modifier = Modifier.fillMaxSize().background(PV.Background)) {

        if (!showBooking && !showDetail && !showSearch) {
            // ── MAIN HOME ──────────────────────────────────────────────────────
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                item { HomeHeader(onSearchClick = { showSearch = true }, onProfileClick = onNavigateToProfile, isDark = isDark, onToggleDark = { isDark = !isDark }) }

                item {
                    MapSection(
                        lots      = sampleLots,
                        expanded  = mapExpanded,
                        onToggle  = { mapExpanded = !mapExpanded },
                        onLotClick = { lot -> selectedLot = lot; showDetail = true }
                    )
                }

                item { QuickStatsBar() }

                item {
                    FilterSortRow(
                        selectedFilter = selectedFilter,
                        selectedSort   = selectedSort,
                        onFilterChange = { selectedFilter = it },
                        onSortChange   = { selectedSort   = it },
                    )
                }

                item {
                    Text(
                        "${filteredLots.size} parking lots found",
                        fontSize = 13.sp,
                        color    = PV.TextSecondary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // ✅ FIX 1: key added for performance
                items(filteredLots, key = { it.id }) { lot ->
                    ParkingLotCard(
                        lot     = lot,
                        onClick = { selectedLot = lot; showDetail = true }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        // ── SEARCH OVERLAY ─────────────────────────────────────────────────────
        if (showSearch) {
            SearchScreen(
                query     = searchQuery,
                onQuery   = { searchQuery = it },
                lots      = filteredLots,
                onLotClick = { lot ->
                    selectedLot = lot
                    showSearch  = false
                    showDetail  = true
                },
                onBack = { showSearch = false }
            )
        }

        // ── LOT DETAIL ─────────────────────────────────────────────────────────
        if (showDetail && selectedLot != null) {
            LotDetailScreen(
                lot    = selectedLot!!,
                onBook = {
                    bookingState = BookingState(lot = selectedLot)
                    bookingStep  = 0
                    showDetail   = false
                    showBooking  = true
                },
                onBack = { showDetail = false }
            )
        }

        // ── BOOKING FLOW ───────────────────────────────────────────────────────
        if (showBooking) {
            BookingFlow(
                state   = bookingState,
                step    = bookingStep,
                onState = { bookingState = it },
                onStep  = { bookingStep  = it },
                onDone  = { showBooking  = false; bookingStep = 0 },
                onBack  = {
                    if (bookingStep == 0) { showBooking = false; showDetail = true }
                    else bookingStep--
                }
            )
        }

        // Bottom nav is now handled by UserMainScreen wrapper
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// HOME HEADER
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun HomeHeader(
    onSearchClick : () -> Unit,
    onProfileClick: () -> Unit,
    isDark        : Boolean,
    onToggleDark  : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PV.Primary)
            .statusBarsPadding()   // ✅ FIX 3: edge-to-edge safe
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 LEFT SIDE
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, null, tint = PV.OnPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Koregaon Park, Pune",
                        fontSize = 13.sp,
                        color = PV.OnPrimary.copy(0.85f),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(Icons.Filled.KeyboardArrowDown, null, tint = PV.OnPrimary.copy(0.85f), modifier = Modifier.size(16.dp))
                }

                Text(
                    "Find Parking",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = PV.OnPrimary
                )

                Text(
                    "Near you · ${sampleLots.size} lots available",
                    fontSize = 13.sp,
                    color = PV.OnPrimary.copy(0.75f)
                )
            }

            // 🔹 RIGHT SIDE (FIXED POSITION)
            Row(verticalAlignment = Alignment.CenterVertically) {

                // 🌙 DARK MODE
                IconButton(
                    onClick = onToggleDark,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = PV.OnPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 🔔 NOTIFICATION
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = PV.Error) {
                                Text("3", fontSize = 9.sp)
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.Notifications, null, tint = PV.OnPrimary)
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 👤 PROFILE
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PV.OnPrimary.copy(0.2f), CircleShape)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("RK", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = PV.OnPrimary)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = PV.Surface,
            shadowElevation = 4.dp,
            onClick = onSearchClick
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Search,
                    null,
                    tint = PV.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Search parking lots, areas...", fontSize = 14.sp, color = PV.TextTertiary)
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DUMMY MAP SECTION
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun MapSection(
    lots: List<ParkingLot>,
    expanded: Boolean,
    onToggle: () -> Unit,
    onLotClick: (ParkingLot) -> Unit,
) {
    val mapHeight by animateDpAsState(
        targetValue = if (expanded) 380.dp else 220.dp,
        animationSpec = tween(350, easing = EaseInOutCubic),
        label = "mapH"
    )

    Card(
        modifier = Modifier.fillMaxWidth().height(mapHeight)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PV.MapBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            DummyMapCanvas(lots = lots, onLotClick = onLotClick)

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PV.Surface.copy(0.92f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val pulse by rememberInfiniteTransition(label = "p").animateFloat(
                            1f,
                            0.3f,
                            infiniteRepeatable(tween(900), RepeatMode.Reverse),
                            label = "dot"
                        )
                        Box(
                            modifier = Modifier.size(7.dp)
                                .background(PV.Success.copy(pulse), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            "Live Map",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PV.TextPrimary
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MapIconBtn(Icons.Outlined.MyLocation, "My location")
                    MapIconBtn(
                        if (expanded) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                        "Expand",
                        onToggle
                    )
                }
            }

            Box(modifier = Modifier.align(Alignment.Center).offset(y = 20.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PV.Primary,
                    shadowElevation = 3.dp
                ) {
                    Text(
                        "📍 You",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PV.OnPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.align(Alignment.BottomStart).padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(
                    PV.Success to "Available",
                    PV.Warning to "Filling",
                    PV.Error to "Full"
                ).forEach { (c, l) ->
                    Surface(shape = RoundedCornerShape(6.dp), color = PV.Surface.copy(0.9f)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(7.dp).background(c, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(l, fontSize = 10.sp, color = PV.TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DummyMapCanvas(lots: List<ParkingLot>, onLotClick: (ParkingLot) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(PV.MapBg)) {
        listOf(0.3f, 0.55f, 0.75f).forEach { y ->
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).offset(y = Dp(y * 300))) {
                Box(modifier = Modifier.fillMaxSize().background(PV.MapRoad))
            }
        }
        listOf(0.25f, 0.5f, 0.75f).forEach { x ->
            Box(modifier = Modifier.fillMaxHeight().width(8.dp).offset(x = Dp(x * 360))) {
                Box(modifier = Modifier.fillMaxSize().background(PV.MapRoad))
            }
        }
        lots.forEach { lot ->
            val pinColor = when {
                lot.occupancyPct > 80 -> PV.Error
                lot.occupancyPct > 50 -> PV.Warning
                else -> PV.Success
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = Dp(lot.mapX * 320 + 20), y = Dp(lot.mapY * 180 + 20))
                    .background(PV.Surface, CircleShape)
                    .border(2.dp, pinColor, CircleShape)
                    .clickable { onLotClick(lot) },
                contentAlignment = Alignment.Center
            ) {
                Text("P", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = pinColor)
            }
        }
    }
}

@Composable
private fun MapIconBtn(icon: ImageVector, desc: String, onClick: () -> Unit = {}) {
    Surface(
        shape = CircleShape,
        color = PV.Surface.copy(0.92f),
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        Icon(
            icon,
            contentDescription = desc,
            tint = PV.TextPrimary,
            modifier = Modifier.padding(8.dp).size(18.dp)
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// QUICK STATS BAR
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun QuickStatsBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf(
            Triple("🅿", "${sampleLots.sumOf { it.available }}", "Free Slots"),
            Triple("💰", "₹25/hr", "Starts From"),
            Triple("⚡", "${sampleLots.count { it.hasEV }}", "EV Stations"),
            Triple("⭐", "${sampleLots.count { it.isPremium }}", "Premium"),
        ).forEach { (emoji, value, label) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(emoji, fontSize = 18.sp)
                Text(
                    value,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = PV.TextPrimary
                )
                Text(label, fontSize = 10.sp, color = PV.TextSecondary)
            }
        }
    }
    Divider(color = PV.Divider, modifier = Modifier.padding(horizontal = 16.dp))
}

// ═══════════════════════════════════════════════════════════════════════════════
// FILTER + SORT
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun FilterSortRow(
    selectedFilter: SlotFilter,
    selectedSort: SortOption,
    onFilterChange: (SlotFilter) -> Unit,
    onSortChange: (SortOption) -> Unit,
) {
    var showSortSheet by remember { mutableStateOf(false) }

    Column {
        // ✅ FIX 4: .values() → .entries
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SlotFilter.entries, key = { it.name }) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { onFilterChange(filter) },
                    label = { Text(filter.label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PV.Primary, selectedLabelColor = PV.OnPrimary,
                        containerColor = PV.Surface, labelColor = PV.TextSecondary,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true, selected = selectedFilter == filter,
                        borderColor = PV.Border, selectedBorderColor = PV.PrimaryDark,
                    )
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Nearby Parking",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = PV.TextPrimary
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PV.SurfaceVariant,
                onClick = { showSortSheet = true }) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Sort,
                        null,
                        tint = PV.TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(selectedSort.label, fontSize = 12.sp, color = PV.TextSecondary)
                }
            }
        }
    }

    if (showSortSheet) {
        SortBottomSheet(
            selected = selectedSort,
            onSelect = { onSortChange(it); showSortSheet = false },
            onDismiss = { showSortSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheet(
    selected: SortOption,
    onSelect: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = PV.Surface) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Sort By", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PV.TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            // ✅ FIX 5: .values() → .entries
            SortOption.entries.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSelect(option) }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        option.label, fontSize = 15.sp,
                        color = if (selected == option) PV.PrimaryDark else PV.TextPrimary,
                        fontWeight = if (selected == option) FontWeight.Bold else FontWeight.Normal
                    )
                    if (selected == option) Icon(Icons.Filled.Check, null, tint = PV.Primary)
                }
                Divider(color = PV.Divider)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// PARKING LOT CARD
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun ParkingLotCard(lot: ParkingLot, onClick: () -> Unit) {
    val availPct = lot.available.toFloat() / lot.totalSlots
    val avColor = when {
        availPct < 0.15f -> PV.Error
        availPct < 0.4f -> PV.Warning
        else -> PV.Success
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PV.Surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            if (lot.isPremium) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(PV.Purple, PV.PrimaryDark)),
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        "⭐ Premium Parking · Valet Available",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PV.Surface
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            lot.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = PV.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                null,
                                tint = PV.TextTertiary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                lot.address,
                                fontSize = 12.sp,
                                color = PV.TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "₹${lot.pricePerHour.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = PV.PrimaryDark
                        )
                        Text("/hour", fontSize = 11.sp, color = PV.TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.NearMe,
                            null,
                            tint = PV.TextTertiary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            "${lot.distance} · ${lot.walkTime} walk",
                            fontSize = 12.sp,
                            color = PV.TextSecondary
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            null,
                            tint = PV.Primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            "${lot.rating} (${lot.reviews})",
                            fontSize = 12.sp,
                            color = PV.TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { availPct },
                        modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = avColor,
                        trackColor = PV.SurfaceDim,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "${lot.available}/${lot.totalSlots}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = avColor
                    )
                    Text(" free", fontSize = 12.sp, color = PV.TextSecondary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    lot.amenities.take(4).forEach { amenity ->
                        Text(
                            amenity,
                            modifier = Modifier.background(
                                PV.SurfaceVariant,
                                RoundedCornerShape(6.dp)
                            ).padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp, color = PV.TextSecondary
                        )
                    }
                    if (lot.amenities.size > 4) {
                        Text(
                            "+${lot.amenities.size - 4}", fontSize = 10.sp, color = PV.TextTertiary,
                            modifier = Modifier.background(
                                PV.SurfaceVariant,
                                RoundedCornerShape(6.dp)
                            ).padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }


            }
        }
    }
}


// ═══════════════════════════════════════════════════════════════════════════════
// SEARCH SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreen(
    query: String,
    onQuery: (String) -> Unit,
    lots: List<ParkingLot>,
    onLotClick: (ParkingLot) -> Unit,
    onBack: () -> Unit,
) {
    val recentSearches = listOf("Phoenix Mall", "FC Road", "Koregaon Park", "Shivajinagar")
    val popularAreas = listOf("Camp", "Baner", "Hinjewadi", "Kothrud", "Wakad")

    Column(modifier = Modifier.fillMaxSize().background(PV.Background)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(PV.Surface).statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, "Back", tint = PV.TextPrimary)
            }
            OutlinedTextField(
                value = query,
                onValueChange = onQuery,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Search parking lots, areas...",
                        fontSize = 14.sp,
                        color = PV.TextTertiary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PV.Primary,
                    unfocusedBorderColor = PV.Border
                ),
                leadingIcon = { Icon(Icons.Outlined.Search, null, tint = PV.TextSecondary) },
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { onQuery("") }) {
                            Icon(Icons.Filled.Close, null, tint = PV.TextSecondary)
                        }
                    }
                } else null
            )
        }
        Divider(color = PV.Divider)

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (query.isEmpty()) {
                item {
                    Text(
                        "Recent Searches",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PV.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    recentSearches.forEach { search ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { onQuery(search) }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.History,
                                null,
                                tint = PV.TextTertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(search, fontSize = 14.sp, color = PV.TextPrimary)
                        }
                        Divider(color = PV.Divider)
                    }
                }
                item {
                    Text(
                        "Popular Areas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PV.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(popularAreas, key = { it }) { area ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PV.PrimaryLight,
                                border = BorderStroke(1.dp, PV.PrimaryContainer),
                                onClick = { onQuery(area) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 14.dp,
                                        vertical = 8.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Outlined.LocationOn,
                                        null,
                                        tint = PV.PrimaryDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        area,
                                        fontSize = 13.sp,
                                        color = PV.PrimaryDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // ✅ FIX 6: key added
                items(lots, key = { it.id }) { lot ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { onLotClick(lot) }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp)
                                .background(PV.PrimaryLight, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "P",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = PV.PrimaryDark
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                lot.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = PV.TextPrimary
                            )
                            Text(
                                "${lot.address} · ${lot.distance}",
                                fontSize = 12.sp,
                                color = PV.TextSecondary
                            )
                        }
                        Text(
                            "₹${lot.pricePerHour.toInt()}/hr",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = PV.PrimaryDark
                        )
                    }
                    Divider(color = PV.Divider)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// LOT DETAIL SCREEN
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun LotDetailScreen(lot: ParkingLot, onBook: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(PV.Background)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp)
                .background(Brush.verticalGradient(listOf(PV.MapBlue, PV.MapBlueDark)))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🅿", fontSize = 64.sp)
                Text(lot.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PV.Surface)
            }
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(PV.Overlay, CircleShape)
                ) {
                    Icon(Icons.Filled.ArrowBack, "Back", tint = PV.Surface)
                }
                IconButton(onClick = {}, modifier = Modifier.background(PV.Overlay, CircleShape)) {
                    Icon(Icons.Outlined.FavoriteBorder, "Save", tint = PV.Surface)
                }
            }
            if (lot.isPremium) {
                Surface(
                    modifier = Modifier.align(Alignment.BottomStart).padding(14.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = PV.Purple
                ) {
                    Text(
                        "⭐ Premium",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PV.Surface,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                lot.name,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = PV.TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Outlined.LocationOn,
                                    null,
                                    tint = PV.TextTertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(lot.address, fontSize = 13.sp, color = PV.TextSecondary)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "₹${lot.pricePerHour.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 26.sp,
                                color = PV.PrimaryDark
                            )
                            Text("/hour", fontSize = 12.sp, color = PV.TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailStat("⭐", "${lot.rating}", "${lot.reviews} reviews")
                        DetailStat("📍", lot.distance, lot.walkTime + " walk")
                        DetailStat("🅿", "${lot.available}", "Available now")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = PV.Divider)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Amenities",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PV.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val amenityIcons = mapOf(
                        "Valet" to "🚗", "EV Charging" to "⚡", "Car Wash" to "🚿",
                        "CCTV" to "📷", "WiFi" to "📶", "Covered" to "🏠",
                        "24/7 Security" to "🛡", "24/7" to "🕐", "Open Air" to "🌤",
                        "Disabled Access" to "♿", "Lounge Access" to "☕"
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(lot.amenities, key = { it }) { amenity ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier.size(52.dp)
                                        .background(PV.PrimaryLight, RoundedCornerShape(13.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(amenityIcons[amenity] ?: "✓", fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    amenity,
                                    fontSize = 10.sp,
                                    color = PV.TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(60.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = PV.Divider)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Pricing",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PV.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            "Hourly" to "₹${lot.pricePerHour.toInt()}",
                            "Daily" to "₹${(lot.pricePerHour * 8).toInt()}",
                            "Monthly" to "₹${(lot.pricePerHour * 200).toInt()}",
                        ).forEach { (label, price) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(label, fontSize = 14.sp, color = PV.TextSecondary)
                                Text(
                                    price,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PV.TextPrimary
                                )
                            }
                        }
                    }

                    if (lot.isPremium) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = PV.PurpleLight)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    "⭐ Premium Benefits",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = PV.Purple
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                listOf(
                                    "Priority slot reservation",
                                    "Free valet parking",
                                    "Car wash included",
                                    "Lounge access while waiting",
                                    "24/7 dedicated support"
                                ).forEach {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Check,
                                            null,
                                            tint = PV.Purple,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(it, fontSize = 13.sp, color = PV.TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(shadowElevation = 8.dp, color = PV.Surface) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    border = BorderStroke(1.5.dp, PV.Primary)
                ) {
                    Icon(
                        Icons.Outlined.Navigation,
                        null,
                        tint = PV.PrimaryDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Navigate", fontWeight = FontWeight.Bold, color = PV.PrimaryDark)
                }
                Button(
                    onClick = onBook,
                    modifier = Modifier.weight(2f).height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PV.Primary,
                        contentColor = PV.OnPrimary
                    )
                ) {
                    Text("Book Parking Slot", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun DetailStat(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 20.sp)
        Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PV.TextPrimary)
        Text(label, fontSize = 11.sp, color = PV.TextSecondary)
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// BOOKING FLOW
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingFlow(
    state: BookingState,
    step: Int,
    onState: (BookingState) -> Unit,
    onStep: (Int) -> Unit,
    onDone: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().background(PV.Background)) {
        BookingProgressHeader(step = step, lot = state.lot, onBack = onBack)
        when (step) {
            0 -> StepTimeSlot(state = state, onNext = { onState(it); onStep(1) })
            1 -> StepVehicleInfo(state = state, onNext = { onState(it); onStep(2) })
            2 -> StepConfirm(state = state, onNext = { onState(it); onStep(3) })
            3 -> StepSuccess(state = state, onDone = onDone)
        }
    }
}

@Composable
private fun BookingProgressHeader(step: Int, lot: ParkingLot?, onBack: () -> Unit) {
    val steps = listOf("Time Slot", "Vehicle", "Confirm", "Done")
    Column(modifier = Modifier.fillMaxWidth().background(PV.Surface)) {
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Back",
                    tint = PV.TextPrimary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Book Parking",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PV.TextPrimary
                )
                Text(lot?.name ?: "", fontSize = 12.sp, color = PV.TextSecondary)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            steps.forEachIndexed { index, label ->
                val active = index == step
                val completed = index < step
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(28.dp).background(
                            when {
                                completed -> PV.Success; active -> PV.Primary; else -> PV.SurfaceDim
                            },
                            CircleShape
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (completed) Icon(
                            Icons.Filled.Check,
                            null,
                            tint = PV.Surface,
                            modifier = Modifier.size(14.dp)
                        )
                        else Text(
                            "${index + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold,
                            color = if (active) PV.OnPrimary else PV.TextTertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        label,
                        fontSize = 10.sp,
                        color = if (active) PV.PrimaryDark else PV.TextTertiary,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                    )
                }
                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier.weight(0.5f).height(2.dp)
                            .background(if (completed) PV.Success else PV.SurfaceDim)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
        Divider(color = PV.Divider)
    }
}

@Composable
private fun StepTimeSlot(state: BookingState, onNext: (BookingState) -> Unit) {
    var selectedStart by remember { mutableStateOf("") }
    var selectedEnd by remember { mutableStateOf("") }
    val dates = listOf("Today", "Tomorrow", "Wed 24", "Thu 25", "Fri 26")
    var selectedDate by remember { mutableStateOf(dates[0]) }

    Column(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    "Select Date",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PV.TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(dates, key = { it }) { date ->
                        val sel = selectedDate == date

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (sel) PV.Primary else PV.Surface,
                            border = BorderStroke(1.dp, if (sel) PV.PrimaryDark else PV.Border),
                            onClick = { selectedDate = date },
                            shadowElevation = if (sel) 3.dp else 1.dp
                        ) {
                            Text(
                                date,
                                fontSize = 13.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                color = if (sel) PV.OnPrimary else PV.TextPrimary,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Available Time Slots",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PV.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Green = available · Grey = booked",
                    fontSize = 12.sp,
                    color = PV.TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                // ✅ FIXED GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)   // 🔥 FIXED
                ) {

                    items(timeSlots, key = { it.startTime }) { slot ->

                        val isSelected = selectedStart == slot.startTime

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),   // 🔥 FIXED SIZE

                            shape = RoundedCornerShape(10.dp),

                            color = when {
                                isSelected -> PV.Primary
                                !slot.available -> PV.SurfaceDim
                                else -> PV.SuccessLight
                            },

                            border = BorderStroke(
                                1.dp,
                                when {
                                    isSelected -> PV.PrimaryDark
                                    !slot.available -> PV.Border
                                    else -> PV.Success.copy(0.4f)
                                }
                            ),

                            onClick = {
                                if (slot.available) {
                                    selectedStart = slot.startTime
                                    selectedEnd = slot.endTime
                                }
                            }
                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize(),   // 🔥 FIXED
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    slot.startTime,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isSelected -> PV.OnPrimary
                                        !slot.available -> PV.TextTertiary
                                        else -> PV.Success
                                    }
                                )

                                Text(
                                    slot.endTime,
                                    fontSize = 10.sp,
                                    color = when {
                                        isSelected -> PV.OnPrimary.copy(0.8f)
                                        !slot.available -> PV.TextTertiary
                                        else -> PV.TextSecondary
                                    }
                                )

                                if (!slot.available) {
                                    Text("Booked", fontSize = 9.sp, color = PV.TextTertiary)
                                }
                            }
                        }
                    }
                }
            }
        }
        BookingNavButton(
            enabled = selectedStart.isNotEmpty(),
            label = "Continue → Vehicle Info",
            onClick = {
                onNext(
                    state.copy(
                        date = selectedDate,
                        startTime = selectedStart,
                        endTime = selectedEnd
                    )
                )
            }
        )
    }
}

@Composable
private fun StepVehicleInfo(state: BookingState, onNext: (BookingState) -> Unit) {
    var vehicleSize by remember { mutableStateOf(state.vehicleSize) }
    var vehicleModel by remember { mutableStateOf(state.vehicleModel) }
    var vehicleNumber by remember { mutableStateOf(state.vehicleNumber) }
    var isPremium by remember { mutableStateOf(state.isPremium) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Vehicle Size",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PV.TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                // ✅ FIX 7: .values() → .entries
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(130.dp)
                ) {
                    items(VehicleSize.entries, key = { it.name }) { size ->
                        val sel = vehicleSize == size
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (sel) PV.Primary else PV.Surface,
                            border = BorderStroke(1.5.dp, if (sel) PV.PrimaryDark else PV.Border),
                            onClick = { vehicleSize = size },
                            shadowElevation = if (sel) 3.dp else 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(size.emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    size.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sel) PV.OnPrimary else PV.TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    "Vehicle Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PV.TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = vehicleModel,
                    onValueChange = { vehicleModel = it },
                    label = { Text("Vehicle Model (e.g. Honda City)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PV.Primary,
                        focusedLabelColor = PV.PrimaryDark
                    ),
                    leadingIcon = { Text("🚗", modifier = Modifier.padding(start = 8.dp)) })
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it.uppercase() },
                    label = { Text("Vehicle Number (e.g. MH12AB1234)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PV.Primary,
                        focusedLabelColor = PV.PrimaryDark
                    ),
                    leadingIcon = { Text("🔢", modifier = Modifier.padding(start = 8.dp)) })
            }
            if (state.lot?.isPremium == true) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isPremium) PV.PurpleLight else PV.Surface),
                        border = BorderStroke(1.5.dp, if (isPremium) PV.Purple else PV.Border)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Upgrade to Premium",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = PV.Purple
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                listOf(
                                    "Valet parking included",
                                    "Priority slot",
                                    "Car wash",
                                    "Lounge access"
                                ).forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.Check,
                                            null,
                                            tint = PV.Purple,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(it, fontSize = 12.sp, color = PV.TextSecondary)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    "+₹150 extra",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PV.Purple
                                )
                            }
                            Switch(
                                checked = isPremium, onCheckedChange = { isPremium = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PV.Surface,
                                    checkedTrackColor = PV.Purple
                                )
                            )
                        }
                    }
                }
            }
        }
        BookingNavButton(
            enabled = vehicleModel.isNotEmpty() && vehicleNumber.isNotEmpty(),
            label = "Continue → Confirm Booking",
            onClick = {
                onNext(
                    state.copy(
                        vehicleSize = vehicleSize,
                        vehicleModel = vehicleModel,
                        vehicleNumber = vehicleNumber,
                        isPremium = isPremium
                    )
                )
            }
        )
    }
}

@Composable
private fun StepConfirm(state: BookingState, onNext: (BookingState) -> Unit) {
    val hours = 1.0
    val base = (state.lot?.pricePerHour ?: 0.0) * hours
    val premium = if (state.isPremium) 150.0 else 0.0
    val gst = (base + premium) * 0.18
    val total = base + premium + gst

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PV.Surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Booking Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PV.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        listOf(
                            "Parking Lot" to (state.lot?.name ?: ""),
                            "Date" to state.date,
                            "Time" to "${state.startTime} – ${state.endTime}",
                            "Vehicle" to "${state.vehicleModel} · ${state.vehicleNumber}",
                            "Size" to state.vehicleSize.label,
                            "Package" to if (state.isPremium) "⭐ Premium" else "Standard",
                        ).forEach { (k, v) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(k, fontSize = 13.sp, color = PV.TextSecondary)
                                Text(
                                    v,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PV.TextPrimary
                                )
                            }
                            Divider(color = PV.Divider)
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PV.Surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Bill Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = PV.TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        listOf(
                            "Parking (1 hr)" to "₹${base.toInt()}",
                            "Premium Add-on" to if (premium > 0) "₹${premium.toInt()}" else "—",
                            "GST (18%)" to "₹${gst.toInt()}"
                        ).forEach { (k, v) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(k, fontSize = 13.sp, color = PV.TextSecondary)
                                Text(v, fontSize = 13.sp, color = PV.TextPrimary)
                            }
                        }
                        Divider(color = PV.Divider, modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = PV.TextPrimary
                            )
                            Text(
                                "₹${total.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = PV.PrimaryDark
                            )
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PV.InfoLight)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ℹ️", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "You will receive notifications when your session starts and 15 minutes before it ends.",
                            fontSize = 12.sp,
                            color = PV.Info,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
        BookingNavButton(
            enabled = true,
            label = "Pay ₹${total.toInt()} & Confirm",
            onClick = { onNext(state.copy(totalAmount = total)) })
    }
}

@Composable
private fun StepSuccess(state: BookingState, onDone: () -> Unit) {
    // ✅ FIX 8: .random() wrapped in remember so it doesn't regenerate on recomposition
    val bookingId = remember { (100000..999999).random() }
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(300); show = true }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(visible = show, enter = scaleIn() + fadeIn()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(100.dp).background(PV.SuccessLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✅", fontSize = 48.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Booking Confirmed!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = PV.TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Your slot at ${state.lot?.name} is reserved for ${state.startTime}",
                    fontSize = 14.sp,
                    color = PV.TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = PV.PrimaryLight),
                    border = BorderStroke(1.dp, PV.PrimaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Booking ID", fontSize = 12.sp, color = PV.TextSecondary)
                        Text(
                            "#PV$bookingId",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = PV.PrimaryDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Show this ID at entry gate",
                            fontSize = 12.sp,
                            color = PV.TextSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PV.SuccessLight)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔔", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "You'll get a notification 15 min before your session ends. Auto-billing is enabled.",
                            fontSize = 12.sp,
                            color = PV.Success,
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PV.Primary,
                        contentColor = PV.OnPrimary
                    )
                ) {
                    Text("Back to Home", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// ✅ BOTTOM NAV — now receives click callbacks
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BottomNavBar(
    modifier: Modifier,
    onBookingsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Surface(modifier = modifier.fillMaxWidth(), shadowElevation = 12.dp, color = PV.Surface) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                Triple(Icons.Filled.Home, "Home", true),
                Triple(Icons.Outlined.BookOnline, "Bookings", false),
                Triple(Icons.Outlined.History, "History", false),
                Triple(Icons.Outlined.Person, "Profile", false),
            ).forEachIndexed { index, (icon, label, active) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        when (index) {
                            1 -> onBookingsClick()
                            3 -> onProfileClick()
                            else -> { /* Home / History — handle later */
                            }
                        }
                    }
                ) {
                    Icon(
                        icon,
                        label,
                        tint = if (active) PV.PrimaryDark else PV.TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        label,
                        fontSize = 10.sp,
                        color = if (active) PV.PrimaryDark else PV.TextTertiary,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                    )
                    if (active) Box(
                        modifier = Modifier.size(4.dp).background(PV.Primary, CircleShape)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHARED
// ═══════════════════════════════════════════════════════════════════════════════
@Composable
private fun BookingNavButton(enabled: Boolean, label: String, onClick: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = PV.Surface) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().height(58.dp)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PV.Primary,
                contentColor = PV.OnPrimary,
                disabledContainerColor = PV.SurfaceDim,
                disabledContentColor = PV.TextTertiary,
            )
        ) {
            Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        }
    }
}
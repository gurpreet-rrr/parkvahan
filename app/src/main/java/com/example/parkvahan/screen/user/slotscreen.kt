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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

// ─────────────────────────────────────────────────────────────────────────────
// TOKENS
// ─────────────────────────────────────────────────────────────────────────────
private object PSC {
    val Primary          = Color(0xFFFFC107)
    val PrimaryDark      = Color(0xFFFF8F00)
    val PrimaryLight     = Color(0xFFFFF8E1)
    val PrimaryContainer = Color(0xFFFFECB3)
    val OnPrimary        = Color(0xFF1A1A1A)

    // Light
    val BgLight          = Color(0xFFF8F9FA)
    val SurfaceLight     = Color(0xFFFFFFFF)
    val SurfaceVarLight  = Color(0xFFF5F5F5)
    val SurfaceDimLight  = Color(0xFFEEEEEE)
    val TextPrimLight    = Color(0xFF1A1A1A)
    val TextSecLight     = Color(0xFF6B7280)
    val TextTertLight    = Color(0xFFBDBDBD)
    val DividerLight     = Color(0xFFF3F4F6)
    val BorderLight      = Color(0xFFE5E7EB)

    // Dark
    val BgDark           = Color(0xFF0F1117)
    val SurfaceDark      = Color(0xFF1A1D27)
    val SurfaceVarDark   = Color(0xFF252836)
    val SurfaceDimDark   = Color(0xFF2E3147)
    val TextPrimDark     = Color(0xFFF0F2F5)
    val TextSecDark      = Color(0xFF9AA0AC)
    val TextTertDark     = Color(0xFF6B7280)
    val DividerDark      = Color(0xFF252836)
    val BorderDark       = Color(0xFF2E3147)

    // Status
    val Success          = Color(0xFF16A34A)
    val SuccessLight     = Color(0xFFDCFCE7)
    val SuccessDark      = Color(0xFF052E16)
    val Error            = Color(0xFFDC2626)
    val ErrorLight       = Color(0xFFFEE2E2)
    val ErrorDark        = Color(0xFF450A0A)
    val Warning          = Color(0xFFD97706)
    val WarningLight     = Color(0xFFFEF3C7)
    val WarningDark      = Color(0xFF451A03)
    val Info             = Color(0xFF2563EB)
    val InfoLight        = Color(0xFFDBEAFE)
    val InfoDark         = Color(0xFF1E3A5F)
    val Purple           = Color(0xFF7C3AED)
    val PurpleLight      = Color(0xFFEDE9FE)
}

// ─────────────────────────────────────────────────────────────────────────────
// THEME
// ─────────────────────────────────────────────────────────────────────────────
private data class PSTheme(
    val isDark     : Boolean,
    val bg         : Color,
    val surface    : Color,
    val surfaceVar : Color,
    val surfaceDim : Color,
    val textPrim   : Color,
    val textSec    : Color,
    val textTert   : Color,
    val divider    : Color,
    val border     : Color,
    val successBg  : Color,
    val errorBg    : Color,
    val warningBg  : Color,
    val infoBg     : Color,
)

private fun lightTheme() = PSTheme(
    isDark = false,
    bg = PSC.BgLight, surface = PSC.SurfaceLight, surfaceVar = PSC.SurfaceVarLight,
    surfaceDim = PSC.SurfaceDimLight, textPrim = PSC.TextPrimLight, textSec = PSC.TextSecLight,
    textTert = PSC.TextTertLight, divider = PSC.DividerLight, border = PSC.BorderLight,
    successBg = PSC.SuccessLight, errorBg = PSC.ErrorLight, warningBg = PSC.WarningLight, infoBg = PSC.InfoLight,
)

private fun darkTheme() = PSTheme(
    isDark = true,
    bg = PSC.BgDark, surface = PSC.SurfaceDark, surfaceVar = PSC.SurfaceVarDark,
    surfaceDim = PSC.SurfaceDimDark, textPrim = PSC.TextPrimDark, textSec = PSC.TextSecDark,
    textTert = PSC.TextTertDark, divider = PSC.DividerDark, border = PSC.BorderDark,
    successBg = PSC.SuccessDark, errorBg = PSC.ErrorDark, warningBg = PSC.WarningDark, infoBg = PSC.InfoDark,
)

// ─────────────────────────────────────────────────────────────────────────────
// MODELS
// ─────────────────────────────────────────────────────────────────────────────
private enum class ParkingSortOption(val label: String) {
    DISTANCE("Distance"),
    PRICE_LOW("Price: Low → High"),
    PRICE_HIGH("Price: High → Low"),
    RATING("Rating"),
    AVAILABILITY("Most Available"),
}

private enum class ParkingFilter(val label: String) {
    ALL("All"),
    NEARBY("Nearby"),
    CHEAPEST("Cheapest"),
    PREMIUM("Premium"),
    COVERED("Covered"),
    EV("EV Charging"),
    OPEN24H("24/7 Open"),
}

private data class SlotInfo(
    val id          : String,
    val number      : String,
    val type        : String,
    val emoji       : String,
    val status      : SlotStatus,
    val pricePerHour: Double,
    val vehicle     : String? = null,
    val driver      : String? = null,
    val entryTime   : String? = null,
    val duration    : String? = null,
)

private enum class SlotStatus(val label: String) {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    RESERVED("Reserved"),
    MAINTENANCE("Maintenance"),
}

private data class ParkingLotDetail(
    val id          : String,
    val name        : String,
    val address     : String,
    val distance    : String,
    val walkTime    : String,
    val rating      : Float,
    val reviews     : Int,
    val pricePerHour: Double,
    val totalSlots  : Int,
    val available   : Int,
    val isPremium   : Boolean      = false,
    val isCovered   : Boolean      = false,
    val hasEV       : Boolean      = false,
    val is24Hours   : Boolean      = false,
    val amenities   : List<String> = emptyList(),
    val slots       : List<SlotInfo> = emptyList(),
)

// ─────────────────────────────────────────────────────────────────────────────
// SAMPLE DATA
// ─────────────────────────────────────────────────────────────────────────────
private val sampleParkingLots = listOf(
    ParkingLotDetail(
        id = "1", name = "Phoenix Mall Parking", address = "Nagar Road, Pune",
        distance = "0.3 km", walkTime = "4 min", rating = 4.5f, reviews = 1240,
        pricePerHour = 60.0, totalSlots = 12, available = 5,
        isPremium = true, isCovered = true, hasEV = true, is24Hours = true,
        amenities = listOf("Valet", "EV Charging", "Car Wash", "CCTV", "WiFi"),
        slots = listOf(
            SlotInfo("s1","A-01","Standard","🚗",SlotStatus.OCCUPIED,  60.0,"DL01AB1234","Rahul Kumar","10:30 AM","2h 15m"),
            SlotInfo("s2","A-02","Compact", "🚙",SlotStatus.AVAILABLE, 40.0),
            SlotInfo("s3","A-03","EV",      "⚡",SlotStatus.OCCUPIED,  80.0,"DL02CD5678","Priya Singh","09:15 AM","3h 30m"),
            SlotInfo("s4","A-04","Standard","🚗",SlotStatus.AVAILABLE, 60.0),
            SlotInfo("s5","A-05","VIP",     "⭐",SlotStatus.RESERVED,  150.0,"DL03EF9012","Amit Sharma","02:00 PM",null),
            SlotInfo("s6","B-01","Large",   "🚐",SlotStatus.OCCUPIED,  70.0,"DL04GH3456","Sneha Gupta","11:00 AM","1h 45m"),
            SlotInfo("s7","B-02","Disabled","♿",SlotStatus.AVAILABLE, 30.0),
            SlotInfo("s8","B-03","Standard","🚗",SlotStatus.OCCUPIED,  60.0,"DL05IJ7890","Vikram","08:45 AM","4h"),
            SlotInfo("s9","B-04","Standard","🚗",SlotStatus.MAINTENANCE,60.0),
            SlotInfo("s10","B-05","EV",     "⚡",SlotStatus.AVAILABLE, 80.0),
            SlotInfo("s11","C-01","Compact","🚙",SlotStatus.OCCUPIED,  40.0,"DL06KL2345","Neha","12:00 PM","45m"),
            SlotInfo("s12","C-02","Standard","🚗",SlotStatus.AVAILABLE,60.0),
        )
    ),
    ParkingLotDetail(
        id = "2", name = "City Center Park", address = "MG Road, Pune",
        distance = "0.7 km", walkTime = "9 min", rating = 4.1f, reviews = 856,
        pricePerHour = 40.0, totalSlots = 8, available = 2,
        isPremium = false, isCovered = false, hasEV = false, is24Hours = false,
        amenities = listOf("CCTV", "24/7 Security"),
        slots = listOf(
            SlotInfo("s13","A-01","Standard","🚗",SlotStatus.OCCUPIED,  40.0,"DL07MN3456","Ravi","07:30 AM","5h"),
            SlotInfo("s14","A-02","Standard","🚗",SlotStatus.AVAILABLE, 40.0),
            SlotInfo("s15","A-03","Standard","🚗",SlotStatus.OCCUPIED,  40.0,"DL08OP4567","Sita","01:15 PM","1h"),
            SlotInfo("s16","A-04","Standard","🚗",SlotStatus.OCCUPIED,  40.0,"DL09QR5678","Raj","03:00 PM","30m"),
            SlotInfo("s17","B-01","Large",   "🚐",SlotStatus.OCCUPIED,  55.0,"DL10ST9012","Anita","09:00 AM","3h"),
            SlotInfo("s18","B-02","Standard","🚗",SlotStatus.AVAILABLE, 40.0),
            SlotInfo("s19","B-03","Standard","🚗",SlotStatus.OCCUPIED,  40.0,"DL11UV3456","Mohan","11:30 AM","2h"),
            SlotInfo("s20","B-04","Standard","🚗",SlotStatus.OCCUPIED,  40.0,"DL12WX7890","Geeta","02:45 PM","45m"),
        )
    ),
    ParkingLotDetail(
        id = "3", name = "Green Plaza Parking", address = "FC Road, Pune",
        distance = "1.1 km", walkTime = "14 min", rating = 4.3f, reviews = 432,
        pricePerHour = 50.0, totalSlots = 10, available = 6,
        isPremium = true, isCovered = true, hasEV = false, is24Hours = true,
        amenities = listOf("CCTV", "Covered", "Disabled Access"),
        slots = listOf(
            SlotInfo("s21","A-01","Standard","🚗",SlotStatus.AVAILABLE,50.0),
            SlotInfo("s22","A-02","Standard","🚗",SlotStatus.OCCUPIED, 50.0,"DL13YZ1234","Suresh","10:00 AM","1h"),
            SlotInfo("s23","A-03","Compact", "🚙",SlotStatus.AVAILABLE,35.0),
            SlotInfo("s24","A-04","Compact", "🚙",SlotStatus.AVAILABLE,35.0),
            SlotInfo("s25","B-01","Large",   "🚐",SlotStatus.OCCUPIED, 65.0,"DL14AB5678","Kavita","08:00 AM","4h"),
            SlotInfo("s26","B-02","Standard","🚗",SlotStatus.AVAILABLE,50.0),
            SlotInfo("s27","B-03","Disabled","♿",SlotStatus.AVAILABLE,25.0),
            SlotInfo("s28","B-04","Standard","🚗",SlotStatus.AVAILABLE,50.0),
            SlotInfo("s29","C-01","Standard","🚗",SlotStatus.OCCUPIED, 50.0,"DL15CD9012","Arun","12:30 PM","2h"),
            SlotInfo("s30","C-02","Standard","🚗",SlotStatus.AVAILABLE,50.0),
        )
    ),
    ParkingLotDetail(
        id = "4", name = "Metro Station P&R", address = "Shivajinagar",
        distance = "1.4 km", walkTime = "18 min", rating = 3.9f, reviews = 2100,
        pricePerHour = 30.0, totalSlots = 15, available = 9,
        isPremium = false, isCovered = false, hasEV = true, is24Hours = true,
        amenities = listOf("EV Charging", "CCTV", "24/7"),
        slots = listOf(
            SlotInfo("s31","A-01","Standard","🚗",SlotStatus.AVAILABLE,30.0),
            SlotInfo("s32","A-02","Standard","🚗",SlotStatus.OCCUPIED, 30.0,"DL16EF3456","Pradeep","09:45 AM","3h"),
            SlotInfo("s33","A-03","EV",      "⚡",SlotStatus.AVAILABLE,50.0),
            SlotInfo("s34","A-04","Standard","🚗",SlotStatus.AVAILABLE,30.0),
            SlotInfo("s35","A-05","Standard","🚗",SlotStatus.OCCUPIED, 30.0,"DL17GH7890","Meena","07:00 AM","6h"),
        )
    ),
    ParkingLotDetail(
        id = "5", name = "The Grand Hotel", address = "Koregaon Park",
        distance = "1.8 km", walkTime = "22 min", rating = 4.7f, reviews = 318,
        pricePerHour = 80.0, totalSlots = 8, available = 1,
        isPremium = true, isCovered = true, hasEV = true, is24Hours = false,
        amenities = listOf("Valet", "EV", "Car Wash", "Lounge Access", "WiFi"),
        slots = listOf(
            SlotInfo("s36","VIP-01","VIP","⭐",SlotStatus.OCCUPIED,    150.0,"DL18IJ1234","Arjun","11:00 AM","3h"),
            SlotInfo("s37","VIP-02","VIP","⭐",SlotStatus.OCCUPIED,    150.0,"DL19KL5678","Nisha","10:00 AM","4h"),
            SlotInfo("s38","A-01","EV",  "⚡",SlotStatus.OCCUPIED,    100.0,"DL20MN9012","Vikash","08:30 AM","5h"),
            SlotInfo("s39","A-02","EV",  "⚡",SlotStatus.AVAILABLE,   100.0),
            SlotInfo("s40","A-03","Large","🚐",SlotStatus.OCCUPIED,   90.0,"DL21OP3456","Deepa","12:00 PM","2h"),
        )
    ),
)

// ─────────────────────────────────────────────────────────────────────────────
// MAIN SCREEN
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserParkingSearchScreen(
    onNavigateBack : () -> Unit,
    onBookSlot     : (lotId: String, slotId: String) -> Unit,
) {
    var isDark          by remember { mutableStateOf(false) }
    val t               = if (isDark) darkTheme() else lightTheme()
    var searchQuery     by remember { mutableStateOf("") }
    var selectedFilter  by remember { mutableStateOf(ParkingFilter.ALL) }
    var selectedSort    by remember { mutableStateOf(ParkingSortOption.DISTANCE) }
    var showSortSheet   by remember { mutableStateOf(false) }
    var expandedLotId   by remember { mutableStateOf<String?>(null) }
    var selectedSlotFilter by remember { mutableStateOf<SlotStatus?>(null) }

    val filteredLots = remember(searchQuery, selectedFilter, selectedSort) {
        sampleParkingLots
            .filter { lot ->
                val matchesSearch = searchQuery.isEmpty() ||
                        lot.name.contains(searchQuery, ignoreCase = true) ||
                        lot.address.contains(searchQuery, ignoreCase = true)
                val matchesFilter = when (selectedFilter) {
                    ParkingFilter.ALL      -> true
                    ParkingFilter.NEARBY   -> lot.distance.replace(" km", "").toFloat() < 1.0f
                    ParkingFilter.CHEAPEST -> lot.pricePerHour < 40.0
                    ParkingFilter.PREMIUM  -> lot.isPremium
                    ParkingFilter.COVERED  -> lot.isCovered
                    ParkingFilter.EV       -> lot.hasEV
                    ParkingFilter.OPEN24H  -> lot.is24Hours
                }
                matchesSearch && matchesFilter
            }
            .sortedWith(when (selectedSort) {
                ParkingSortOption.DISTANCE     -> compareBy { it.distance.replace(" km", "").toFloat() }
                ParkingSortOption.PRICE_LOW    -> compareBy { it.pricePerHour }
                ParkingSortOption.PRICE_HIGH   -> compareByDescending { it.pricePerHour }
                ParkingSortOption.RATING       -> compareByDescending { it.rating }
                ParkingSortOption.AVAILABILITY -> compareByDescending { it.available }
            })
    }

    Surface(modifier = Modifier.fillMaxSize(), color = t.bg) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top Bar ────────────────────────────────────────────────────────
            Surface(color = t.surface, shadowElevation = 2.dp) {
                Column {
                    Row(
                        modifier          = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, "Back", tint = t.textPrim)
                        }
                        OutlinedTextField(
                            value         = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier      = Modifier.weight(1f),
                            placeholder   = { Text("Search parking lots, areas...", fontSize = 13.sp, color = t.textTert) },
                            singleLine    = true,
                            shape         = RoundedCornerShape(12.dp),
                            colors        = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = PSC.Primary,
                                unfocusedBorderColor = t.border,
                                focusedTextColor     = t.textPrim,
                                unfocusedTextColor   = t.textPrim,
                            ),
                            leadingIcon  = { Icon(Icons.Outlined.Search, null, tint = t.textSec, modifier = Modifier.size(18.dp)) },
                            trailingIcon = if (searchQuery.isNotEmpty()) {{
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Close, null, tint = t.textSec, modifier = Modifier.size(18.dp))
                                }
                            }} else null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        // Dark mode toggle
                        IconButton(onClick = { isDark = !isDark }) {
                            Icon(
                                if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                "Theme", tint = t.textPrim
                            )
                        }
                    }

                    // Filter chips
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ParkingFilter.entries, key = { it.name }) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick  = { selectedFilter = filter },
                                label    = { Text(filter.label, fontSize = 12.sp) },
                                colors   = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PSC.Primary,
                                    selectedLabelColor     = PSC.OnPrimary,
                                    containerColor         = t.surface,
                                    labelColor             = t.textSec,
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled             = true,
                                    selected            = selectedFilter == filter,
                                    borderColor         = t.border,
                                    selectedBorderColor = PSC.PrimaryDark,
                                )
                            )
                        }
                    }

                    // Sort row
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            "${filteredLots.size} lots found",
                            fontSize = 13.sp, color = t.textSec, fontWeight = FontWeight.Medium
                        )
                        Surface(
                            shape   = RoundedCornerShape(8.dp),
                            color   = t.surfaceVar,
                            onClick = { showSortSheet = true }
                        ) {
                            Row(
                                modifier          = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.Sort, null, tint = t.textSec, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(selectedSort.label, fontSize = 12.sp, color = t.textSec)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Filled.KeyboardArrowDown, null, tint = t.textTert, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            // ── Lot List ───────────────────────────────────────────────────────
            LazyColumn(
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(top = 12.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLots, key = { it.id }) { lot ->
                    ExpandableLotCard(
                        t                  = t,
                        lot                = lot,
                        isExpanded         = expandedLotId == lot.id,
                        selectedSlotFilter = selectedSlotFilter,
                        onToggleExpand     = { expandedLotId = if (expandedLotId == lot.id) null else lot.id },
                        onSlotFilterChange = { selectedSlotFilter = it },
                        onBookSlot         = { slotId -> onBookSlot(lot.id, slotId) },
                    )
                }
            }
        }
    }

    // Sort bottom sheet
    if (showSortSheet) {
        ParkingSortSheet(
            t        = t,
            selected = selectedSort,
            onSelect = { selectedSort = it; showSortSheet = false },
            onDismiss= { showSortSheet = false }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// EXPANDABLE LOT CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ExpandableLotCard(
    t                  : PSTheme,
    lot                : ParkingLotDetail,
    isExpanded         : Boolean,
    selectedSlotFilter : SlotStatus?,
    onToggleExpand     : () -> Unit,
    onSlotFilterChange : (SlotStatus?) -> Unit,
    onBookSlot         : (slotId: String) -> Unit,
) {
    val availPct = lot.available.toFloat() / lot.totalSlots
    val avColor  = when {
        availPct < 0.15f -> PSC.Error
        availPct < 0.4f  -> PSC.Warning
        else             -> PSC.Success
    }
    val avBg = when {
        availPct < 0.15f -> if (t.isDark) PSC.ErrorDark else PSC.ErrorLight
        availPct < 0.4f  -> if (t.isDark) PSC.WarningDark else PSC.WarningLight
        else             -> if (t.isDark) PSC.SuccessDark else PSC.SuccessLight
    }

    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = t.surface),
        elevation = CardDefaults.cardElevation(if (t.isDark) 0.dp else 2.dp),
        border    = if (t.isDark) BorderStroke(1.dp, t.border) else null,
    ) {
        Column {
            // Premium badge
            if (lot.isPremium) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(PSC.Purple, PSC.PrimaryDark)), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text("⭐ Premium · Valet Available", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Column(
                modifier = Modifier.padding(14.dp).clickable { onToggleExpand() }
            ) {
                // Header row
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(lot.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = t.textPrim, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.LocationOn, null, tint = t.textTert, modifier = Modifier.size(12.dp))
                            Text(lot.address, fontSize = 12.sp, color = t.textSec, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("₹${lot.pricePerHour.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = PSC.PrimaryDark)
                        Text("/hour", fontSize = 11.sp, color = t.textSec)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Info row
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.NearMe, null, tint = t.textTert, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("${lot.distance} · ${lot.walkTime}", fontSize = 12.sp, color = t.textSec)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, tint = PSC.Primary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("${lot.rating} (${lot.reviews})", fontSize = 12.sp, color = t.textSec)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Availability bar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress   = { availPct },
                        modifier   = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color      = avColor,
                        trackColor = t.surfaceDim,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text     = "${lot.available}/${lot.totalSlots} free",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color    = avColor
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Amenity tags
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(lot.amenities.take(5), key = { it }) { amenity ->
                        Text(
                            amenity,
                            modifier = Modifier.background(t.surfaceVar, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp, color = t.textSec
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Expand/collapse button
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(if (t.isDark) avBg else avBg, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(7.dp).background(avColor, androidx.compose.foundation.shape.CircleShape))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("${lot.available} slots available", fontSize = 12.sp, color = avColor, fontWeight = FontWeight.SemiBold)
                    }

                    Row(
                        modifier          = Modifier
                            .background(PSC.PrimaryLight, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (isExpanded) "Hide Slots" else "View Slots",
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color      = PSC.PrimaryDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            null, tint = PSC.PrimaryDark, modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // ── Slot Info Panel ────────────────────────────────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter   = expandVertically() + fadeIn(),
                exit    = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider(color = t.divider)
                    SlotInfoPanel(
                        t                  = t,
                        slots              = lot.slots,
                        selectedSlotFilter = selectedSlotFilter,
                        onSlotFilterChange = onSlotFilterChange,
                        onBookSlot         = onBookSlot,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SLOT INFO PANEL
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SlotInfoPanel(
    t                  : PSTheme,
    slots              : List<SlotInfo>,
    selectedSlotFilter : SlotStatus?,
    onSlotFilterChange : (SlotStatus?) -> Unit,
    onBookSlot         : (slotId: String) -> Unit,
) {
    val filteredSlots = if (selectedSlotFilter == null) slots else slots.filter { it.status == selectedSlotFilter }

    Column(modifier = Modifier.padding(14.dp)) {
        // Slot stats row
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text("Slot Details", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = t.textPrim)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(
                    Triple(PSC.Success, "Free",     slots.count { it.status == SlotStatus.AVAILABLE }),
                    Triple(PSC.Error,   "Occupied", slots.count { it.status == SlotStatus.OCCUPIED }),
                    Triple(PSC.Warning, "Reserved", slots.count { it.status == SlotStatus.RESERVED }),
                ).forEach { (color, label, count) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(color, androidx.compose.foundation.shape.CircleShape))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("$count $label", fontSize = 11.sp, color = t.textSec)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Slot visual grid
        SlotVisualGrid(t = t, slots = slots)

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = t.divider)
        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips for slots
        LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            item {
                FilterChip(
                    selected = selectedSlotFilter == null,
                    onClick  = { onSlotFilterChange(null) },
                    label    = { Text("All (${slots.size})", fontSize = 11.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PSC.Primary,
                        selectedLabelColor     = PSC.OnPrimary,
                        containerColor         = t.surface,
                        labelColor             = t.textSec,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true, selected = selectedSlotFilter == null,
                        borderColor = t.border, selectedBorderColor = PSC.PrimaryDark,
                    )
                )
            }
            items(SlotStatus.entries, key = { it.name }) { status ->
                FilterChip(
                    selected = selectedSlotFilter == status,
                    onClick  = { onSlotFilterChange(if (selectedSlotFilter == status) null else status) },
                    label    = { Text("${status.label} (${slots.count { it.status == status }})", fontSize = 11.sp) },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PSC.Primary,
                        selectedLabelColor     = PSC.OnPrimary,
                        containerColor         = t.surface,
                        labelColor             = t.textSec,
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true, selected = selectedSlotFilter == status,
                        borderColor = t.border, selectedBorderColor = PSC.PrimaryDark,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Slot cards list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            filteredSlots.forEach { slot ->
                SlotDetailCard(t = t, slot = slot, onBook = { onBookSlot(slot.id) })
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SLOT VISUAL GRID
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SlotVisualGrid(t: PSTheme, slots: List<SlotInfo>) {
    val cols   = 6
    val chunks = slots.chunked(cols)

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        chunks.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { slot ->
                    val (bg, border, textColor) = when (slot.status) {
                        SlotStatus.AVAILABLE    -> Triple(if (t.isDark) PSC.Success.copy(0.15f) else PSC.SuccessLight, PSC.Success.copy(0.5f), PSC.Success)
                        SlotStatus.OCCUPIED     -> Triple(if (t.isDark) PSC.Error.copy(0.15f)   else PSC.ErrorLight,   PSC.Error.copy(0.5f),   PSC.Error)
                        SlotStatus.RESERVED     -> Triple(if (t.isDark) PSC.Warning.copy(0.15f) else PSC.WarningLight, PSC.Warning.copy(0.5f), PSC.Warning)
                        SlotStatus.MAINTENANCE  -> Triple(t.surfaceVar, t.border, t.textTert)
                    }
                    Box(
                        modifier         = Modifier
                            .size(44.dp)
                            .background(bg, RoundedCornerShape(8.dp))
                            .border(1.dp, border, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (slot.status == SlotStatus.OCCUPIED) "🚗" else slot.emoji, fontSize = 12.sp)
                            Text(slot.number, fontSize = 7.sp, fontWeight = FontWeight.Bold, color = textColor)
                        }
                    }
                }
                // Fill remainder
                repeat(cols - row.size) {
                    Box(modifier = Modifier.size(44.dp))
                }
            }
        }

        // Legend
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf(PSC.Success to "Free", PSC.Error to "Occupied", PSC.Warning to "Reserved", t.textTert to "Blocked").forEach { (c, l) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).background(c, androidx.compose.foundation.shape.CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(l, fontSize = 10.sp, color = t.textSec)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SLOT DETAIL CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SlotDetailCard(t: PSTheme, slot: SlotInfo, onBook: () -> Unit) {
    val (statusColor, statusBg) = when (slot.status) {
        SlotStatus.AVAILABLE    -> PSC.Success  to (if (t.isDark) PSC.SuccessDark else PSC.SuccessLight)
        SlotStatus.OCCUPIED     -> PSC.Error    to (if (t.isDark) PSC.ErrorDark   else PSC.ErrorLight)
        SlotStatus.RESERVED     -> PSC.Warning  to (if (t.isDark) PSC.WarningDark else PSC.WarningLight)
        SlotStatus.MAINTENANCE  -> t.textTert   to t.surfaceVar
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = t.surface),
        elevation = CardDefaults.cardElevation(if (t.isDark) 0.dp else 1.dp),
        border    = BorderStroke(1.dp, t.border),
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Slot icon box
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(statusBg, RoundedCornerShape(10.dp))
                    .border(1.dp, statusColor.copy(0.4f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (slot.status == SlotStatus.OCCUPIED) "🚗" else slot.emoji, fontSize = 16.sp)
                    Text(slot.number, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(slot.type, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = t.textPrim)
                    Spacer(modifier = Modifier.width(6.dp))
                    // Status pill
                    Text(
                        slot.status.label,
                        modifier   = Modifier.background(statusBg, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = statusColor
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                when (slot.status) {
                    SlotStatus.OCCUPIED -> {
                        Text(slot.driver ?: "", fontSize = 12.sp, color = t.textSec)
                        Text("In: ${slot.entryTime ?: "--"}  ·  ${slot.duration ?: "--"}", fontSize = 11.sp, color = t.textTert)
                        if (slot.vehicle != null) {
                            Text(
                                slot.vehicle,
                                fontSize = 10.sp, color = t.textSec,
                                modifier = Modifier.background(t.surfaceVar, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    SlotStatus.RESERVED -> {
                        Text(slot.driver ?: "Reserved", fontSize = 12.sp, color = t.textSec)
                        Text("From: ${slot.entryTime ?: "--"}", fontSize = 11.sp, color = t.textTert)
                    }
                    SlotStatus.MAINTENANCE -> Text("Under maintenance", fontSize = 12.sp, color = t.textTert)
                    SlotStatus.AVAILABLE   -> Text("Ready to book", fontSize = 12.sp, color = PSC.Success)
                }
            }

            // Price + action
            Column(horizontalAlignment = Alignment.End) {
                Text("₹${slot.pricePerHour.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = PSC.PrimaryDark)
                Text("/hr", fontSize = 10.sp, color = t.textSec)
                Spacer(modifier = Modifier.height(6.dp))
                if (slot.status == SlotStatus.AVAILABLE) {
                    Button(
                        onClick  = onBook,
                        modifier = Modifier.height(32.dp),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = PSC.Primary, contentColor = PSC.OnPrimary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("Book", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(statusBg, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(slot.status.label, fontSize = 11.sp, color = statusColor, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SORT BOTTOM SHEET
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParkingSortSheet(
    t        : PSTheme,
    selected : ParkingSortOption,
    onSelect : (ParkingSortOption) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor   = t.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Sort Parking Lots", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = t.textPrim)
            Spacer(modifier = Modifier.height(12.dp))
            ParkingSortOption.entries.forEach { option ->
                Row(
                    modifier              = Modifier.fillMaxWidth().clickable { onSelect(option) }.padding(vertical = 14.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        option.label,
                        fontSize   = 15.sp,
                        fontWeight = if (selected == option) FontWeight.Bold else FontWeight.Normal,
                        color      = if (selected == option) PSC.PrimaryDark else t.textPrim
                    )
                    if (selected == option) {
                        Icon(Icons.Filled.Check, null, tint = PSC.Primary, modifier = Modifier.size(18.dp))
                    }
                }
                HorizontalDivider(color = t.divider)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
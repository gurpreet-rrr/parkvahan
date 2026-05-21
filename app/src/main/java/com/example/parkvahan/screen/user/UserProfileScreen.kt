package com.example.parkvahan.screen.user

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

private object UP {
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
    val Info          = Color(0xFF2563EB)
    val Purple        = Color(0xFF7C3AED)
    val Divider       = Color(0xFFF3F4F6)
    val Border        = Color(0xFFE5E7EB)
}

private data class UserProfile(
    val name          : String  = "Rahul Kumar",
    val email         : String  = "rahul.kumar@email.com",
    val phone         : String  = "+91 98765 43210",
    val initials      : String  = "RK",
    val memberSince   : String  = "March 2024",
    val totalBookings : Int     = 47,
    val totalSpent    : Double  = 4850.0,
    val savedLots     : Int     = 6,
    val loyaltyPoints : Int     = 1240,
    val memberTier    : String  = "Gold",
)

private data class SavedVehicle(
    val id        : String,
    val model     : String,
    val number    : String,
    val type      : String,
    val emoji     : String,
    val isPrimary : Boolean = false,
)

private data class PaymentMethod(
    val id        : String,
    val name      : String,
    val detail    : String,
    val icon      : String,
    val isDefault : Boolean = false,
)

private data class ToggleRowData(
    val icon      : ImageVector,
    val iconColor : Color,
    val title     : String,
    val subtitle  : String,
    val checked   : Boolean,
    val onToggle  : (Boolean) -> Unit,
)

private val sampleProfile = UserProfile()

private val savedVehicles = listOf(
    SavedVehicle("v1", "Honda City",    "MH12AB1234", "Sedan", "🚗", true),
    SavedVehicle("v2", "Hyundai Creta", "MH14CD5678", "SUV",   "🚙"),
    SavedVehicle("v3", "Royal Enfield", "MH12EF9012", "Bike",  "🏍"),
)

private val paymentMethods = listOf(
    PaymentMethod("p1", "Google Pay", "rahul@okaxis",  "G", true),
    PaymentMethod("p2", "HDFC Bank",  "•••• 4321",     "💳"),
    PaymentMethod("p3", "Paytm",      "rahul@paytm",   "P"),
)

@Composable
fun UserProfileScreen(
    onNavigateBack : () -> Unit,
    onLogout       : () -> Unit,
) {
    var showEditProfile  by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var notificationsOn  by remember { mutableStateOf(true) }
    var locationOn       by remember { mutableStateOf(true) }

    var autoPayOn        by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize().background(UP.Background)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

            ProfileHeader(profile = sampleProfile, onBack = onNavigateBack, onEdit = { showEditProfile = true })
            StatsRow(profile = sampleProfile)
            LoyaltyCard(profile = sampleProfile)
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "My Vehicles", actionLabel = "Add", onAction = {})
            VehiclesSection()
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Payment Methods", actionLabel = "Add", onAction = {})
            PaymentSection()
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Preferences", actionLabel = null, onAction = {})
            PreferencesSection(
                notificationsOn = notificationsOn,
                locationOn      = locationOn,

                autoPayOn       = autoPayOn,
                onNotifications = { notificationsOn = it },
                onLocation      = { locationOn      = it },

                onAutoPay       = { autoPayOn       = it },
            )
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(title = "Account & Support", actionLabel = null, onAction = {})
            AccountSection(onLogoutClick = { showLogoutDialog = true })
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "ParkVahan v1.0.0",
                fontSize  = 12.sp,
                color     = UP.TextTertiary,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )
        }

        if (showEditProfile) {
            EditProfileSheet(profile = sampleProfile, onDismiss = { showEditProfile = false })
        }
        if (showLogoutDialog) {
            LogoutDialog(onConfirm = { showLogoutDialog = false; onLogout() }, onDismiss = { showLogoutDialog = false })
        }
    }
}

@Composable
private fun ProfileHeader(profile: UserProfile, onBack: () -> Unit, onEdit: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().background(
            Brush.verticalGradient(listOf(UP.Primary, UP.PrimaryDark, Color(0xFFE65100)))
        )
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().statusBarsPadding().padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back", tint = UP.OnPrimary) }
                Text("My Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = UP.OnPrimary)
                IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, "Edit", tint = UP.OnPrimary) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier.size(90.dp).clip(CircleShape)
                        .background(UP.OnPrimary.copy(alpha = 0.2f))
                        .border(3.dp, UP.OnPrimary.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profile.initials, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = UP.OnPrimary)
                }
                Box(
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                        .background(UP.Surface).border(2.dp, UP.Primary, CircleShape).clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CameraAlt, "Change photo", tint = UP.PrimaryDark, modifier = Modifier.size(14.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(profile.name, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = UP.OnPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(profile.email, fontSize = 13.sp, color = UP.OnPrimary.copy(alpha = 0.75f))
            Spacer(modifier = Modifier.height(2.dp))
            Text(profile.phone, fontSize = 13.sp, color = UP.OnPrimary.copy(alpha = 0.75f))
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.background(UP.OnPrimary.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Verified, null, tint = UP.OnPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text("Member since ${profile.memberSince}", fontSize = 12.sp, color = UP.OnPrimary, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun StatsRow(profile: UserProfile) {
    val fmt = java.text.NumberFormat.getInstance(java.util.Locale("en", "IN"))
    Card(
        modifier  = Modifier.fillMaxWidth().padding(16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = UP.Surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(
                Triple("${profile.totalBookings}", "Bookings",   Icons.Outlined.BookOnline),
                Triple("₹${fmt.format(profile.totalSpent)}", "Total Spent", Icons.Outlined.CurrencyRupee),
                Triple("${profile.savedLots}",    "Saved Lots", Icons.Outlined.Favorite),
            ).forEach { (value, label, icon) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Icon(icon, null, tint = UP.PrimaryDark, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = UP.TextPrimary)
                    Text(label, fontSize = 11.sp, color = UP.TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun LoyaltyCard(profile: UserProfile) {
    val tierColor = when (profile.memberTier) {
        "Gold"     -> Color(0xFFFFB200)
        "Platinum" -> Color(0xFF7C3AED)
        "Silver"   -> Color(0xFF6B7280)
        else       -> UP.Info
    }
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(listOf(tierColor, tierColor.copy(alpha = 0.7f))))
    ) {
        Box(modifier = Modifier.size(120.dp).align(Alignment.CenterEnd).offset(x = 30.dp).background(Color.White.copy(alpha = 0.08f), CircleShape))
        Box(modifier = Modifier.size(80.dp).align(Alignment.TopEnd).offset(x = 10.dp, y = (-20).dp).background(Color.White.copy(alpha = 0.06f), CircleShape))
        Row(
            modifier              = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${profile.memberTier} Member", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("${profile.loyaltyPoints} points", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("260 pts to Platinum tier", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.width(180.dp).height(6.dp).clip(RoundedCornerShape(3.dp)).background(Color.White.copy(alpha = 0.3f))) {
                    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.82f).clip(RoundedCornerShape(3.dp)).background(Color.White))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("82% to Platinum", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
            }
            Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.2f), onClick = {}) {
                Text("Redeem", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun VehiclesSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        savedVehicles.forEach { vehicle ->
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(14.dp),
                colors    = CardDefaults.cardColors(containerColor = UP.Surface),
                elevation = CardDefaults.cardElevation(1.dp),
                border    = if (vehicle.isPrimary) BorderStroke(1.5.dp, UP.Primary) else BorderStroke(1.dp, UP.Border)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp).background(if (vehicle.isPrimary) UP.PrimaryLight else UP.SurfaceVar, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) { Text(vehicle.emoji, fontSize = 22.sp) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(vehicle.model, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = UP.TextPrimary)
                            if (vehicle.isPrimary) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Primary", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UP.PrimaryDark,
                                    modifier = Modifier.background(UP.PrimaryLight, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text("${vehicle.number} · ${vehicle.type}", fontSize = 12.sp, color = UP.TextSecondary)
                    }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, null, tint = UP.TextTertiary, modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PaymentSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        paymentMethods.forEach { method ->
            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(14.dp),
                colors    = CardDefaults.cardColors(containerColor = UP.Surface),
                elevation = CardDefaults.cardElevation(1.dp),
                border    = if (method.isDefault) BorderStroke(1.5.dp, UP.Primary) else BorderStroke(1.dp, UP.Border)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(44.dp).background(UP.SurfaceVar, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Text(method.icon, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = UP.PrimaryDark)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(method.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = UP.TextPrimary)
                            if (method.isDefault) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Default", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UP.Success,
                                    modifier = Modifier.background(UP.SuccessLight, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Text(method.detail, fontSize = 12.sp, color = UP.TextSecondary)
                    }
                    IconButton(onClick = {}) { Icon(Icons.Outlined.MoreVert, null, tint = UP.TextTertiary, modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}

@Composable
private fun PreferencesSection(
    notificationsOn : Boolean, locationOn: Boolean,  autoPayOn: Boolean,
    onNotifications : (Boolean) -> Unit, onLocation: (Boolean) -> Unit,
    onAutoPay: (Boolean) -> Unit,
) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = UP.Surface),
        elevation = CardDefaults.cardElevation(1.dp),
        border    = BorderStroke(1.dp, UP.Border)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            listOf(
                ToggleRowData(Icons.Outlined.Notifications, UP.Warning, "Push Notifications", "Booking alerts & reminders", notificationsOn, onNotifications),
                ToggleRowData(Icons.Outlined.LocationOn,    UP.Info,    "Location Access",    "For finding nearby parking", locationOn,      onLocation),

                ToggleRowData(Icons.Outlined.Payment,       UP.Success, "Auto Pay",           "Pay automatically on exit",  autoPayOn,       onAutoPay),
            ).forEachIndexed { index, data ->
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(38.dp).background(data.iconColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Icon(data.icon, null, tint = data.iconColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(data.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = UP.TextPrimary)
                        Text(data.subtitle, fontSize = 12.sp, color = UP.TextSecondary)
                    }
                    Switch(checked = data.checked, onCheckedChange = data.onToggle,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = UP.Primary, uncheckedTrackColor = UP.SurfaceVar))
                }
                if (index < 3) Divider(color = UP.Divider, modifier = Modifier.padding(horizontal = 14.dp))
            }
        }
    }
}

@Composable
private fun AccountSection(onLogoutClick: () -> Unit) {
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = UP.Surface),
        elevation = CardDefaults.cardElevation(1.dp),
        border    = BorderStroke(1.dp, UP.Border)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            listOf(
                Triple(Icons.Outlined.Security,    UP.Info,          "Privacy & Security"),
                Triple(Icons.Outlined.HelpOutline, UP.Success,       "Help & Support"),
                Triple(Icons.Outlined.StarOutline, UP.Warning,       "Rate the App"),
                Triple(Icons.Outlined.Share,       UP.Purple,        "Refer a Friend"),
                Triple(Icons.Outlined.Description, UP.TextSecondary, "Terms & Privacy Policy"),
            ).forEachIndexed { index, (icon, color, title) ->
                Row(modifier = Modifier.fillMaxWidth().clickable { }.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(38.dp).background(color.copy(alpha = 0.10f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = UP.TextPrimary, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, null, tint = UP.TextTertiary, modifier = Modifier.size(18.dp))
                }
                if (index < 4) Divider(color = UP.Divider, modifier = Modifier.padding(horizontal = 14.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Card(
        modifier  = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = UP.ErrorLight),
        elevation = CardDefaults.cardElevation(0.dp),
        border    = BorderStroke(1.dp, UP.Error.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().clickable { onLogoutClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(38.dp).background(UP.Error.copy(alpha = 0.15f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ExitToApp, null, tint = UP.Error, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Log Out", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = UP.Error, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null, tint = UP.Error.copy(alpha = 0.5f))
        }
    }
}

@Composable
private fun SectionHeader(title: String, actionLabel: String?, onAction: () -> Unit) {
    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = UP.TextPrimary)
        if (actionLabel != null) {
            Text("+ $actionLabel", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = UP.PrimaryDark, modifier = Modifier.clickable { onAction() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileSheet(profile: UserProfile, onDismiss: () -> Unit) {
    var name  by remember { mutableStateOf(profile.name) }
    var phone by remember { mutableStateOf(profile.phone) }
    var email by remember { mutableStateOf(profile.email) }

    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = UP.Surface, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
            Text("Edit Profile", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = UP.TextPrimary)
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(UP.PrimaryLight).border(2.dp, UP.Primary, CircleShape), contentAlignment = Alignment.Center) {
                        Text(profile.initials, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = UP.PrimaryDark)
                    }
                    Box(modifier = Modifier.size(26.dp).clip(CircleShape).background(UP.Primary).clickable { }, contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.CameraAlt, null, tint = UP.OnPrimary, modifier = Modifier.size(14.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            listOf(
                Triple("Full Name",    name,  { v: String -> name  = v }),
                Triple("Phone Number", phone, { v: String -> phone = v }),
                Triple("Email",        email, { v: String -> email = v }),
            ).forEach { (label, value, onChange) ->
                OutlinedTextField(
                    value         = value,
                    onValueChange = onChange,
                    label         = { Text(label) },
                    modifier      = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape         = RoundedCornerShape(12.dp),
                    singleLine    = true,
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = UP.Primary,
                        focusedLabelColor    = UP.PrimaryDark,
                        unfocusedBorderColor = UP.Border,
                        focusedTextColor     = UP.TextPrimary,
                        unfocusedTextColor   = UP.TextPrimary,
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UP.Primary, contentColor = UP.OnPrimary)) {
                Text("Save Changes", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = UP.Surface,
        shape            = RoundedCornerShape(20.dp),
        icon = {
            Box(modifier = Modifier.size(52.dp).background(UP.ErrorLight, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ExitToApp, null, tint = UP.Error, modifier = Modifier.size(26.dp))
            }
        },
        title = { Text("Log Out?", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = UP.TextPrimary) },
        text  = { Text("You'll need to sign in again to access your bookings and profile.", fontSize = 14.sp, color = UP.TextSecondary, textAlign = TextAlign.Center) },
        confirmButton = {
            Button(onClick = onConfirm, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UP.Error, contentColor = Color.White)) {
                Text("Yes, Log Out", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, UP.Border)) {
                Text("Cancel", color = UP.TextSecondary)
            }
        }
    )
}
package com.example.parkvahan.screen.owner

import androidx.compose.ui.graphics.Color

            data class DashboardStats(
    val totalSlots        : Int    = 35,
    val occupiedSlots     : Int    = 22,
    val availableSlots    : Int    = 13,
    val reservedSlots     : Int    = 3,
    val maintenanceSlots  : Int    = 1,
    val todayRevenue      : Double = 4850.0,
    val monthRevenue      : Double = 124500.0,
    val totalRevenue      : Double = 892000.0,
    val activeBookings    : Int    = 22,
    val upcomingBookings  : Int    = 8,
    val occupancyRate     : Float  = 62.8f,
    val yesterdayRate     : Float  = 58.3f,
    val carsEnteredToday  : Int    = 87,
    val carsExitedToday   : Int    = 65,
    val entryPeakHour     : String = "9–10 AM",
    val exitPeakHour      : String = "6–7 PM",
    val avgDuration       : String = "1h 42m",
    val customerReturnRate: Int    = 68,
    val bestSlot          : String = "A-03 (EV)",
    val lastUpdated       : String = "2 min ago",
)

            enum class SlotStatus(val label: String) {
    AVAILABLE("Free"), OCCUPIED("Occupied"), RESERVED("Reserved"), MAINTENANCE("Blocked")
}

            enum class SlotType(val label: String, val emoji: String) {
    STANDARD("Standard","🚗"), COMPACT("Compact","🚙"), LARGE("Large","🚐"),
    EV("EV","⚡"), DISABLED("Disabled","♿"), VIP("VIP","⭐")
}

            data class ParkingSlot(
    val id      : String,
    val number  : String,
    val type    : SlotType,
    val status  : SlotStatus,
    val price   : Double,
    val vehicle : String? = null,
    val driver  : String? = null,
    val entry   : String? = null,
    val dur     : String? = null,
)

            enum class BookingStatus(val label: String) {
    ACTIVE("Active"), UPCOMING("Upcoming"), COMPLETED("Completed"), CANCELLED("Cancelled")
}

            enum class PayStatus(val label: String) {
    PAID("Paid"), PENDING("Pending"), FAILED("Failed"), REFUNDED("Refunded")
}

            data class Booking(
    val id        : String,
    val driver    : String,
    val vehicle   : String,
    val slot      : String,
    val type      : SlotType,
    val status    : BookingStatus,
    val entry     : String,
    val exit      : String?,
    val dur       : String?,
    val amount    : Double,
    val payStatus : PayStatus,
)

            data class SlotPricing(
    val type        : SlotType,
    val perHour     : Double,
    val perDay      : Double,
    val perMonth    : Double,
    val autoBilling : Boolean,
    val graceMins   : Int,
    val overtime    : Double,
)

            data class RevenuePoint(val label: String, val actual: Double, val projected: Double)
            data class FlowPoint(val hour: String, val inflow: Int, val outflow: Int)
            data class PaymentRecord(
    val id: String, val driver: String, val slot: String,
    val amount: Double, val method: String, val status: PayStatus, val time: String
)

            enum class AlertType { OVERSTAY, PAYMENT, CAMERA, BARRIER, SUSPICIOUS }
            data class Alert(val id: String, val type: AlertType, val message: String, val time: String)

// ═══════════════════════════════════════════════════════════════════════════════
// SAMPLE DATA
// ═══════════════════════════════════════════════════════════════════════════════
            val stats = DashboardStats()

            val slots = listOf(
    ParkingSlot("1","A-01",SlotType.STANDARD,SlotStatus.OCCUPIED,50.0,"DL01AB1234","Rahul Kumar","10:30 AM","2h 15m"),
    ParkingSlot("2","A-02",SlotType.STANDARD,SlotStatus.AVAILABLE,50.0),
    ParkingSlot("3","A-03",SlotType.EV,SlotStatus.OCCUPIED,80.0,"DL02CD5678","Priya Singh","09:15 AM","3h 30m"),
    ParkingSlot("4","A-04",SlotType.COMPACT,SlotStatus.AVAILABLE,40.0),
    ParkingSlot("5","A-05",SlotType.VIP,SlotStatus.RESERVED,150.0,"DL03EF9012","Amit Sharma","02:00 PM",null),
    ParkingSlot("6","B-01",SlotType.LARGE,SlotStatus.OCCUPIED,70.0,"DL04GH3456","Sneha Gupta","11:00 AM","1h 45m"),
    ParkingSlot("7","B-02",SlotType.DISABLED,SlotStatus.AVAILABLE,30.0),
    ParkingSlot("8","B-03",SlotType.STANDARD,SlotStatus.OCCUPIED,50.0,"DL05IJ7890","Vikram Patel","08:45 AM","4h"),
    ParkingSlot("9","B-04",SlotType.STANDARD,SlotStatus.MAINTENANCE,50.0),
    ParkingSlot("10","B-05",SlotType.EV,SlotStatus.AVAILABLE,80.0),
    ParkingSlot("11","C-01",SlotType.COMPACT,SlotStatus.OCCUPIED,40.0,"DL06KL2345","Neha Joshi","12:00 PM","45m"),
    ParkingSlot("12","C-02",SlotType.STANDARD,SlotStatus.AVAILABLE,50.0),
    ParkingSlot("13","C-03",SlotType.STANDARD,SlotStatus.OCCUPIED,50.0,"DL07MN3456","Ravi Kumar","07:30 AM","5h"),
    ParkingSlot("14","C-04",SlotType.LARGE,SlotStatus.RESERVED,70.0),
    ParkingSlot("15","C-05",SlotType.EV,SlotStatus.OCCUPIED,80.0,"DL08OP4567","Sita Devi","01:15 PM","1h 30m"),
)

            val activeBookings = listOf(
    Booking("BK001","Rahul Kumar","DL01AB1234","A-01",SlotType.STANDARD,BookingStatus.ACTIVE,"10:30 AM",null,"2h 15m",112.5,PayStatus.PAID),
    Booking("BK002","Priya Singh","DL02CD5678","A-03",SlotType.EV,BookingStatus.ACTIVE,"09:15 AM",null,"3h 30m",280.0,PayStatus.PAID),
    Booking("BK003","Sneha Gupta","DL04GH3456","B-01",SlotType.LARGE,BookingStatus.ACTIVE,"11:00 AM",null,"1h 45m",122.5,PayStatus.PENDING),
)

            val upcomingBookings = listOf(
    Booking("BK005","Amit Sharma","DL03EF9012","A-05",SlotType.VIP,BookingStatus.UPCOMING,"02:00 PM","05:00 PM","3h",450.0,PayStatus.PAID),
    Booking("BK006","Neha Joshi","DL06KL2345","A-02",SlotType.STANDARD,BookingStatus.UPCOMING,"03:30 PM","06:30 PM","3h",150.0,PayStatus.PENDING),
)

            val payments = listOf(
    PaymentRecord("P001","Rahul Kumar","A-01",112.5,"UPI",PayStatus.PAID,"Today 10:30 AM"),
    PaymentRecord("P002","Priya Singh","A-03",280.0,"Card",PayStatus.PAID,"Today 09:15 AM"),
    PaymentRecord("P003","Vikram Patel","B-03",200.0,"UPI",PayStatus.PAID,"Today 08:45 AM"),
    PaymentRecord("P004","Anjali Roy","C-02",75.0,"Cash",PayStatus.PAID,"Yesterday 06 PM"),
    PaymentRecord("P005","Suresh Kumar","C-04",160.0,"Card",PayStatus.FAILED,"Yesterday 02 PM"),
)

            val pricing = listOf(
    SlotPricing(SlotType.STANDARD,50.0,400.0,8000.0,true,10,60.0),
    SlotPricing(SlotType.COMPACT,40.0,320.0,6500.0,true,10,50.0),
    SlotPricing(SlotType.LARGE,70.0,560.0,11000.0,true,15,85.0),
    SlotPricing(SlotType.EV,80.0,640.0,12500.0,true,10,100.0),
    SlotPricing(SlotType.DISABLED,30.0,240.0,5000.0,true,15,35.0),
    SlotPricing(SlotType.VIP,150.0,1200.0,25000.0,true,20,180.0),
)

            val weekRevenue = listOf(
    RevenuePoint("Mon",3200.0,3000.0), RevenuePoint("Tue",4100.0,3800.0),
    RevenuePoint("Wed",3800.0,4000.0), RevenuePoint("Thu",5200.0,4500.0),
    RevenuePoint("Fri",6800.0,5500.0), RevenuePoint("Sat",7500.0,6000.0),
    RevenuePoint("Sun",4850.0,5000.0),
)

            val monthRevenue = listOf(
    RevenuePoint("W1",18000.0,16000.0), RevenuePoint("W2",22000.0,20000.0),
    RevenuePoint("W3",31000.0,28000.0), RevenuePoint("W4",28000.0,30000.0),
    RevenuePoint("W5",25500.0,27000.0),
)

            val flowData = listOf(
    FlowPoint("6AM",2,1),  FlowPoint("7AM",8,3),   FlowPoint("8AM",15,5),
    FlowPoint("9AM",22,8), FlowPoint("10AM",18,12), FlowPoint("11AM",14,15),
    FlowPoint("12PM",10,18),FlowPoint("1PM",12,14), FlowPoint("2PM",9,10),
    FlowPoint("3PM",11,8), FlowPoint("4PM",13,9),  FlowPoint("5PM",16,20),
    FlowPoint("6PM",8,22), FlowPoint("7PM",4,15),
)

            val alerts = listOf(
    Alert("a1",AlertType.OVERSTAY,"Car DL01AB1234 in A-01 overstayed 45 min","2 min ago"),
    Alert("a2",AlertType.PAYMENT,"Vehicle DL05IJ7890 left without payment","15 min ago"),
    Alert("a3",AlertType.CAMERA,"Camera C-Zone offline","32 min ago"),
    Alert("a4",AlertType.BARRIER,"Entry barrier response delayed","1 hr ago"),
    Alert("a5",AlertType.SUSPICIOUS,"DL09QR5678 parked 6+ hrs in B-03","2 hrs ago"),
)

// Revenue analysis data
            val slotTypeRevenue = listOf(
    Triple("Standard", 43750.0, UPV.ChartBlue),
    Triple("EV",       34860.0, UPV.ChartGreen),
    Triple("VIP",      22410.0, UPV.ChartAmber),
    Triple("Large",    14940.0, UPV.ChartPurple),
    Triple("Compact",   8715.0, UPV.ChartRed),
)

            val dayOccupancy = listOf(
    "Mon" to 55, "Tue" to 62, "Wed" to 58,
    "Thu" to 71, "Fri" to 83, "Sat" to 92, "Sun" to 67,
)

            val hourlyPeaks = listOf(
    Triple("9–10 AM",  88, "ENTRY"),
    Triple("12–1 PM",  72, "MIXED"),
    Triple("6–7 PM",   95, "EXIT"),
    Triple("2–4 AM",    8, "LOW"),
)

// ═══════════════════════════════════════════════════════════════════════════════
// HELPERS
// ═══════════════════════════════════════════════════════════════════════════════
            val fmt get() = java.text.NumberFormat.getInstance(java.util.Locale("en","IN"))
            fun fmtInr(v: Double) = "₹${fmt.format(v)}"

            fun <T> List<T>.indexOfMax(selector: (T) -> Double): Int {
    var maxIdx = 0; var maxVal = Double.MIN_VALUE
    forEachIndexed { i, item -> val v = selector(item); if (v > maxVal) { maxVal = v; maxIdx = i } }
    return maxIdx
}

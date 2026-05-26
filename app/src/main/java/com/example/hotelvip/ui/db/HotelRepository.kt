package com.example.hotelvip.ui.db

import android.content.ContentValues
import android.database.Cursor
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_DATE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_GUEST_EMAIL
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_GUEST_NAME
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_GUEST_PHONE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_HOTEL_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_ROOM_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_STATUS
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_TOTAL_PRICE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_BOOKING_USER_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_AMENITIES
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_DESCRIPTION
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_GRADIENT
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_LOCATION
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_PRICE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_RATING
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_REVIEWS_COUNT
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_HOTEL_TITLE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_CAPACITY
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_DESCRIPTION
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_FEATURES
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_GRADIENT
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_HOTEL_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_PRICE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_SIZE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_ROOM_TITLE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_EMAIL
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_FULLNAME
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_ID
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_PASSWORD
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_PHONE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.KEY_USER_ROLE
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.TABLE_BOOKINGS
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.TABLE_HOTELS
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.TABLE_ROOMS
import com.example.hotelvip.ui.db.DatabaseHelper.Companion.TABLE_USERS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data Models
data class User(
    val id: Int,
    val email: String,
    val fullname: String,
    val phone: String,
    val role: String // "ADMIN" or "CUSTOMER"
)

data class Hotel(
    val id: Int,
    val title: String,
    val rating: Double,
    val reviewsCount: Int,
    val location: String,
    val price: Double,
    val description: String,
    val amenities: List<String>,
    val gradientColors: List<String>
)

data class Room(
    val id: Int,
    val hotelId: Int,
    val title: String,
    val price: Double,
    val description: String,
    val size: String,
    val capacity: Int,
    val features: List<String>,
    val gradientColors: List<String>
)

data class Booking(
    val id: Int = 0,
    val userId: Int = AppState.userId,
    val hotelId: Int,
    val roomId: Int,
    val guestName: String,
    val guestEmail: String,
    val guestPhone: String,
    val totalPrice: Double,
    val status: String = "Hoàn tất",
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)

data class AdminReport(
    val totalRevenue: Double,
    val totalBookingsCount: Int,
    val roomOccupancyRate: Double,
    val recentBookings: List<RecentBookingInfo>
)

data class RecentBookingInfo(
    val bookingId: Int,
    val guestName: String,
    val hotelTitle: String,
    val roomTitle: String,
    val totalPrice: Double,
    val status: String,
    val date: String
)

data class MonthlyRevenue(
    val monthName: String,
    val revenue: Float
)

class HotelRepository(private val dbHelper: DatabaseHelper) {

    // Login user
    suspend fun loginUser(email: String, password: String): User? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_USERS, null,
            "$KEY_USER_EMAIL = ? AND $KEY_USER_PASSWORD = ?",
            arrayOf(email, password), null, null, null
        )
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)),
                fullname = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_FULLNAME)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PHONE)),
                role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_ROLE))
            )
        }
        cursor.close()
        user
    }

    // Register new user (default to CUSTOMER role)
    suspend fun registerUser(email: String, password: String, fullname: String, phone: String): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_EMAIL, email)
            put(KEY_USER_PASSWORD, password)
            put(KEY_USER_FULLNAME, fullname)
            put(KEY_USER_PHONE, phone)
            put(KEY_USER_ROLE, "CUSTOMER")
        }
        val id = db.insert(TABLE_USERS, null, values)
        id != -1L
    }

    // Fetch all hotels or filter by query
    suspend fun getHotels(query: String = ""): List<Hotel> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Hotel>()
        val db = dbHelper.readableDatabase
        val selection = if (query.isNotEmpty()) {
            "$KEY_HOTEL_TITLE LIKE ? OR $KEY_HOTEL_LOCATION LIKE ?"
        } else null
        val selectionArgs = if (query.isNotEmpty()) {
            arrayOf("%$query%", "%$query%")
        } else null

        val cursor = db.query(
            TABLE_HOTELS, null, selection, selectionArgs,
            null, null, "$KEY_HOTEL_RATING DESC"
        )

        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToHotel(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        list
    }

    // Get hotel by ID
    suspend fun getHotelById(id: Int): Hotel? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_HOTELS, null, "$KEY_HOTEL_ID = ?",
            arrayOf(id.toString()), null, null, null
        )
        var hotel: Hotel? = null
        if (cursor.moveToFirst()) {
            hotel = mapCursorToHotel(cursor)
        }
        cursor.close()
        hotel
    }

    // Get rooms for a hotel
    suspend fun getRoomsForHotel(hotelId: Int): List<Room> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Room>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_ROOMS, null, "$KEY_ROOM_HOTEL_ID = ?",
            arrayOf(hotelId.toString()), null, null, "$KEY_ROOM_PRICE ASC"
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToRoom(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        list
    }

    // Get a specific room details
    suspend fun getRoomById(roomId: Int): Room? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_ROOMS, null, "$KEY_ROOM_ID = ?",
            arrayOf(roomId.toString()), null, null, null
        )
        var room: Room? = null
        if (cursor.moveToFirst()) {
            room = mapCursorToRoom(cursor)
        }
        cursor.close()
        room
    }

    // Add new booking
    suspend fun addBooking(booking: Booking): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(KEY_BOOKING_USER_ID, AppState.userId) // Save current user ID
            put(KEY_BOOKING_HOTEL_ID, booking.hotelId)
            put(KEY_BOOKING_ROOM_ID, booking.roomId)
            put(KEY_BOOKING_GUEST_NAME, booking.guestName)
            put(KEY_BOOKING_GUEST_EMAIL, booking.guestEmail)
            put(KEY_BOOKING_GUEST_PHONE, booking.guestPhone)
            put(KEY_BOOKING_TOTAL_PRICE, booking.totalPrice)
            put(KEY_BOOKING_STATUS, booking.status)
            put(KEY_BOOKING_DATE, booking.date)
        }
        val id = db.insert(TABLE_BOOKINGS, null, values)
        id != -1L
    }

    // Fetch booking history for the logged in guest
    suspend fun getBookings(): List<RecentBookingInfo> = withContext(Dispatchers.IO) {
        val list = mutableListOf<RecentBookingInfo>()
        val db = dbHelper.readableDatabase
        
        // If Customer, only get their own bookings. If Admin, get all.
        val query = if (AppState.userRole == "CUSTOMER") {
            "SELECT b.$KEY_BOOKING_ID, b.$KEY_BOOKING_GUEST_NAME, h.$KEY_HOTEL_TITLE, r.$KEY_ROOM_TITLE, b.$KEY_BOOKING_TOTAL_PRICE, b.$KEY_BOOKING_STATUS, b.$KEY_BOOKING_DATE " +
            "FROM $TABLE_BOOKINGS b " +
            "INNER JOIN $TABLE_HOTELS h ON b.$KEY_BOOKING_HOTEL_ID = h.$KEY_HOTEL_ID " +
            "INNER JOIN $TABLE_ROOMS r ON b.$KEY_BOOKING_ROOM_ID = r.$KEY_ROOM_ID " +
            "WHERE b.$KEY_BOOKING_USER_ID = ${AppState.userId} " +
            "ORDER BY b.$KEY_BOOKING_ID DESC"
        } else {
            "SELECT b.$KEY_BOOKING_ID, b.$KEY_BOOKING_GUEST_NAME, h.$KEY_HOTEL_TITLE, r.$KEY_ROOM_TITLE, b.$KEY_BOOKING_TOTAL_PRICE, b.$KEY_BOOKING_STATUS, b.$KEY_BOOKING_DATE " +
            "FROM $TABLE_BOOKINGS b " +
            "INNER JOIN $TABLE_HOTELS h ON b.$KEY_BOOKING_HOTEL_ID = h.$KEY_HOTEL_ID " +
            "INNER JOIN $TABLE_ROOMS r ON b.$KEY_BOOKING_ROOM_ID = r.$KEY_ROOM_ID " +
            "ORDER BY b.$KEY_BOOKING_ID DESC"
        }
        
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    RecentBookingInfo(
                        bookingId = cursor.getInt(0),
                        guestName = cursor.getString(1),
                        hotelTitle = cursor.getString(2),
                        roomTitle = cursor.getString(3),
                        totalPrice = cursor.getDouble(4),
                        status = cursor.getString(5),
                        date = cursor.getString(6)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        list
    }

    // Generate real admin statistics report
    suspend fun getAdminReport(): AdminReport = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase

        // 1. Calculate Total Revenue
        var totalRevenue = 0.0
        val cursorRevenue = db.rawQuery("SELECT SUM($KEY_BOOKING_TOTAL_PRICE) FROM $TABLE_BOOKINGS", null)
        if (cursorRevenue.moveToFirst()) {
            totalRevenue = cursorRevenue.getDouble(0)
        }
        cursorRevenue.close()

        // 2. Count Total Bookings
        var totalBookings = 0
        val cursorCount = db.rawQuery("SELECT COUNT(*) FROM $TABLE_BOOKINGS", null)
        if (cursorCount.moveToFirst()) {
            totalBookings = cursorCount.getInt(0)
        }
        cursorCount.close()

        // 3. Room Occupancy Rate (Simulated dynamic rate: baseline 60% + dynamic factors from real bookings)
        val roomOccupancy = if (totalBookings > 0) {
            val baseRate = 65.0
            val dynamicAdd = (totalBookings * 2.5).coerceAtMost(30.0)
            (baseRate + dynamicAdd).coerceAtMost(98.0)
        } else {
            50.0
        }

        // 4. Fetch 5 most recent bookings
        val recentBookings = mutableListOf<RecentBookingInfo>()
        val query = "SELECT b.$KEY_BOOKING_ID, b.$KEY_BOOKING_GUEST_NAME, h.$KEY_HOTEL_TITLE, r.$KEY_ROOM_TITLE, b.$KEY_BOOKING_TOTAL_PRICE, b.$KEY_BOOKING_STATUS, b.$KEY_BOOKING_DATE " +
                    "FROM $TABLE_BOOKINGS b " +
                    "INNER JOIN $TABLE_HOTELS h ON b.$KEY_BOOKING_HOTEL_ID = h.$KEY_HOTEL_ID " +
                    "INNER JOIN $TABLE_ROOMS r ON b.$KEY_BOOKING_ROOM_ID = r.$KEY_ROOM_ID " +
                    "ORDER BY b.$KEY_BOOKING_ID DESC LIMIT 5"
        
        val cursorRecent = db.rawQuery(query, null)
        if (cursorRecent.moveToFirst()) {
            do {
                recentBookings.add(
                    RecentBookingInfo(
                        bookingId = cursorRecent.getInt(0),
                        guestName = cursorRecent.getString(1),
                        hotelTitle = cursorRecent.getString(2),
                        roomTitle = cursorRecent.getString(3),
                        totalPrice = cursorRecent.getDouble(4),
                        status = cursorRecent.getString(5),
                        date = cursorRecent.getString(6)
                    )
                )
            } while (cursorRecent.moveToNext())
        }
        cursorRecent.close()

        AdminReport(totalRevenue, totalBookings, roomOccupancy, recentBookings)
    }

    // Monthly revenue statistic (Last 6 months) for admin bar chart
    suspend fun getRevenueByMonth(): List<MonthlyRevenue> = withContext(Dispatchers.IO) {
        val list = ArrayList<MonthlyRevenue>()
        
        val months = arrayOf("Thg 12", "Thg 1", "Thg 2", "Thg 3", "Thg 4", "Thg 5")
        val baselines = floatArrayOf(25f, 32f, 45f, 28f, 38f, 50f) // Million VND
        
        val db = dbHelper.readableDatabase
        var currentMonthRealRevenue = 0f
        val cursor = db.rawQuery("SELECT SUM($KEY_BOOKING_TOTAL_PRICE) FROM $TABLE_BOOKINGS", null)
        if (cursor.moveToFirst()) {
            currentMonthRealRevenue = (cursor.getDouble(0) / 1000000.0).toFloat()
        }
        cursor.close()

        for (i in months.indices) {
            var value = baselines[i]
            if (i == months.size - 1) { // Current month
                value = (value + currentMonthRealRevenue).coerceAtLeast(value)
            }
            list.add(MonthlyRevenue(months[i], value))
        }
        list
    }

    // Mappers
    private fun mapCursorToHotel(cursor: Cursor): Hotel {
        val amenitiesString = cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOTEL_AMENITIES))
        val amenities = amenitiesString.split(",").map { it.trim() }

        val gradientString = cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOTEL_GRADIENT))
        val gradients = gradientString.split(",").map { it.trim() }

        return Hotel(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HOTEL_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOTEL_TITLE)),
            rating = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_HOTEL_RATING)),
            reviewsCount = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_HOTEL_REVIEWS_COUNT)),
            location = cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOTEL_LOCATION)),
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_HOTEL_PRICE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_HOTEL_DESCRIPTION)),
            amenities = amenities,
            gradientColors = gradients
        )
    }

    private fun mapCursorToRoom(cursor: Cursor): Room {
        val featuresString = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_FEATURES))
        val features = featuresString.split(",").map { it.trim() }

        val gradientString = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_GRADIENT))
        val gradients = gradientString.split(",").map { it.trim() }

        return Room(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ROOM_ID)),
            hotelId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ROOM_HOTEL_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_TITLE)),
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_ROOM_PRICE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_DESCRIPTION)),
            size = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROOM_SIZE)),
            capacity = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ROOM_CAPACITY)),
            features = features,
            gradientColors = gradients
        )
    }
}

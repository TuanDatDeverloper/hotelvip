package com.example.hotelvip.ui.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "hotelvip.db"
        const val DATABASE_VERSION = 2 // Incremented version to apply table changes

        // Table Users
        const val TABLE_USERS = "users"
        const val KEY_USER_ID = "id"
        const val KEY_USER_EMAIL = "email"
        const val KEY_USER_PASSWORD = "password"
        const val KEY_USER_FULLNAME = "fullname"
        const val KEY_USER_PHONE = "phone"
        const val KEY_USER_ROLE = "role" // "ADMIN" or "CUSTOMER"

        // Table Hotels
        const val TABLE_HOTELS = "hotels"
        const val KEY_HOTEL_ID = "id"
        const val KEY_HOTEL_TITLE = "title"
        const val KEY_HOTEL_RATING = "rating"
        const val KEY_HOTEL_REVIEWS_COUNT = "reviews_count"
        const val KEY_HOTEL_LOCATION = "location"
        const val KEY_HOTEL_PRICE = "price"
        const val KEY_HOTEL_DESCRIPTION = "description"
        const val KEY_HOTEL_AMENITIES = "amenities"
        const val KEY_HOTEL_GRADIENT = "gradient_colors"

        // Table Rooms
        const val TABLE_ROOMS = "rooms"
        const val KEY_ROOM_ID = "id"
        const val KEY_ROOM_HOTEL_ID = "hotel_id"
        const val KEY_ROOM_TITLE = "title"
        const val KEY_ROOM_PRICE = "price"
        const val KEY_ROOM_DESCRIPTION = "description"
        const val KEY_ROOM_SIZE = "size"
        const val KEY_ROOM_CAPACITY = "capacity"
        const val KEY_ROOM_FEATURES = "features"
        const val KEY_ROOM_GRADIENT = "gradient_colors"

        // Table Bookings
        const val TABLE_BOOKINGS = "bookings"
        const val KEY_BOOKING_ID = "id"
        const val KEY_BOOKING_USER_ID = "user_id" // Foreign key to users
        const val KEY_BOOKING_HOTEL_ID = "hotel_id"
        const val KEY_BOOKING_ROOM_ID = "room_id"
        const val KEY_BOOKING_GUEST_NAME = "guest_name"
        const val KEY_BOOKING_GUEST_EMAIL = "guest_email"
        const val KEY_BOOKING_GUEST_PHONE = "guest_phone"
        const val KEY_BOOKING_TOTAL_PRICE = "total_price"
        const val KEY_BOOKING_STATUS = "status"
        const val KEY_BOOKING_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users Table
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_EMAIL + " TEXT UNIQUE,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_FULLNAME + " TEXT,"
                + KEY_USER_PHONE + " TEXT,"
                + KEY_USER_ROLE + " TEXT" + ")")
        db.execSQL(createUsersTable)

        // Create Hotels Table
        val createHotelsTable = ("CREATE TABLE " + TABLE_HOTELS + "("
                + KEY_HOTEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_HOTEL_TITLE + " TEXT,"
                + KEY_HOTEL_RATING + " REAL,"
                + KEY_HOTEL_REVIEWS_COUNT + " INTEGER,"
                + KEY_HOTEL_LOCATION + " TEXT,"
                + KEY_HOTEL_PRICE + " REAL,"
                + KEY_HOTEL_DESCRIPTION + " TEXT,"
                + KEY_HOTEL_AMENITIES + " TEXT,"
                + KEY_HOTEL_GRADIENT + " TEXT" + ")")
        db.execSQL(createHotelsTable)

        // Create Rooms Table
        val createRoomsTable = ("CREATE TABLE " + TABLE_ROOMS + "("
                + KEY_ROOM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ROOM_HOTEL_ID + " INTEGER,"
                + KEY_ROOM_TITLE + " TEXT,"
                + KEY_ROOM_PRICE + " REAL,"
                + KEY_ROOM_DESCRIPTION + " TEXT,"
                + KEY_ROOM_SIZE + " TEXT,"
                + KEY_ROOM_CAPACITY + " INTEGER,"
                + KEY_ROOM_FEATURES + " TEXT,"
                + KEY_ROOM_GRADIENT + " TEXT,"
                + "FOREIGN KEY(" + KEY_ROOM_HOTEL_ID + ") REFERENCES " + TABLE_HOTELS + "(" + KEY_HOTEL_ID + ")" + ")")
        db.execSQL(createRoomsTable)

        // Create Bookings Table
        val createBookingsTable = ("CREATE TABLE " + TABLE_BOOKINGS + "("
                + KEY_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_BOOKING_USER_ID + " INTEGER,"
                + KEY_BOOKING_HOTEL_ID + " INTEGER,"
                + KEY_BOOKING_ROOM_ID + " INTEGER,"
                + KEY_BOOKING_GUEST_NAME + " TEXT,"
                + KEY_BOOKING_GUEST_EMAIL + " TEXT,"
                + KEY_BOOKING_GUEST_PHONE + " TEXT,"
                + KEY_BOOKING_TOTAL_PRICE + " REAL,"
                + KEY_BOOKING_STATUS + " TEXT,"
                + KEY_BOOKING_DATE + " TEXT,"
                + "FOREIGN KEY(" + KEY_BOOKING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + "),"
                + "FOREIGN KEY(" + KEY_BOOKING_HOTEL_ID + ") REFERENCES " + TABLE_HOTELS + "(" + KEY_HOTEL_ID + "),"
                + "FOREIGN KEY(" + KEY_BOOKING_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + KEY_ROOM_ID + ")" + ")")
        db.execSQL(createBookingsTable)

        // Seed initial data
        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HOTELS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        // Seed Users
        // 1. Admin account
        val uAdmin = ContentValues().apply {
            put(KEY_USER_EMAIL, "admin@hotelvip.com")
            put(KEY_USER_PASSWORD, "admin123")
            put(KEY_USER_FULLNAME, "Admin Manager")
            put(KEY_USER_PHONE, "0900000000")
            put(KEY_USER_ROLE, "ADMIN")
        }
        val adminUserId = db.insert(TABLE_USERS, null, uAdmin)

        // 2. Customer account
        val uCust = ContentValues().apply {
            put(KEY_USER_EMAIL, "vip@gmail.com")
            put(KEY_USER_PASSWORD, "vip123")
            put(KEY_USER_FULLNAME, "Khách Hàng Vip")
            put(KEY_USER_PHONE, "0987654321")
            put(KEY_USER_ROLE, "CUSTOMER")
        }
        val custUserId = db.insert(TABLE_USERS, null, uCust)

        // Seed Hotels
        // 1. Vinpearl Resort & Spa Phú Quốc
        val h1 = ContentValues().apply {
            put(KEY_HOTEL_TITLE, "Vinpearl Resort & Spa Phú Quốc")
            put(KEY_HOTEL_RATING, 4.8)
            put(KEY_HOTEL_REVIEWS_COUNT, 1250)
            put(KEY_HOTEL_LOCATION, "Phú Quốc, Kiên Giang")
            put(KEY_HOTEL_PRICE, 2500000.0)
            put(KEY_HOTEL_DESCRIPTION, "Nằm tại Bãi Dài hoang sơ, Vinpearl Resort & Spa Phú Quốc mang đến trải nghiệm nghỉ dưỡng 5 sao đẳng cấp với hồ bơi ngoài trời siêu rộng, bãi biển cát trắng riêng biệt, dịch vụ Akoya Spa cao cấp mang đậm phong cách Maldives và hệ thống ẩm thực phong phú.")
            put(KEY_HOTEL_AMENITIES, "Wifi miễn phí,Hồ bơi ngoài trời,Phòng Gym,Akoya Spa,Bãi biển riêng,Nhà hàng & Bar,Câu lạc bộ trẻ em")
            put(KEY_HOTEL_GRADIENT, "#FF9800,#F44336")
        }
        val hotelId1 = db.insert(TABLE_HOTELS, null, h1)

        // 2. InterContinental Danang Sun Peninsula Resort
        val h2 = ContentValues().apply {
            put(KEY_HOTEL_TITLE, "InterContinental Danang Resort")
            put(KEY_HOTEL_RATING, 4.9)
            put(KEY_HOTEL_REVIEWS_COUNT, 980)
            put(KEY_HOTEL_LOCATION, "Bán đảo Sơn Trà, Đà Nẵng")
            put(KEY_HOTEL_PRICE, 5500000.0)
            put(KEY_HOTEL_DESCRIPTION, "Tuyệt tác kiến trúc của Bill Bensley tọa lạc trên những ngọn đồi thoai thoải của Bán đảo Sơn Trà hoang sơ. Nơi giao hòa tuyệt vời giữa văn hóa truyền thống Việt Nam và sự xa hoa, đẳng cấp chuẩn quốc tế.")
            put(KEY_HOTEL_AMENITIES, "Wifi miễn phí,Hồ bơi vô cực,Trung tâm Fitness,Harnn Heritage Spa,Bãi biển riêng biệt,Nhà hàng La Maison 1888,Sân Golf đẳng cấp")
            put(KEY_HOTEL_GRADIENT, "#2196F3,#00BCD4")
        }
        val hotelId2 = db.insert(TABLE_HOTELS, null, h2)

        // 3. Sofitel Legend Metropole Hà Nội
        val h3 = ContentValues().apply {
            put(KEY_HOTEL_TITLE, "Sofitel Legend Metropole Hà Nội")
            put(KEY_HOTEL_RATING, 4.8)
            put(KEY_HOTEL_REVIEWS_COUNT, 1120)
            put(KEY_HOTEL_LOCATION, "Quận Hoàn Kiếm, Hà Nội")
            put(KEY_HOTEL_PRICE, 4000000.0)
            put(KEY_HOTEL_DESCRIPTION, "Khách sạn di sản mang tính biểu tượng lịch sử được xây dựng từ năm 1901 ngay trung tâm Thủ đô cổ kính. Nơi giao thoa hoàn mỹ giữa nét thanh lịch kiểu Pháp thuộc địa cổ điển và tâm hồn Việt ấm áp.")
            put(KEY_HOTEL_AMENITIES, "Wifi miễn phí,Hồ bơi nước ấm,Le Spa du Metropole,Nhà hàng ẩm thực Pháp,Hầm rượu cổ lịch sử,Phòng trà La Terrasse")
            put(KEY_HOTEL_GRADIENT, "#4CAF50,#8BC34A")
        }
        val hotelId3 = db.insert(TABLE_HOTELS, null, h3)

        // 4. The Reverie Saigon
        val h4 = ContentValues().apply {
            put(KEY_HOTEL_TITLE, "The Reverie Saigon")
            put(KEY_HOTEL_RATING, 4.9)
            put(KEY_HOTEL_REVIEWS_COUNT, 850)
            put(KEY_HOTEL_LOCATION, "Quận 1, TP. Hồ Chí Minh")
            put(KEY_HOTEL_PRICE, 6000000.0)
            put(KEY_HOTEL_DESCRIPTION, "Thành viên duy nhất của Hiệp hội các Khách sạn Hàng đầu Thế giới tại trung tâm Quận 1 phồn hoa. Tòa nhà nổi bật với phong cách nội thất thiết kế hoàng gia Ý lộng lẫy và dịch vụ xa hoa bậc nhất Sài Gòn.")
            put(KEY_HOTEL_AMENITIES, "Wifi miễn phí,Hồ bơi ngoài trời nghệ thuật,Fitness Center hiện đại,The Spa,Bãi đáp trực thăng,Bar sảnh chờ,Nhà hàng Ý đỉnh cao")
            put(KEY_HOTEL_GRADIENT, "#9C27B0,#E91E63")
        }
        val hotelId4 = db.insert(TABLE_HOTELS, null, h4)

        // Seed Rooms for Hotel 1 (Vinpearl Phú Quốc)
        val r1_1 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId1)
            put(KEY_ROOM_TITLE, "Deluxe Room Ocean View")
            put(KEY_ROOM_PRICE, 2500000.0)
            put(KEY_ROOM_DESCRIPTION, "Phòng Deluxe sang trọng ngập tràn ánh nắng tự nhiên với cửa kính lớn hướng biển rộng mở.")
            put(KEY_ROOM_SIZE, "46 m²")
            put(KEY_ROOM_CAPACITY, 2)
            put(KEY_ROOM_FEATURES, "1 Giường đôi cỡ King,Ban công hướng biển,Bồn tắm nằm cao cấp,Tivi màn hình phẳng,Mini bar")
            put(KEY_ROOM_GRADIENT, "#FF9800,#FF5722")
        }
        db.insert(TABLE_ROOMS, null, r1_1)

        val r1_2 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId1)
            put(KEY_ROOM_TITLE, "Executive Suite Ocean Front")
            put(KEY_ROOM_PRICE, 4500000.0)
            put(KEY_ROOM_DESCRIPTION, "Căn Suite thượng hạng với phòng khách riêng biệt và ban công rộng mở ngắm toàn cảnh hoàng hôn tuyệt mỹ.")
            put(KEY_ROOM_SIZE, "96 m²")
            put(KEY_ROOM_CAPACITY, 3)
            put(KEY_ROOM_FEATURES, "Phòng khách riêng sang trọng,Ban công góc biển panorama,Đặc quyền Executive Lounge,Bồn tắm massage Jacuzzi")
            put(KEY_ROOM_GRADIENT, "#FF9800,#E65100")
        }
        db.insert(TABLE_ROOMS, null, r1_2)

        // Seed Rooms for Hotel 2 (InterContinental Danang)
        val r2_1 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId2)
            put(KEY_ROOM_TITLE, "Classic Resort Room Ocean View")
            put(KEY_ROOM_PRICE, 5500000.0)
            put(KEY_ROOM_DESCRIPTION, "Thiết kế mang đậm hồn Việt tinh tế với ban công đón gió biển lộng gió hoang sơ của bán đảo Sơn Trà.")
            put(KEY_ROOM_SIZE, "70 m²")
            put(KEY_ROOM_CAPACITY, 2)
            put(KEY_ROOM_FEATURES, "Ban công thư giãn siêu rộng,Vòi sen tắm mưa ngoài trời,Bồn tắm cẩm thạch nguyên khối,Hệ thống âm thanh BOSE")
            put(KEY_ROOM_GRADIENT, "#2196F3,#00BCD4")
        }
        db.insert(TABLE_ROOMS, null, r2_1)

        val r2_2 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId2)
            put(KEY_ROOM_TITLE, "Club Peninsula Suite Private Pool")
            put(KEY_ROOM_PRICE, 9000000.0)
            put(KEY_ROOM_DESCRIPTION, "Trải nghiệm xa hoa tột cùng với hồ bơi riêng bên sườn đồi, dịch vụ quản gia phục vụ 24/7.")
            put(KEY_ROOM_SIZE, "130 m²")
            put(KEY_ROOM_CAPACITY, 3)
            put(KEY_ROOM_FEATURES, "Bể bơi vô cực riêng biệt,Quản gia riêng phục vụ 24/7,Đặc quyền Club Lounge sang trọng,Đồ ăn uống miễn phí cả ngày")
            put(KEY_ROOM_GRADIENT, "#2196F3,#0D47A1")
        }
        db.insert(TABLE_ROOMS, null, r2_2)

        // Seed Rooms for Hotel 3 (Sofitel Legend Metropole)
        val r3_1 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId3)
            put(KEY_ROOM_TITLE, "Premium Room Opera Wing")
            put(KEY_ROOM_PRICE, 4000000.0)
            put(KEY_ROOM_DESCRIPTION, "Thiết kế hiện đại mang nét cổ điển Pháp sang trọng, sàn gỗ lim ấm áp hướng ra mặt đường Tràng Tiền thơ mộng.")
            put(KEY_ROOM_SIZE, "32 m²")
            put(KEY_ROOM_CAPACITY, 2)
            put(KEY_ROOM_FEATURES, "Sàn gỗ tự nhiên cổ kính,Phòng tắm phong cách Pháp tân cổ điển,Hướng vườn hoa thơ mộng,Giường ngủ Sofitel MyBed")
            put(KEY_ROOM_GRADIENT, "#4CAF50,#8BC34A")
        }
        db.insert(TABLE_ROOMS, null, r3_1)

        val r3_2 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId3)
            put(KEY_ROOM_TITLE, "Prestige Suite Historical Wing")
            put(KEY_ROOM_PRICE, 8000000.0)
            put(KEY_ROOM_DESCRIPTION, "Nằm trong tòa nhà di sản thế kỷ của Metropole, từng đón tiếp các tiểu thuyết gia và nguyên thủ hàng đầu.")
            put(KEY_ROOM_SIZE, "64 m²")
            put(KEY_ROOM_CAPACITY, 2)
            put(KEY_ROOM_FEATURES, "Tọa lạc tại khu di sản cổ kính lâu đời,Quản gia di sản Metropole riêng,Thiết kế tân cổ điển độc bản,Trà chiều kiểu Anh")
            put(KEY_ROOM_GRADIENT, "#4CAF50,#1B5E20")
        }
        db.insert(TABLE_ROOMS, null, r3_2)

        // Seed Rooms for Hotel 4 (The Reverie Saigon)
        val r4_1 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId4)
            put(KEY_ROOM_TITLE, "Grand Deluxe River View Room")
            put(KEY_ROOM_PRICE, 6000000.0)
            put(KEY_ROOM_DESCRIPTION, "Tận hưởng không gian lộng lẫy thiết kế bởi nhà mốt Ý và ngắm nhìn trọn vẹn vẻ đẹp lung linh của sông Sài Gòn.")
            put(KEY_ROOM_SIZE, "43 m²")
            put(KEY_ROOM_CAPACITY, 2)
            put(KEY_ROOM_FEATURES, "Nội thất tơ tằm Ý xa xỉ,Cửa kính chạm sàn ngắm sông Sài Gòn,Bồn tắm massage hiện đại,Đồ dùng phòng tắm Chopard")
            put(KEY_ROOM_GRADIENT, "#9C27B0,#E91E63")
        }
        db.insert(TABLE_ROOMS, null, r4_1)

        val r4_2 = ContentValues().apply {
            put(KEY_ROOM_HOTEL_ID, hotelId4)
            put(KEY_ROOM_TITLE, "Reverie Designer Suite Executive")
            put(KEY_ROOM_PRICE, 12000000.0)
            put(KEY_ROOM_DESCRIPTION, "Kiệt tác thiết kế độc bản xa hoa bậc nhất, đi kèm xe đón tiễn siêu sang và phục vụ chuyên biệt.")
            put(KEY_ROOM_SIZE, "120 m²")
            put(KEY_ROOM_CAPACITY, 3)
            put(KEY_ROOM_FEATURES, "Thiết kế hoàng gia Ý độc bản,Dịch vụ Quản gia Reverie phục vụ 24/7,Đưa đón sân bay bằng xe siêu sang Rolls-Royce,Đặc quyền Reverie Lounge")
            put(KEY_ROOM_GRADIENT, "#9C27B0,#4A148C")
        }
        db.insert(TABLE_ROOMS, null, r4_2)

        // Seed sample bookings linked to Customer account
        val b1 = ContentValues().apply {
            put(KEY_BOOKING_USER_ID, custUserId)
            put(KEY_BOOKING_HOTEL_ID, hotelId1)
            put(KEY_BOOKING_ROOM_ID, 1) // Deluxe room
            put(KEY_BOOKING_GUEST_NAME, "Khách Hàng Vip")
            put(KEY_BOOKING_GUEST_EMAIL, "vip@gmail.com")
            put(KEY_BOOKING_GUEST_PHONE, "0987654321")
            put(KEY_BOOKING_TOTAL_PRICE, 5000000.0) // 2 nights
            put(KEY_BOOKING_STATUS, "Hoàn tất")
            put(KEY_BOOKING_DATE, "2026-05-10")
        }
        db.insert(TABLE_BOOKINGS, null, b1)

        val b2 = ContentValues().apply {
            put(KEY_BOOKING_USER_ID, custUserId)
            put(KEY_BOOKING_HOTEL_ID, hotelId2)
            put(KEY_BOOKING_ROOM_ID, 3) // Classic room
            put(KEY_BOOKING_GUEST_NAME, "Khách Hàng Vip")
            put(KEY_BOOKING_GUEST_EMAIL, "vip@gmail.com")
            put(KEY_BOOKING_GUEST_PHONE, "0987654321")
            put(KEY_BOOKING_TOTAL_PRICE, 5500000.0) // 1 night
            put(KEY_BOOKING_STATUS, "Hoàn tất")
            put(KEY_BOOKING_DATE, "2026-05-18")
        }
        db.insert(TABLE_BOOKINGS, null, b2)

        val b3 = ContentValues().apply {
            put(KEY_BOOKING_USER_ID, custUserId)
            put(KEY_BOOKING_HOTEL_ID, hotelId3)
            put(KEY_BOOKING_ROOM_ID, 5) // Premium room
            put(KEY_BOOKING_GUEST_NAME, "Lê Hoàng Nam")
            put(KEY_BOOKING_GUEST_EMAIL, "namlh@example.com")
            put(KEY_BOOKING_GUEST_PHONE, "0901234567")
            put(KEY_BOOKING_TOTAL_PRICE, 8000000.0) // 2 nights
            put(KEY_BOOKING_STATUS, "Chờ xác nhận")
            put(KEY_BOOKING_DATE, "2026-05-25")
        }
        db.insert(TABLE_BOOKINGS, null, b3)
    }
}

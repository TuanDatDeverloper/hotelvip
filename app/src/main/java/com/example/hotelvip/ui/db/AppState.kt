package com.example.hotelvip.ui.db

object AppState {
    var userId: Int = 2 // Default to customer VIP seeded user ID
    var userRole: String = "CUSTOMER" // Default role
    
    var selectedHotelId: Int = 1
    var selectedRoomId: Int = 1
    
    // Guest information for the booking flow
    var currentGuestName: String = "Khách Hàng Vip"
    var currentGuestEmail: String = "vip@gmail.com"
    var currentGuestPhone: String = "0987654321"
}

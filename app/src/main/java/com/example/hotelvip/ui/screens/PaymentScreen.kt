package com.example.hotelvip.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotelvip.Routes
import com.example.hotelvip.ui.db.AppState
import com.example.hotelvip.ui.db.Booking
import com.example.hotelvip.ui.db.Hotel
import com.example.hotelvip.ui.db.HotelRepository
import com.example.hotelvip.ui.db.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, repository: HotelRepository) {
    var hotel by remember { mutableStateOf<Hotel?>(null) }
    var room by remember { mutableStateOf<Room?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedMethod by remember { mutableStateOf(0) } // 0: Credit card, 1: E-wallets, 2: Bank transfer

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        hotel = repository.getHotelById(AppState.selectedHotelId)
        room = repository.getRoomById(AppState.selectedRoomId)
        isLoading = false
    }

    Scaffold(
        containerColor = Color(0xFFFBFBFB),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chi tiết thanh toán", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Trang chủ") },
                    selected = false,
                    onClick = { navController.navigate(Routes.USER_ROOT) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Tìm kiếm") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text("Lịch đặt") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.BookmarkBorder, contentDescription = null) },
                    label = { Text("Đã lưu") },
                    selected = false,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Cá nhân") },
                    selected = false,
                    onClick = {}
                )
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (hotel == null || room == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy thông tin.")
            }
        } else {
            val basePrice = room!!.price * 2
            val serviceFee = basePrice * 0.125 // Matching ~300k if base is 2.4M
            val discount = 100000.0
            val totalAmount = basePrice + serviceFee - discount

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Hotel Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hotel!!.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(room!!.title, fontSize = 14.sp, color = Color.Gray)
                        Text("2 người lớn, 1 phòng", fontSize = 13.sp, color = Color.Gray)
                        Text("15/06/2024 - 17/06/2024 (2 đêm)", fontSize = 13.sp, color = Color.Gray)
                    }
                }

                Text(
                    "Phương thức thanh toán",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column {
                        PaymentMethodItem(
                            selected = selectedMethod == 0,
                            icon = { Icon(Icons.Default.CreditCard, null, tint = Color(0xFF1976D2)) },
                            title = "Thẻ tín dụng/Ghi nợ",
                            subtitle = "Thẻ đã lưu **** .... 4567",
                            badges = {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    BadgeBox("VISA", Color(0xFF1A1F71))
                                    BadgeBox("MC", Color(0xFFFF5F00))
                                }
                            },
                            onClick = { selectedMethod = 0 }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                        PaymentMethodItem(
                            selected = selectedMethod == 1,
                            icon = { Icon(Icons.Default.BookOnline, null, tint = Color(0xFFE91E63)) },
                            title = "Ví điện tử",
                            badges = {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    BadgeBox("MoMo", Color(0xFFA50064))
                                    BadgeBox("Zalo", Color(0xFF0088FF))
                                }
                            },
                            onClick = { selectedMethod = 1 }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                        PaymentMethodItem(
                            selected = selectedMethod == 2,
                            icon = { Icon(painterResource(android.R.drawable.ic_menu_agenda), null, tint = Color.Gray) },
                            title = "Chuyển khoản ngân hàng",
                            subtitle = "Số tài khoản 102306-XXXX-XXXX",
                            onClick = { selectedMethod = 2 }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 8.dp)

                Text(
                    "Chi tiết giá",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    PriceRow("Giá gốc", basePrice)
                    PriceRow("Phí dịch vụ", serviceFee)
                    PriceRow("Giảm giá", -discount, color = Color(0xFFE91E63))
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Tổng cộng", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("%,.0f đ".format(totalAmount), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val newBooking = Booking(
                                hotelId = hotel!!.id,
                                roomId = room!!.id,
                                guestName = AppState.currentGuestName.ifEmpty { "Khách hàng VIP" },
                                guestEmail = AppState.currentGuestEmail.ifEmpty { "vipcustomer@gmail.com" },
                                guestPhone = AppState.currentGuestPhone.ifEmpty { "0987654321" },
                                totalPrice = totalAmount
                            )
                            repository.addBooking(newBooking)
                            Toast.makeText(context, "Thanh toán thành công!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.USER_ROOT)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Thanh toán ngay %,.0f đ".format(totalAmount), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PaymentMethodItem(selected: Boolean, icon: @Composable () -> Unit, title: String, subtitle: String? = null, badges: @Composable (() -> Unit)? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF1F4F8), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { icon() }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (subtitle != null) Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        if (badges != null) badges()
    }
}

@Composable
fun BadgeBox(text: String, color: Color) {
    Box(modifier = Modifier.background(color, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
        Text(text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PriceRow(label: String, value: Double, color: Color = Color.Black) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text("${if (value < 0) "-" else ""}${"%,.0f".format(kotlin.math.abs(value))} đ", color = color, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

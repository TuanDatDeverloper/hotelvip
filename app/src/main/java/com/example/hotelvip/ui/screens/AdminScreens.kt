package com.example.hotelvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotelvip.ui.db.AdminReport
import com.example.hotelvip.ui.db.HotelRepository
import com.example.hotelvip.ui.db.MonthlyRevenue
import com.example.hotelvip.ui.db.RecentBookingInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController, repository: HotelRepository) {
    var report by remember { mutableStateOf<AdminReport?>(null) }
    var monthlyRevenues by remember { mutableStateOf<List<MonthlyRevenue>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        report = repository.getAdminReport()
        monthlyRevenues = repository.getRevenueByMonth()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảng Điều Khiển Quản Trị", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_revert),
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading || report == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Key Metrics
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MetricCard(
                            title = "Tổng doanh thu",
                            value = "%,.0fđ".format(report!!.totalRevenue),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            title = "Tổng đơn đặt",
                            value = "${report!!.totalBookingsCount} đơn",
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    MetricCard(
                        title = "Tỷ lệ phòng lấp đầy",
                        value = "%,.1f%%".format(report!!.roomOccupancyRate),
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Simple Visual Bar Chart for Revenue
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Doanh Thu 6 Tháng Gần Nhất (Triệu VNĐ)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                monthlyRevenues.forEach { item ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(44.dp)
                                    ) {
                                        Text(text = "%.0f".format(item.revenue), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        
                                        // Bar representation
                                        Box(
                                            modifier = Modifier
                                                .width(16.dp)
                                                .height((item.revenue * 1.5).coerceAtMost(90.0).dp)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = item.monthName, fontSize = 9.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }

                // Recent Transactions List
                item {
                    Text("5 Đơn Đặt Phòng Gần Nhất", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
                }

                if (report!!.recentBookings.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text("Chưa có đơn đặt phòng nào.", color = Color.Gray)
                        }
                    }
                } else {
                    items(report!!.recentBookings) { booking ->
                        BookingTransactionCard(booking)
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, color: Color, textColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
fun BookingTransactionCard(booking: RecentBookingInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.guestName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                Spacer(modifier = Modifier.height(2.dp))
                Text("${booking.hotelTitle} - ${booking.roomTitle}", fontSize = 11.sp, color = Color.Gray)
                Text("Ngày đặt: ${booking.date}", fontSize = 10.sp, color = Color.LightGray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%,.0fđ".format(booking.totalPrice),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(
                            if (booking.status == "Hoàn tất") Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFFFF9800).copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = booking.status,
                        fontSize = 9.sp,
                        color = if (booking.status == "Hoàn tất") Color(0xFF4CAF50) else Color(0xFFFF9800),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

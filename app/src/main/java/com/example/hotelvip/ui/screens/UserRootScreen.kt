package com.example.hotelvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hotelvip.Routes
import com.example.hotelvip.ui.db.HotelRepository
import com.example.hotelvip.ui.db.RecentBookingInfo

@Composable
fun UserRootScreen(rootNavController: NavController, repository: HotelRepository) {
    val bottomNavController = rememberNavController()
    
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(if (currentRoute == Routes.MAIN_TAB_HOME) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Trang chủ") },
                    selected = currentRoute == Routes.MAIN_TAB_HOME,
                    onClick = {
                        bottomNavController.navigate(Routes.MAIN_TAB_HOME) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Tìm kiếm") },
                    selected = currentRoute == Routes.MAIN_TAB_SEARCH,
                    onClick = {
                        bottomNavController.navigate(Routes.MAIN_TAB_SEARCH) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(if (currentRoute == Routes.MAIN_TAB_BOOKINGS) Icons.Filled.DateRange else Icons.Outlined.DateRange, contentDescription = null) },
                    label = { Text("Lịch đặt") },
                    selected = currentRoute == Routes.MAIN_TAB_BOOKINGS,
                    onClick = {
                        bottomNavController.navigate(Routes.MAIN_TAB_BOOKINGS) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Cá nhân") },
                    selected = currentRoute == Routes.MAIN_TAB_PROFILE,
                    onClick = {
                        bottomNavController.navigate(Routes.MAIN_TAB_PROFILE) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Routes.MAIN_TAB_HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.MAIN_TAB_HOME) { HomeScreen(rootNavController, repository) }
            composable(Routes.MAIN_TAB_SEARCH) { SearchScreen(rootNavController) }
            composable(Routes.MAIN_TAB_BOOKINGS) { BookingsHistoryTab(repository) }
            composable(Routes.MAIN_TAB_PROFILE) { ProfileTab(rootNavController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsHistoryTab(repository: HotelRepository) {
    var bookings by remember { mutableStateOf<List<RecentBookingInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        bookings = repository.getBookings()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lịch Sử Đặt Phòng", fontSize = 16.sp, fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Chưa có lịch sử đặt phòng nào.", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(bookings) { booking ->
                    BookingHistoryCard(booking)
                }
            }
        }
    }
}

@Composable
fun BookingHistoryCard(booking: RecentBookingInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(booking.hotelTitle, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                Spacer(modifier = Modifier.height(2.dp))
                Text("Hạng phòng: ${booking.roomTitle}", fontSize = 12.sp, color = Color.Gray)
                Text("Người nhận: ${booking.guestName}", fontSize = 11.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Ngày đặt: ${booking.date}", fontSize = 10.sp, color = Color.LightGray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "%,.0fđ".format(booking.totalPrice),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
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
                        fontSize = 10.sp,
                        color = if (booking.status == "Hoàn tất") Color(0xFF4CAF50) else Color(0xFFFF9800),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTab(rootNavController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hồ Sơ Cá Nhân", fontSize = 16.sp, fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // User Avatar mockup
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Khách Hàng Vip", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("vipcustomer@gmail.com", fontSize = 12.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Switch to Admin Button
            Button(
                onClick = { rootNavController.navigate(Routes.ADMIN_ROOT) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chuyển Sang Quản Trị Viên (Admin)", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Log out Button
            OutlinedButton(
                onClick = { rootNavController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng Xuất", fontWeight = FontWeight.Bold)
            }
        }
    }
}

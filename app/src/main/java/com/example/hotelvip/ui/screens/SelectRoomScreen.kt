package com.example.hotelvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hotelvip.Routes
import com.example.hotelvip.ui.db.AppState
import com.example.hotelvip.ui.db.HotelRepository
import com.example.hotelvip.ui.db.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRoomScreen(navController: NavController, repository: HotelRepository) {
    var rooms by remember { mutableStateOf<List<Room>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(AppState.selectedHotelId) {
        rooms = repository.getRoomsForHotel(AppState.selectedHotelId)
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chọn Hạng Phòng", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (rooms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy phòng trống nào.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(rooms) { room ->
                    RoomCard(room, navController)
                }
            }
        }
    }
}

@Composable
fun RoomCard(room: Room, navController: NavController) {
    val gradientColors = remember(room.gradientColors) {
        try {
            listOf(Color(android.graphics.Color.parseColor(room.gradientColors[0])), Color(android.graphics.Color.parseColor(room.gradientColors[1])))
        } catch (e: Exception) {
            listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
        }
    }

    // Set static premium image from Unsplash for beautiful room matching UI
    val imgUrl = when(room.id) {
        1, 3, 5, 7 -> "https://images.unsplash.com/photo-1618773928121-c32242e63f39?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Deluxe
        else -> "https://images.unsplash.com/photo-1590490360182-c33d57733427?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Suite
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    model = imgUrl,
                    contentDescription = room.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                )
                Text(
                    text = room.size + " | Sức chứa: " + room.capacity + " khách",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(room.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                Spacer(modifier = Modifier.height(4.dp))
                Text(room.description, fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Show features / details
                Text("Đặc quyền & Tiện ích:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Spacer(modifier = Modifier.height(6.dp))
                
                room.features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(gradientColors[0])
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(feature, fontSize = 11.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Giá phòng một đêm", fontSize = 10.sp, color = Color.Gray)
                        Text(
                            text = "%,.0fđ".format(room.price),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = gradientColors[1]
                        )
                    }
                    Button(
                        onClick = {
                            AppState.selectedRoomId = room.id
                            navController.navigate(Routes.BOOKING_INFO)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = gradientColors[1]),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Đặt Ngay", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

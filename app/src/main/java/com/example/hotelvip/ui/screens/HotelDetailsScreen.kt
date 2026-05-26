package com.example.hotelvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import com.example.hotelvip.ui.db.Hotel
import com.example.hotelvip.ui.db.HotelRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailsScreen(navController: NavController, repository: HotelRepository) {
    var hotel by remember { mutableStateOf<Hotel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(AppState.selectedHotelId) {
        hotel = repository.getHotelById(AppState.selectedHotelId)
        isLoading = false
    }

    val gradientColors = remember(hotel) {
        hotel?.gradientColors?.let { colors ->
            try {
                listOf(Color(android.graphics.Color.parseColor(colors[0])), Color(android.graphics.Color.parseColor(colors[1])))
            } catch (e: Exception) {
                listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
            }
        } ?: listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
    }

    val hotelImage = remember(AppState.selectedHotelId) {
        when(AppState.selectedHotelId) {
            1 -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
            2 -> "https://images.unsplash.com/photo-1540541338287-41700207dee6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
            3 -> "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
            else -> "https://images.unsplash.com/photo-1582719478250-c89cae4db85b?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"
        }
    }

    val listDetailImages = remember(AppState.selectedHotelId) {
        listOf(
            "https://images.unsplash.com/photo-1571896349842-33c89424de2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80",
            "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80",
            "https://images.unsplash.com/photo-1566665797739-1674de7a421a?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80",
            "https://images.unsplash.com/photo-1484154218962-a197022b5858?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(hotel?.title ?: "Chi Tiết Khách Sạn", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_revert),
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (hotel != null) {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Giá phòng trung bình", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = "%,.0fđ".format(hotel!!.price) + "/đêm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = gradientColors[1]
                            )
                        }
                        Button(
                            onClick = { navController.navigate(Routes.SELECT_ROOM) },
                            colors = ButtonDefaults.buttonColors(containerColor = gradientColors[1])
                        ) {
                            Text("Chọn Loại Phòng", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (hotel == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy dữ liệu khách sạn.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Main Photo Hero Banner with beautiful border radius
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)) {
                    AsyncImage(
                        model = hotelImage,
                        contentDescription = hotel!!.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("1 / 5 Hình ảnh", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(hotel!!.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(hotel!!.rating.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("(${hotel!!.reviewsCount} đánh giá khách hàng thực tế)", color = Color.Gray, fontSize = 12.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(hotel!!.location, color = gradientColors[0], fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Tiện Nghi Nổi Bật", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Render dynamic facility icons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        hotel!!.amenities.take(4).forEach { amenity ->
                            Box(modifier = Modifier.weight(1f)) {
                                FacilityItem(amenity, gradientColors[0])
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Thông Tin Về Khách Sạn", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = hotel!!.description,
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Hình Ảnh & Không Gian", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(listDetailImages) { img ->
                            AsyncImage(
                                model = img,
                                contentDescription = "Detail Photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(130.dp, 100.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun FacilityItem(name: String, themeColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(themeColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_info_details),
                contentDescription = null,
                tint = themeColor,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            maxLines = 1
        )
    }
}

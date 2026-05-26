package com.example.hotelvip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
fun HomeScreen(navController: NavController, repository: HotelRepository) {
    var hotels by remember { mutableStateOf<List<Hotel>>(emptyList()) }
    var searchFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hotels = repository.getHotels()
        searchFinished = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp), contentAlignment = Alignment.CenterStart) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable { navController.navigate(Routes.MAIN_TAB_SEARCH) }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                                Text("Bạn muốn đi đâu nghỉ dưỡng?", color = Color.Gray, fontSize = 14.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.ADMIN_ROOT) }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Chuyển sang Admin",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                // Premium Promo Card (Hi ! Hotel Brand Card with Glassmorphism concept)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(180.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1571896349842-33c89424de2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
                            contentDescription = "Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text("Chào mừng đến với Hi ! Hotel", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("Trải nghiệm kỳ nghỉ dưỡng 5 sao mơ ước của bạn tại Việt Nam", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Text("Điểm Đến Nổi Tiếng", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val destinations = listOf(
                        Triple("Phú Quốc", "1540541338287-41700207dee6", "Phú Quốc"),
                        Triple("Đà Nẵng", "1507526300108-238b0115f3f3", "Đà Nẵng"),
                        Triple("Hà Nội", "1509060464153-a29ab2e408e0", "Hà Nội"),
                        Triple("TP. Hồ Chí Minh", "1527245386804-ac316538c2f1", "TP. Hồ Chí Minh")
                    )
                    items(destinations) { dest ->
                        Box(
                            modifier = Modifier
                                .size(140.dp, 90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    navController.navigate(Routes.MAIN_TAB_SEARCH)
                                }
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-${dest.second}?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80",
                                contentDescription = dest.first,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                                        )
                                    )
                            )
                            Box(modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)) {
                                Text(dest.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            item {
                Text("Khách Sạn Đề Xuất Cho Bạn", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            
            if (hotels.isEmpty() && !searchFinished) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (hotels.isEmpty()) {
                item {
                    Text("Không tìm thấy khách sạn nào.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            } else {
                items(hotels) { hotel ->
                    RecommendedHotelCard(hotel, navController)
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RecommendedHotelCard(hotel: Hotel, navController: NavController) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val gradientColors = remember(hotel.gradientColors) {
        try {
            listOf(Color(android.graphics.Color.parseColor(hotel.gradientColors[0])), Color(android.graphics.Color.parseColor(hotel.gradientColors[1])))
        } catch (e: Exception) {
            listOf(primaryColor, secondaryColor)
        }
    }

    // Set static premium image from Unsplash for beautiful UI match
    val imgUrl = when(hotel.id) {
        1 -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Vinpearl
        2 -> "https://images.unsplash.com/photo-1540541338287-41700207dee6?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Danang
        3 -> "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Metropole
        else -> "https://images.unsplash.com/photo-1582719478250-c89cae4db85b?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80" // Reverie
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                AppState.selectedHotelId = hotel.id
                navController.navigate(Routes.HOTEL_DETAILS)
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(colors = listOf(Color.White, Color(0xFFF9F9F9))))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imgUrl,
                contentDescription = hotel.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp, 90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(hotel.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E2E2E))
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(hotel.rating.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(" (${hotel.reviewsCount} đánh giá)", fontSize = 11.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                
                // Location badge with custom gradient pill
                Box(
                    modifier = Modifier
                        .background(gradientColors[0].copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(hotel.location, fontSize = 10.sp, color = gradientColors[0], fontWeight = FontWeight.SemiBold)
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Giá từ:", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = "%,.0fđ".format(hotel.price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = gradientColors[1]
                    )
                }
            }
        }
    }
}

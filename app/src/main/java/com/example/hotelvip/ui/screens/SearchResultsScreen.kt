package com.example.hotelvip.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hotelvip.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Kết quả tìm kiếm", fontWeight = FontWeight.Bold)
                        Text("Đà Nẵng • 15-17/06 • 2 khách • 1 phòng", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_revert), contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = {}) {
                        Text("Lọc")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(5) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { navController.navigate(Routes.HOTEL_DETAILS) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1540541338287-41700207dee6?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&q=80",
                            contentDescription = "Hotel",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(100.dp, 120.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f).height(120.dp), verticalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Sala Danang Beach Hotel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                    Text("4.5 (312)", fontSize = 14.sp)
                                }
                            }
                            Column {
                                Text("1,300,000 đ", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.align(Alignment.End))
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                                        Text("Cách trung tâm 5.5 km", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Button(onClick = { navController.navigate(Routes.HOTEL_DETAILS) }, modifier = Modifier.height(36.dp), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)) {
                                        Text("Chọn")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



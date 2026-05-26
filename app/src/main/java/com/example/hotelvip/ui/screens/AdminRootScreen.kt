package com.example.hotelvip.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hotelvip.Routes

import com.example.hotelvip.ui.db.HotelRepository

@Composable
fun AdminRootScreen(rootNavController: NavController, repository: HotelRepository) {
    var selectedItem by remember { mutableStateOf(0) }
    
    val items = listOf("Bảng điều khiển", "Khách sạn", "Đơn đặt", "Doanh thu", "Khách hàng", "Cài đặt")
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationCity, Icons.Default.DateRange, Icons.Default.AccountBalance, Icons.Default.Person, Icons.Default.Settings)
    
    // Instead of actual navHost for simplicity on tablet-like sidebar, use state
    
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(modifier = Modifier.width(120.dp)) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
        
        Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
            when(selectedItem) {
                0 -> AdminDashboardScreen(rootNavController, repository)
                // We will build a simple dashboard and the others can just route or show placeholders
                else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { Text(items[selectedItem]) }
            }
        }
    }
}



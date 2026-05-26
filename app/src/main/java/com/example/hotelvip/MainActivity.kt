package com.example.hotelvip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.hotelvip.ui.theme.HotelvipTheme
import com.example.hotelvip.ui.db.DatabaseHelper
import com.example.hotelvip.ui.db.HotelRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val dbHelper = DatabaseHelper(this)
        val repository = HotelRepository(dbHelper)
        
        enableEdgeToEdge()
        setContent {
            HotelvipTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainApp(repository)
                    }
                }
            }
        }
    }
}

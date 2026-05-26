package com.example.hotelvip.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hotelvip.Routes
import com.example.hotelvip.ui.db.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInfoScreen(navController: NavController) {
    var name by remember { mutableStateOf(AppState.currentGuestName) }
    var email by remember { mutableStateOf(AppState.currentGuestEmail) }
    var phone by remember { mutableStateOf(AppState.currentGuestPhone) }
    
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông Tin Liên Hệ", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Nhập thông tin người đặt phòng", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Vui lòng nhập chính xác để chúng tôi gửi hóa đơn và thông tin nhận phòng.", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("Họ và Tên") },
                isError = nameError,
                supportingText = { if (nameError) Text("Vui lòng nhập họ và tên") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                label = { Text("Địa chỉ Email") },
                isError = emailError,
                supportingText = { if (emailError) Text("Vui lòng nhập địa chỉ email hợp lệ") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = false
                },
                label = { Text("Số Điện Thoại") },
                isError = phoneError,
                supportingText = { if (phoneError) Text("Vui lòng nhập số điện thoại liên hệ") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = {
                    var hasError = false
                    if (name.trim().isEmpty()) {
                        nameError = true
                        hasError = true
                    }
                    if (email.trim().isEmpty() || !email.contains("@")) {
                        emailError = true
                        hasError = true
                    }
                    if (phone.trim().isEmpty()) {
                        phoneError = true
                        hasError = true
                    }
                    
                    if (!hasError) {
                        // Save information to global AppState
                        AppState.currentGuestName = name
                        AppState.currentGuestEmail = email
                        AppState.currentGuestPhone = phone
                        
                        navController.navigate(Routes.PAYMENT)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Tiếp Tục Thanh Toán", fontWeight = FontWeight.Bold)
            }
        }
    }
}

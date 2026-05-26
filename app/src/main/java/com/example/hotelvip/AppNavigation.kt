package com.example.hotelvip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hotelvip.ui.screens.*
import com.example.hotelvip.ui.db.HotelRepository

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val USER_ROOT = "user_root"
    
    // User Flow
    const val HOME = "home"
    const val SEARCH = "search"
    const val SEARCH_RESULTS = "search_results"
    const val HOTEL_DETAILS = "hotel_details"
    const val SELECT_ROOM = "select_room"
    const val BOOKING_INFO = "booking_info"
    const val PAYMENT = "payment"
    
    // Bottom Nav (User)
    const val MAIN_TAB_HOME = "main_tab_home"
    const val MAIN_TAB_SEARCH = "main_tab_search"
    const val MAIN_TAB_BOOKINGS = "main_tab_bookings"
    const val MAIN_TAB_SAVED = "main_tab_saved"
    const val MAIN_TAB_PROFILE = "main_tab_profile"

    // Admin Flow
    const val ADMIN_ROOT = "admin_root"
}


@Composable
fun MainApp(repository: HotelRepository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.LOGIN) { LoginScreen(navController, repository) }
        composable(Routes.SIGNUP) { SignupScreen(navController, repository) }
        composable(Routes.USER_ROOT) { UserRootScreen(navController, repository) }
        composable(Routes.SEARCH_RESULTS) { SearchResultsScreen(navController) }
        composable(Routes.HOTEL_DETAILS) { HotelDetailsScreen(navController, repository) }
        composable(Routes.SELECT_ROOM) { SelectRoomScreen(navController, repository) }
        composable(Routes.BOOKING_INFO) { BookingInfoScreen(navController) }
        composable(Routes.PAYMENT) { PaymentScreen(navController, repository) }
        
        // Admin
        composable(Routes.ADMIN_ROOT) { AdminRootScreen(navController, repository) }
    }
}



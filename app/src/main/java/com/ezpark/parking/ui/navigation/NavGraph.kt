package com.ezpark.parking.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ezpark.parking.ui.screens.detail.ParkingLotDetailScreen
import com.ezpark.parking.ui.screens.map.MapScreen

object Routes {
    const val MAP = "map"
    const val DETAIL = "detail/{lotId}"
    fun detail(lotId: String) = "detail/$lotId"
}

@Composable
fun EzparkNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.MAP) {
        composable(Routes.MAP) {
            MapScreen(onNavigateToDetail = { lotId -> navController.navigate(Routes.detail(lotId)) })
        }
        composable(Routes.DETAIL) { backStackEntry ->
            val lotId = backStackEntry.arguments?.getString("lotId") ?: "unknown"
            ParkingLotDetailScreen(lotId = lotId, onBack = { navController.popBackStack() })
        }
    }
}

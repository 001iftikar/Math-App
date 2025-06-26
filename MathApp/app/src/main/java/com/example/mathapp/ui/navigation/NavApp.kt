package com.example.mathapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.ui.home.HomeScreen
import com.example.mathapp.ui.teacher.TeacherScreen

@Composable
fun NavApp(modifier: Modifier) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HomeScreenRoute) {
        composable<Routes.HomeScreenRoute> {
            HomeScreen(navHostController = navController)
        }

        composable<Routes.TeacherScreenRoute> {
            TeacherScreen()
        }
    }
}
package com.example.localngalam.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.presentation.home.HomeScreen
import com.example.localngalam.presentation.login.LoginScreen
import com.example.localngalam.presentation.register.RegisterScreen
import androidx.navigation.compose.composable
import com.example.localngalam.presentation.history.HistoryScreen
import com.example.localngalam.presentation.profile.ProfileScreen
import com.example.localngalam.presentation.search.SearchScreen

@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}
package com.example.localngalam.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.localngalam.presentation.home.HomeScreen
import androidx.navigation.compose.composable
import com.example.localngalam.presentation.auth.LogoutScreen
import com.example.localngalam.presentation.history.HistoryScreen
import com.example.localngalam.presentation.profile.ProfileScreen
import com.example.localngalam.presentation.createPlan.CreatePlanScreen1
import com.example.localngalam.presentation.login.LoginScreen
import com.example.localngalam.presentation.register.RegisterScreen
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen1
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen2
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen4
import com.example.localngalam.presentation.search.SearchScreen
import tempatScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "test") //Buat Test Filter Data ntar pindahin ke login aja lagi
    {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("sendemail") { ResetPasswordScreen1(navController) }
        composable("verifemail") { ResetPasswordScreen2(navController) }
        composable("resetpassword") { ResetPasswordScreen4(navController) }
        composable("add_plan") { CreatePlanScreen1(navController) }
        composable("test") { tempatScreen(navController) }
        composable("logout") { LogoutScreen(navController) } }
    }

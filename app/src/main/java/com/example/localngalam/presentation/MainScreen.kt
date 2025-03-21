package com.example.localngalam.presentation

import Tempat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import com.example.localngalam.presentation.history.HistoryScreen
import com.example.localngalam.presentation.profile.ProfileScreen

import com.example.localngalam.presentation.createPlan.CreatePlanScreen1
import com.example.localngalam.presentation.createPlan.CreatePlanScreen2
import com.example.localngalam.presentation.createPlan.CreatePlanScreen3
import com.example.localngalam.presentation.home.DetailTempatScreen
import com.example.localngalam.presentation.home.HomeScreen
import com.example.localngalam.presentation.login.LoginScreen
import com.example.localngalam.presentation.register.RegisterScreen
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen1
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen2
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen4
import com.example.localngalam.presentation.search.SearchScreen
import okhttp3.Route
import tempatScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController, onClick = {
            navigateToDetail(navController, it)
        }) }
        composable("search") { SearchScreen(navController, onClick = {
            navigateToDetail(navController, it)
        }) }
        composable("history") { HistoryScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("sendemail") { ResetPasswordScreen1(navController) }
        composable("verifemail") { ResetPasswordScreen2(navController) }
        composable("resetpassword") { ResetPasswordScreen4(navController) }
        composable("tesimport") { tempatScreen(navController) }
        composable("add_plan") { CreatePlanScreen1(navController) }
        composable("create_plan_2") { CreatePlanScreen2(navController) }
        composable("create_plan_3") { CreatePlanScreen3(navController)}
        composable("detail_tempat") {
            navController.previousBackStackEntry?.savedStateHandle?.get<Tempat>("tempat")
                ?.let {
                    DetailTempatScreen(navController, tempat = it)
                }
             }

        }
    }
private fun navigateToDetail(navController: NavController, tempat: Tempat ){
    navController.currentBackStackEntry?.savedStateHandle?.set("tempat",tempat)
    navController.navigate(
        route = "detail_tempat"
    )
}

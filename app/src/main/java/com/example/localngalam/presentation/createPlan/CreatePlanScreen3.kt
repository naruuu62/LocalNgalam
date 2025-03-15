package com.example.localngalam.createPlan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.ButtonTypeTripPlan
import com.example.localngalam.presentation.ui_component.Navbar2
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun CreatePlanScreen3(navController: NavController) {
    var selectedTripType by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            Navbar2(
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onPlusClick = { navController.navigate("add_plan") },
                onHistoryClick = { navController.navigate("history") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        }
    }
}

@Preview(showBackground = false, device = "spec:width=412dp,height=917dp")
@Composable
private fun Preview() {
    val navController = androidx.navigation.compose.rememberNavController()
    CreatePlanScreen3(navController = navController)
}
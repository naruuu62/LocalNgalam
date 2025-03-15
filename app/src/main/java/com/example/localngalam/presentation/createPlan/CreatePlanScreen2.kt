package com.example.localngalam.presentation.createPlan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.localngalam.presentation.ui.theme.poppinsFont
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.Navbar2
import java.time.LocalDate

@Composable
fun CreatePlanScreen2(navController: NavController) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var daysCount by remember { mutableStateOf<Long?>(null) }
    var planName by remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.height(61.dp))


                Text(
                text = "Malang Trip",
                fontSize = 16.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Bold,
                color = Blue2
                )

                Text(
                    text = "2 of 4",
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFCCCCCC),
                    letterSpacing = 0.5.sp,
                )


            Text(
                text = "Jenis perjalanan apa yang Anda Rencanakan?",
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    color = Blue3,
                    letterSpacing = 0.5.sp,
                )

            Row (modifier = Modifier.width(295.dp)
            ){

                Text(
                    text = "Pilih salah satu.",
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFCCCCCC),
                        letterSpacing = 0.5.sp,
                    )

            }




        }
    }
}

@Preview(showBackground = false, device = "spec:width=412dp,height=917dp")
@Composable
private fun Preview() {
    val navController = rememberNavController()
    CreatePlanScreen2(navController = navController)
}


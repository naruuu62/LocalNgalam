package com.example.localngalam.createPlan

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
fun CreatePlanScreen2(navController: NavController) {
    var selectedTripType by remember { mutableStateOf<String?>(null) }
    //VARIABLE JENIS PERJALANAM /solo /friendship /romance /family
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.BackButton
import com.example.localngalam.presentation.ui_component.Calendar
import com.example.localngalam.presentation.ui_component.TextFieldCreatePlan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.ButtonPrevCreatePlan
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

            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = painterResource(id = R.drawable.loading_2_of_4),
                contentDescription = "image description",
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Jenis perjalanan apa yang Anda Rencanakan?",
                fontSize = 12.sp,
                lineHeight = 24.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Blue3,
                letterSpacing = 0.5.sp,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(modifier = Modifier.width(295.dp)) {
                Text(
                    text = "Pilih salah satu.",
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

            }

            Spacer(modifier = Modifier.height(25.dp))

            Row {
                ButtonTypeTripPlan(
                    text = "Perjalanan Solo",
                    iconResId = R.drawable.ic_solo_trip,
                    isSelected = selectedTripType == "solo",
                    onClick = { selectedTripType = "solo" }
                )

                Spacer(modifier = Modifier.width(25.dp))

                ButtonTypeTripPlan(
                    text = "Perjalanan Sahabat",
                    iconResId = R.drawable.ic_friendship_trip,
                    isSelected = selectedTripType == "friendship",
                    onClick = { selectedTripType = "friendship" }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Row {
                ButtonTypeTripPlan(
                    text = "Perjalanan\nRomantis",
                    iconResId = R.drawable.ic_romance_trip,
                    isSelected = selectedTripType == "romance",
                    onClick = { selectedTripType = "romance" }
                )

                Spacer(modifier = Modifier.width(25.dp))

                ButtonTypeTripPlan(
                    text = "Perjalanan Keluarga",
                    iconResId = R.drawable.ic_family_trip,
                    isSelected = selectedTripType == "family",
                    onClick = { selectedTripType = "family" }
                )
            }

            Spacer(modifier = Modifier.height(72.dp))

            Row(
                modifier = Modifier.width(325.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ButtonNextCreatePlan(
                    onClick = { navController.navigate("add_plan_route") },
                    enabled = selectedTripType != null
                )
            }


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
    val navController = androidx.navigation.compose.rememberNavController()
    CreatePlanScreen2(navController = navController)
}


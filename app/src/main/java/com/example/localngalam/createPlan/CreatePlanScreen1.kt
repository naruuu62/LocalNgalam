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
fun CreatePlanScreen1(navController: NavController) {
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
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp)
            ){
                BackButton(
                onClick = { navController.popBackStack() }
            )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Membuat Perjalanan",
                    fontSize = 16.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = Blue2,
                )
            }

            Spacer(modifier = Modifier.height(23.dp))

            Text(
                text = "Nama Perjalanan",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Blue3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp)
            )

            TextFieldCreatePlan(
                value = planName,
                onValueChange = {planName = it}, //VARIABEL NYIMPAN NAMA PERJALANAN
                placeholderText = "Tambahkan nama perjalanan",
                modifier = Modifier
                    .padding( top = 6.dp, start = 31.dp, end = 31.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Jadwal",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Blue3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Calendar(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically)
                        .shadow(elevation = 4.dp, spotColor = Color.Black, ambientColor = Color.Black)
                ) {
                  selectedStart, selectedEnd, totalDays ->
                    startDate = selectedStart //VARIABEL TANGGAL MULAI
                    endDate = selectedEnd //VARIABEL TANGGAL AKHIR
                    daysCount = totalDays //VARIABEL TOTAL HARI
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row (
                modifier = Modifier.padding(start = 22.dp,end= 22.dp)
            ){
                ButtonPrevCreatePlan(
                    onClick = { navController.popBackStack()}
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNextCreatePlan(
                    onClick = {},
                    enabled = planName.isNotBlank() && startDate != null && endDate != null
                )
            }
        }
    }
}

@Preview(showBackground = false, device = "spec:width=412dp,height=917dp")
@Composable
private fun Preview() {
    val navController = androidx.navigation.compose.rememberNavController()
    CreatePlanScreen1(navController = navController)
}


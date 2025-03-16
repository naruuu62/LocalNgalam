package com.example.localngalam.presentation.createPlan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.localngalam.presentation.ui_component.BackButton
import com.example.localngalam.presentation.ui_component.Calendar
import com.example.localngalam.presentation.ui_component.TextFieldCreatePlan
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.saveToFirestoreJadwal
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.ButtonPrevCreatePlan
import com.example.localngalam.presentation.ui_component.Navbar2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import planViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePlanScreen1(navController: NavController) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDates by remember { mutableStateOf<List<LocalDate>>(emptyList()) }
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp)
            ) {
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
                onValueChange = { planName = it }, //VARIABEL NYIMPAN NAMA PERJALANAN
                placeholderText = "Tambahkan nama perjalanan",
                modifier = Modifier
                    .padding(top = 6.dp, start = 31.dp, end = 31.dp)
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
                        .shadow(
                            elevation = 4.dp,
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                ) { selectedStart, selectedEnd, totalDays ->
                    startDate = selectedStart //VARIABEL TANGGAL MULAI
                    endDate = selectedEnd //VARIABEL TANGGAL AKHIR
                    daysCount = totalDays //VARIABEL TOTAL HARI
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.padding(start = 22.dp, end = 22.dp)
            ) {
                ButtonPrevCreatePlan(
                    onClick = { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                ButtonNextCreatePlan(
                    onClick = {
                        val perjalanan = Perjalanan(namaPerjalanan = planName, tanggalBerangkat = startDate.toString(), tanggalSelesai = endDate.toString())
                        saveToFirestoreJadwal(perjalanan)
                        navController.navigate("create_plan_2")

                       // saveUserDataFireStore(planName, startDate, endDate)
                    },
                    enabled = planName.isNotBlank() && startDate != null && endDate != null
                )
            }
        }
    }

    fun getDatesBetween(start: LocalDate, end: LocalDate): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var currentDate = start
        while (!currentDate.isAfter(end)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        return dates
    }

}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false, device = "spec:width=412dp,height=917dp")
@Composable
private fun Preview() {
    val navController = rememberNavController()
    CreatePlanScreen1(navController = navController)
}




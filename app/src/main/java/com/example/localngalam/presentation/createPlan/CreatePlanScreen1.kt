package com.example.localngalam.presentation.createPlan

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.BackgroundImage
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.ButtonPrevCreatePlan
import com.example.localngalam.presentation.ui_component.Navbar2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import planViewModel
import java.time.LocalDate

@Composable
fun CreatePlanScreen1(navController: NavController, viewModel: planViewModel = viewModel()) {
    var startDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var endDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }
    var planName by rememberSaveable { mutableStateOf("") }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            BackgroundImage()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))
            )

            LazyColumn( // 🔥 LazyColumn agar halaman bisa di-scroll
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                item {
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
                        onValueChange = { planName = it },
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
                                .background(Blue3)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .wrapContentHeight(Alignment.CenterVertically)
                                .shadow(
                                    elevation = 4.dp,
                                    spotColor = Color.Black,
                                    ambientColor = Color.Black
                                )
                        ) { selectedStart, selectedEnd, _ ->
                            startDate = selectedStart
                            endDate = selectedEnd
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
                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                if (uid != null) {
                                    val perjalanan = Perjalanan(
                                        namaPerjalanan = planName,
                                        tanggalBerangkat = startDate.toString(),
                                        tanggalSelesai = endDate.toString()
                                    )
                                    viewModel.setNamaPerjalanan(planName)
                                    viewModel.setTanggalBerangkat(startDate.toString())
                                    viewModel.saveToFirestoreJadwal(perjalanan) {
                                        navController.navigate("create_plan_2")
                                    }
                                }
                            },
                            enabled = planName.isNotBlank() && startDate != null && endDate != null
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Jaga agar tombol tidak terlalu bawah
                }
            }
        }
    }
}


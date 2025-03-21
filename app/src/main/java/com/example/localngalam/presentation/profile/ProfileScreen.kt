package com.example.localngalam.presentation.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.localngalam.presentation.ui_component.Navbar
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.SettingList
import com.example.localngalam.presentation.ui_component.settingIcon

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {

    val userData = viewModel.userData.collectAsState().value

    Scaffold(
        bottomBar = {
            Navbar(
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
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "LocalNgalam",
                            fontSize = 16.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.padding(10.dp)
                        )
                        Spacer(modifier = Modifier.width(220.dp))
                        settingIcon(onItemClick = { navController.navigate("register") })
                    }

                    Row {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                        )

                        Column(modifier = Modifier.padding(15.dp)) {
                            Spacer(modifier = Modifier.height(5.dp))

                            if (userData != null) { // Pengecekan null yang lebih tegas
                                println("Data user di UI: ${userData.namaLengkap}")
                                Text(
                                    text = userData.namaLengkap, // Gunakan userData.namaLengkap
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFont,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Level user", // Gunakan userData.level, atau "Level user" jika null
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFont,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Pengaturan",
                        fontSize = 16.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Blue2,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SettingList(onItemClick = { navController.navigate(it) })
                }
            }
        }
    }
}
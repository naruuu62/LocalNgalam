package com.example.localngalam.presentation.createPlan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.R
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.BackgroundImage
import com.example.localngalam.presentation.ui_component.ButtonNextCreatePlan
import com.example.localngalam.presentation.ui_component.Navbar2
import com.google.firebase.auth.FirebaseAuth
import planViewModel
import java.time.LocalDate

@Composable
fun CreatePlanScreen2(navController: NavController, viewModel: planViewModel = viewModel()) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var daysCount by remember { mutableStateOf<Long?>(null) }
    var planName by remember { mutableStateOf("") }
    var pilih by remember { mutableStateOf<String?>(null) }
    var tipePerjalanan by remember { mutableStateOf("") }

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
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {

            BackgroundImage()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.5f))

            )
            Spacer(modifier = Modifier.height(80.dp))


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

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

                Row(
                    modifier = Modifier.width(295.dp)
                ) {

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
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                ) {
                    Box(

                        modifier = Modifier
                            .width(149.dp)
                            .height(66.dp)
                            .shadow(36.dp, shape = RoundedCornerShape(33.dp))
                            .clip(RoundedCornerShape(33.dp))
                            .background(if (pilih == "Solo") Blue3 else Color.White)
                            .clickable {
                                tipePerjalanan = "Solo"
                                pilih = "Solo"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.ic_solo_trip),
                                contentDescription = "solo",
                                Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("perjalanan Solo", fontFamily = poppinsFont, fontSize = 12.sp)
                        }
                    }





                    Spacer(modifier = Modifier.width(45.dp))

                    Box(
                        modifier = Modifier
                            .width(149.dp)
                            .height(66.dp)
                            .shadow(36.dp, shape = RoundedCornerShape(33.dp))
                            .clip(RoundedCornerShape(33.dp))
                            .background(if (pilih == "Sahabat") Blue3 else Color.White)
                            .clickable {
                                tipePerjalanan = "Sahabat"
                                pilih = "Sahabat"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.ic_friendship_trip),
                                contentDescription = "Sahabat",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Perjalanan Sahabat", fontFamily = poppinsFont, fontSize = 12.sp)
                        }
                    }


                }
                Spacer(modifier = Modifier.height(50.dp))
                Row(

                ) {

                    Box(
                        modifier = Modifier
                            .width(149.dp)
                            .height(66.dp)
                            .shadow(36.dp, shape = RoundedCornerShape(33.dp))
                            .clip(RoundedCornerShape(33.dp))
                            .background(if (pilih == "Romantis") Blue3 else Color.White)
                            .clickable {
                                tipePerjalanan = "Romantis"
                                pilih = "Romantis"

                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.ic_romance_trip),
                                contentDescription = "Romantis",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "perjalanan \n Romantis",
                                fontFamily = poppinsFont,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(45.dp))

                    var isKeluargaClick by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .width(149.dp)
                            .height(66.dp)
                            .shadow(36.dp, shape = RoundedCornerShape(33.dp))
                            .clip(RoundedCornerShape(33.dp))
                            .background(if (pilih == "Keluarga") Blue3 else Color.White)
                            .clickable {
                                tipePerjalanan = "Keluarga"
                                pilih = "Keluarga"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.ic_family_trip),
                                contentDescription = "Keluarga",
                                modifier = Modifier.size(27.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("perjalanan\nKeluarga", fontFamily = poppinsFont, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(Modifier.height(60.dp))
                ButtonNextCreatePlan(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        if (uid != null) {
                            if (tipePerjalanan.isNotEmpty()) {
                                viewModel.updatePlan(tipePerjalanan)
                            }
                            navController.navigate("create_plan_3")
                        }
                    },
                    enabled = tipePerjalanan.isNotEmpty()
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

package com.example.localngalam.presentation.profile

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.localngalam.presentation.ui_component.Navbar
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.SettingList
import com.example.localngalam.presentation.ui_component.settingIcon

@Composable
fun ProfileScreen(navController: NavController) {
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
                            text = "Namamu", //nama user
                            fontSize = 16.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(10.dp)
                        )
                        Spacer(modifier = Modifier.width(220.dp))

                        settingIcon(onItemClick = {navController.navigate("register")})
                    }

                    Row() {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                        )

                        Column(modifier = Modifier.padding(15.dp)) {
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "Nama user",
                                fontSize = 16.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.Bold,


                            )
                            Text(
                                text = "Level user",
                                fontSize = 16.sp,
                                fontFamily = poppinsFont,
                                modifier = Modifier

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Pengaturan", //nama user
                        fontSize = 16.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Blue2,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .padding(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    SettingList(onItemClick = {navController.navigate(it)})
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val navController = androidx.navigation.compose.rememberNavController()
    ProfileScreen(navController = navController)
}


package com.example.localngalam.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localngalam.R
import com.example.localngalam.presentation.ui_component.Navbar
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.Blue4
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.SettingList
import com.example.localngalam.presentation.ui_component.settingIcon


@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {

    val userData by viewModel.userData.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar", fontFamily = poppinsFont, fontWeight = FontWeight.Bold) },
            text = { Text("Apakah kamu yakin ingin keluar dari akun ini?", fontFamily = poppinsFont) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Keluar", color = Color.Red, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Navbar(
                onHomeClick    = { navController.navigate("home") },
                onSearchClick  = { navController.navigate("search") },
                onPlusClick    = { navController.navigate("add_plan") },
                onHistoryClick = { navController.navigate("history") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F8FA)),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                // Header card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Foto profil
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, Blue3, CircleShape)
                        ) {
                            if (!userData?.fotoProfil.isNullOrBlank()) {
                                AsyncImage(
                                    model = userData!!.fotoProfil,
                                    contentDescription = "Foto Profil",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Blue4),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = Blue3,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = userData?.namaLengkap?.ifBlank { "Pengguna" } ?: "Pengguna",
                            fontSize = 20.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            color = Blue3
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = userData?.email ?: "",
                            fontSize = 13.sp,
                            fontFamily = poppinsFont,
                            color = Color.Gray
                        )

                        if (!userData?.noTelepon.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userData?.noTelepon ?: "",
                                fontSize = 13.sp,
                                fontFamily = poppinsFont,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Menu fitur
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Menu items list
                        val menuItems = listOf(
                            Triple("Bucket List", Icons.Default.List) { navController.navigate("bucket_list") },
                            Triple("Riwayat Trip Plan", Icons.Default.DateRange) { navController.navigate("history") },
                            Triple("Ulasan Tempat", Icons.Default.Star) { /* TODO */ },
                            Triple("Tentang", Icons.Default.Info) { /* TODO */ },
                            Triple("Bahasa", Icons.Default.Face) { /* TODO */ }
                        )

                        menuItems.forEachIndexed { index, item ->
                            TextButton(
                                onClick = item.third,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = item.second,
                                        contentDescription = item.first,
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        item.first,
                                        fontFamily = poppinsFont,
                                        fontSize = 14.sp,
                                        color = Color.DarkGray,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_next_createplan), // Fallback if no icon
                                        contentDescription = "Go",
                                        tint = Blue3,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            if (index < menuItems.size) {
                                HorizontalDivider(color = Color(0xFFEEEEEE))
                            }
                        }

                        // Logout
                        TextButton(
                            onClick = { showLogoutDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = Color.Black
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    "Keluar",
                                    fontFamily = poppinsFont,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(id = com.example.localngalam.R.drawable.ic_next_createplan),
                                    contentDescription = "Go",
                                    tint = Blue3,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
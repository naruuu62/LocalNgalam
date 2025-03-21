package com.example.localngalam.presentation.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.Navbar
import planViewModel

@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel = viewModel(), planViewModel: planViewModel = viewModel()) {
    val journeyData by viewModel.journeyData.collectAsStateWithLifecycle()
    val tanggalBerangkat by planViewModel.tanggalBerangkat.collectAsStateWithLifecycle() // Ambil tanggal dari planViewModel


    LaunchedEffect(Unit) {
        viewModel.fetchJourneyData()
    }

    Scaffold(
        bottomBar = {
            Navbar(
                onHomeClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onPlusClick = { navController.navigate("add_plan") },
                onHistoryClick = { navController.navigate("history") },
                onProfileClick = { navController.navigate("profile") }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            journeyData?.let { data ->
                val tanggalBerangkat = data["tanggalBerangkat"] as? String ?: ""
                val daftarPerjalanan = data["daftarPerjalanan"] as? List<Map<String, Any>> ?: emptyList()
                val namaPerjalanan = data["namaPerjalanan"] as? String ?: ""

                Text(
                    text = "Rencana Perjalanan ($tanggalBerangkat)", // Menambahkan tanggal ke header
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )

                if (daftarPerjalanan.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f), // Agar konten berada di tengah
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada perjalanan yang tersimpan",
                            textAlign = TextAlign.Center
                        )
                    }


                } else {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row {
                        Spacer(modifier = Modifier.width(18.dp))
                        Text(
                            text = "$namaPerjalanan",
                            fontSize = 20.sp,
                            fontFamily = poppinsFont,
                            color = Blue3,
                            fontWeight = Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Agar LazyColumn mengisi ruang yang tersedia
                    ) {
                        items(daftarPerjalanan) { trip ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = rememberImagePainter(data = trip["gambar"]),
                                        contentDescription = "Gambar Tempat",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = trip["namaTempat"] as? String
                                                ?: "Nama Tidak Diketahui",
                                            fontWeight = FontWeight.Bold,
                                            color = Blue3,
                                            fontFamily = poppinsFont
                                        )
                                        Text(
                                            text = trip["address"] as? String
                                                ?: "Alamat Tidak Diketahui",
                                            fontFamily = poppinsFont,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row {
                                            Icon(
                                                painter = painterResource(id = R.drawable.jam),
                                                contentDescription = "Jam Mulai",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(text = " ${trip["jamMulai"] ?: "-"} - ${trip["jamSelesai"] ?: "-"}", fontFamily = poppinsFont)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row {
                                            Icon(
                                                painter = painterResource(id = R.drawable.telepon),
                                                contentDescription = "Nomor Telepon",
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(text = " ${trip["nomorTelepon"] ?: "-"}")
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = trip["deskripsi"] as? String
                                                ?: "Deskripsi Tidak Diketahui", fontFamily = poppinsFont
                                        )
                                    }
                                    IconButton(onClick = { /* Handle delete */ }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
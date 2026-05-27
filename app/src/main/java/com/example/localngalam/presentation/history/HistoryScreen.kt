package com.example.localngalam.presentation.history

import Tempat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import java.time.LocalDate
import com.example.localngalam.R
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.Navbar

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val journeyList by viewModel.journeyList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchJourneyData()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 16.dp), // Some padding from status bar
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Trip Plan",
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Blue3
                )
            }
        },
        bottomBar = {
            Navbar(
                onHomeClick    = { navController.navigate("home") },
                onSearchClick  = { navController.navigate("search") },
                onPlusClick    = { navController.navigate("add_plan") },
                onHistoryClick = { navController.navigate("history") },
                onProfileClick = { navController.navigate("profile") }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (journeyList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Belum ada perjalanan yang tersimpan",
                            textAlign = TextAlign.Center,
                            fontFamily = poppinsFont,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { navController.navigate("add_plan") }) {
                            Text("Buat Perjalanan")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(journeyList) { journey ->
                        JourneyCard(journey = journey, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun JourneyCard(
    journey: Perjalanan,
    navController: NavController
) {
    val statusUI = getJourneyStatusUI(journey.tanggalBerangkat, journey.tanggalSelesai)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("journey", journey)
                navController.navigate("detail_itinerary")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = journey.namaPerjalanan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFont,
                        color = Color.Black
                    )
                }
                
                // Badge Status
                Box(
                    modifier = Modifier
                        .background(statusUI.bgColor, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusUI.text,
                        fontSize = 12.sp,
                        color = statusUI.textColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            // Body
            if (journey.daftarPerjalanan.isNotEmpty()) {
                journey.daftarPerjalanan.forEachIndexed { index, place ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberImagePainter(data = place.gambar),
                            contentDescription = "Gambar ${place.namaTempat}",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = place.namaTempat,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFont,
                                color = Blue3,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (place.jamMulai.isNotBlank() && place.jamSelesai.isNotBlank()) {
                                Text(
                                    text = "🕐 ${place.jamMulai} - ${place.jamSelesai}",
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFont,
                                    color = Color.DarkGray
                                )
                            }
                            if (place.address.isNotBlank()) {
                                Text(
                                    text = place.address,
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFont,
                                    color = Color.Gray,
                                    maxLines = 1
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Kunjungi Lagi Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        val mockTempat = Tempat(
                                            id = place.tempatId,
                                            namaLokasi = place.namaTempat,
                                            gambar = place.gambar,
                                            address = place.address,
                                            deskripsi = place.deskripsi,
                                            phoneNumber = place.nomorTelepon,
                                            open = place.jamMulai,
                                            close = place.jamSelesai,
                                            category = "",
                                            priceRange = 0L,
                                            tags = emptyList(),
                                        )
                                        navController.currentBackStackEntry?.savedStateHandle?.set("tempat", mockTempat)
                                        navController.navigate("detail_tempat")
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Blue3),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Kunjungi Lagi", fontSize = 12.sp, color = Color.White)
                                }
                            }
                        }
                    }
                    if (index < journey.daftarPerjalanan.size - 1) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Text(
                    text = "Belum ada destinasi ditambahkan",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

data class StatusUI(val text: String, val bgColor: Color, val textColor: Color)

fun getJourneyStatusUI(startDateStr: String, endDateStr: String): StatusUI {
    return try {
        // Strip time if it exists (e.g., "2026-05-26T10:00:00" -> "2026-05-26")
        val startDate = LocalDate.parse(startDateStr.substringBefore("T").substringBefore(" "))
        val endDate = LocalDate.parse(endDateStr.substringBefore("T").substringBefore(" "))
        val today = LocalDate.now()

        when {
            today.isBefore(startDate) -> StatusUI(
                "Menunggu",
                Color(0xFFFFF9C4), // Light Yellow
                Color(0xFFF57F17) // Dark Yellow
            )
            today.isAfter(endDate) -> StatusUI(
                "Selesai",
                Color(0xFFC5E1A5), // Light Green
                Color(0xFF33691E) // Dark Green
            )
            else -> StatusUI(
                "Sedang Dijalankan",
                Color(0xFFB3E5FC), // Light Blue
                Color(0xFF01579B) // Dark Blue
            )
        }
    } catch (e: Exception) {
        StatusUI("Menunggu", Color(0xFFFFF9C4), Color(0xFFF57F17))
    }
}
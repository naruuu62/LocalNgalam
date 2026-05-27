package com.example.localngalam.presentation.history

import Tempat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.localngalam.model.Perjalanan
import com.example.localngalam.model.tempatPerjalanan
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.Blue4
import com.example.localngalam.presentation.ui.theme.Green
import com.example.localngalam.presentation.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItineraryScreen(
    navController: NavController,
    journey: Perjalanan
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Detail Rencana Trip",
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Bold,
                        color = Blue3,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Blue3
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.White),
                modifier = Modifier.shadowUnderTopAppBar()
            )
        },
        containerColor = Color(0xFFF7F8FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = journey.namaPerjalanan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Blue3
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Tanggal",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${journey.tanggalBerangkat} s/d ${journey.tanggalSelesai}",
                                fontSize = 13.sp,
                                fontFamily = poppinsFont,
                                color = Color.DarkGray
                            )
                        }

                        if (journey.tipePerjalanan.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Tipe",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .background(Blue4, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = journey.tipePerjalanan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = poppinsFont,
                                        color = Blue3
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Timeline Header
            item {
                Text(
                    text = "Rencana Perjalanan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFont,
                    color = Blue3,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Timeline Destinations
            if (journey.daftarPerjalanan.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada destinasi di rencana ini.",
                            fontStyle = FontStyle.Italic,
                            fontFamily = poppinsFont,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                itemsIndexed(journey.daftarPerjalanan) { index, item ->
                    TimelineItem(
                        index = index,
                        totalCount = journey.daftarPerjalanan.size,
                        item = item,
                        onViewDetail = {
                            val mockTempat = Tempat(
                                id = item.tempatId,
                                namaLokasi = item.namaTempat,
                                gambar = item.gambar,
                                address = item.address,
                                deskripsi = item.deskripsi,
                                phoneNumber = item.nomorTelepon,
                                open = item.jamMulai,
                                close = item.jamSelesai,
                                category = "",
                                priceRange = 0L,
                                tags = emptyList()
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set("tempat", mockTempat)
                            navController.navigate("detail_tempat")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineItem(
    index: Int,
    totalCount: Int,
    item: tempatPerjalanan,
    onViewDetail: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Left timeline track (Node & Line)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            // Node
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Blue3),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (index + 1).toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFont
                )
            }
            // Vertical Line
            if (index < totalCount - 1) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(Blue3.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Content Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.gambar.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = item.gambar),
                            contentDescription = item.namaTempat,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.namaTempat,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            fontSize = 14.sp,
                            color = Blue3,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (item.jamMulai.isNotBlank() && item.jamSelesai.isNotBlank()) {
                            Text(
                                text = "🕐 ${item.jamMulai} - ${item.jamSelesai}",
                                fontSize = 12.sp,
                                fontFamily = poppinsFont,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                if (item.address.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Lokasi",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.address,
                            fontSize = 12.sp,
                            fontFamily = poppinsFont,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onViewDetail,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = "Lihat Detail Destinasi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Green
                        )
                    }
                }
            }
        }
    }
}

// Extension modifier to draw custom thin divider under TopAppBar
private fun Modifier.shadowUnderTopAppBar(): Modifier = this.drawUnderTopAppBar()

private fun Modifier.drawUnderTopAppBar(): Modifier = this.border(
    width = 0.5.dp,
    color = Color(0xFFEEEEEE)
)

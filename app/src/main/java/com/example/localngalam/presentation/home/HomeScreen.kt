package com.example.localngalam.presentation.home

import Tempat // Import yang benar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.localngalam.R
import com.example.localngalam.data.local.SessionManager
import com.example.localngalam.data.repository.ReviewRepository
import com.example.localngalam.presentation.ui_component.Navbar
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.poppinsFont
import planViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: planViewModel = viewModel(),
    sharedViewModel: sharedViewModel = viewModel(),
    onClick : (Tempat) -> Unit
) {
    val tempatList by viewModel.tempatList.collectAsState()
    
    // Asumsi: Kita ganti tipe viewModel ke homeViewModel untuk mengakses userData
    val homeViewModel: com.example.localngalam.presentation.home.homeViewModel = viewModel()
    val userData by homeViewModel.userData.collectAsState()
    val namaPengguna = userData?.namaLengkap?.split(" ")?.firstOrNull() ?: "Pengguna"

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
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pics),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.5f),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Mau pergi ke mana, \n$namaPengguna?",
                            fontSize = 20.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            color = Blue3
                        )
                    }
                }
            }

            // List kategori & tempat — sekarang pakai Grid
            val tempatPerKategori = tempatList.groupBy { it.category }

            tempatPerKategori.forEach { (kategori, tempatKategori) ->
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = kategori,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.Black
                        )

                        val displayedTempat = remember(tempatKategori) {
                            tempatKategori.shuffled().take(6)
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(628.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(displayedTempat) { tempat ->
                                TempatItem(tempat, onClick = {
                                    onClick(tempat)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TempatItem(tempat: Tempat, onClick: (() -> Unit)?= null) {
    val context = LocalContext.current
    val sessionManager = remember(context) { SessionManager(context) }
    val reviewRepository = remember(sessionManager) { ReviewRepository(sessionManager) }
    var rating by remember(tempat.id) { mutableFloatStateOf(0f) }
    var ratingLoaded by remember(tempat.id) { mutableStateOf(false) }

    LaunchedEffect(tempat.id) {
        val reviews = reviewRepository.getReviews(tempat.id)
        rating = if (reviews.isEmpty()) 0f else reviews.map { it.rating }.average().toFloat()
        ratingLoaded = true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick?.invoke() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = tempat.gambar),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tempat.namaLokasi,
                fontSize = 14.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tempat.category ?: "Category Tidak Tersedia",
                    fontSize = 12.sp,
                    fontFamily = poppinsFont,
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = if (ratingLoaded && rating > 0f) "%.1f".format(rating) else "-",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

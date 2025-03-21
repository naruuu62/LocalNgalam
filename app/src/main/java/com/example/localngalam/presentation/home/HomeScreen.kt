package com.example.localngalam.presentation.home

import Tempat // Import yang benar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.localngalam.R
import com.example.localngalam.presentation.ui_component.Navbar
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.poppinsFont
import homeViewModel
import planViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: planViewModel = viewModel(),
    sharedViewModel: sharedViewModel = viewModel(),
    onClick : (Tempat) -> Unit
) {
    val tempatList by viewModel.tempatList.collectAsState()

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
                            text = "Mau pergi ke mana, \nHessi?",
                            fontSize = 20.sp,
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            color = Blue3
                        )
                    }
                }
            }

            // List kategori & tempat
            val tempatPerKategori = tempatList.groupBy { it.category }

            tempatPerKategori.forEach { (kategori, tempatList) ->
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = kategori,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = Color.Black
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(tempatList) { tempat ->
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
    Card(
        modifier = Modifier
            .width(220.dp)
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
                    .height(146.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tempat.id,
                fontSize = 16.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = tempat.category ?: "Category Tidak Tersedia",
                fontSize = 14.sp,
                fontFamily = poppinsFont,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

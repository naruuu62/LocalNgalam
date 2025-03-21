    package com.example.localngalam.presentation.home

    import Tempat
    import androidx.compose.foundation.BorderStroke
    import androidx.compose.foundation.Image
    import androidx.compose.runtime.Composable
    import com.example.localngalam.presentation.ui_component.Navbar

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.derivedStateOf
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.layout.ContentScale
    import androidx.compose.ui.text.font.FontStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import coil.compose.rememberAsyncImagePainter
    import com.example.localngalam.presentation.ui_component.Navbar
    import com.example.localngalam.presentation.ui.theme.Blue
    import com.example.localngalam.presentation.ui.theme.poppinsFont
    import homeViewModel
    import java.sql.RowId

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.lazy.items

    import androidx.compose.material.icons.automirrored.filled.ArrowBack
    import androidx.compose.material.icons.filled.Star
    import androidx.compose.material3.*
    import androidx.compose.runtime.*

    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight.Companion.Bold

    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import coil.compose.rememberAsyncImagePainter
    import com.example.localngalam.R
    import com.example.localngalam.presentation.ui.theme.Blue3
    import com.example.localngalam.presentation.ui.theme.Blue4
    import com.example.localngalam.presentation.ui.theme.Green
    import com.example.localngalam.presentation.ui_component.Navbar
    import com.example.localngalam.presentation.ui.theme.poppinsFont

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailTempatScreen(
        navController: NavController,
        sharedViewModel: sharedViewModel = viewModel(),
        tempat: Tempat
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = tempat.id ?: "Detail Tempat",
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Blue3
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(Color.White),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
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
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                item {
                    Text(
                        text = tempat?.address ?: "Alamat tidak tersedia",
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = tempat?.gambar
                        ),
                        contentDescription = "Tempat Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "130 reviews", fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "4.6",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (!tempat?.tags.isNullOrEmpty()) {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(tempat?.tags ?: emptyList()) { tag ->
                                    Text(
                                        text = tag,
                                        modifier = Modifier
                                            .background(Blue4, RoundedCornerShape(12.dp))
                                            .padding(8.dp),
                                        fontSize = 12.sp,
                                        fontFamily = poppinsFont
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Tentang",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Blue3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tempat?.deskripsi ?: "Deskripsi tidak tersedia",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Jam Operasional",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont,
                            color = Blue3
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Blue4.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            val jamOperasional = mapOf(
                                "Minggu" to "10.00 - 22.00",
                                "Senin" to "11.00 - 21.00",
                                "Selasa" to "10.00 - 22.00",
                                "Rabu" to "10.00 - 22.00",
                                "Kamis" to "10.00 - 22.00",
                                "Jumat" to "11.00 - 21.00",
                                "Sabtu" to "11.00 - 21.00"
                            )
                            jamOperasional.forEach { (hari, jam) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = hari, fontSize = 14.sp)
                                    Text(
                                        text = jam,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Ulasan",
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Blue3
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                Card(
                                    title = "Lezat dan Terjangkau!",
                                    username = "foodie_malang",
                                    rating = 5,
                                    content = "Makanannya enak, porsinya pas, dan harganya ramah di kantong. Cocok buat nongkrong santai!"
                                )
                            }
                            item {
                                Card(
                                    title = "Mie Pedasnya Mantap!",
                                    username = "spicylover",
                                    rating = 4,
                                    content = "Mie pedas spesialnya benar-benar nendang, bikin ketagihan buat datang lagi!"
                                )
                            }
                            item {
                                Card(
                                    title = "Tempat Cozy",
                                    username = "traveladdict",
                                    rating = 5,
                                    content = "Tempatnya nyaman, suasana asik, cocok buat kerja atau sekadar ngopi santai."
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { /* Tambahkan ulasan */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green)
                        ) {
                            Text(
                                text = "Tambahkan Ulasan", color = Color.White, fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Card(title: String, username: String, rating: Int, content: String) {
        Card(
            modifier = Modifier
                .width(300.dp) // Sesuaikan lebar card
                .padding(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = username, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    for (i in 1..5) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = if (i <= rating) Color.Yellow else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = content, fontSize = 14.sp)
            }
        }
    }

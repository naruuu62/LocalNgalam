package com.example.localngalam.presentation.createPlan

import Tempat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.poppinsFont
import planViewModel


@Composable
fun CreatePlanScreen3(navController: NavController, viewModel: planViewModel = viewModel()) {
    val tempatList by viewModel.tempatList.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedPlaces by remember { mutableStateOf<List<Tempat>>(emptyList()) }
    var isAdding by remember { mutableStateOf(false) }

    fun hapusTempat(selectedPlace: Tempat, selectedPlaces: List<Tempat>, setSelectedPlaces: (List<Tempat>) -> Unit) {
        setSelectedPlaces(selectedPlaces.filter { it != selectedPlace })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Malang Trip",
            fontSize = 16.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )

        Text(
            text = "3 of 4",
            fontSize = 12.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Blue,
                modifier = Modifier.clickable {
                    isAdding = !isAdding
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                if (!isAdding) {
                    Text(
                        text = "Bangun rencana perjalanan anda dengan memilih tiap kategori yang sesuai.",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                } else {
                    selectedPlaces.forEach { selectedPlace ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = selectedPlace.gambar),
                                        contentDescription = "Tempat Image",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {

                                        Text(
                                            text = selectedPlace.id,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Blue3
                                        )
                                        Text(
                                            text = selectedPlace.address,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            text = selectedPlace.category,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_setting__1_),
                                            contentDescription = "Options",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "View Rute Bemo",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_warning),
                                        contentDescription = "Add to Chat",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable {
                                                hapusTempat(selectedPlace, selectedPlaces, { updatedPlaces -> selectedPlaces = updatedPlaces })
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Pilih Kategori",
            fontSize = 14.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            val categories = listOf("Semua", "Alam", "Cafe", "Restoran")
            items(categories) { category ->
                Button(
                    onClick = {
                        selectedCategory = category
                        viewModel.getTempatFilter(if (category == "Semua") "All" else category)
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategory == category) Color.Blue else Color.LightGray,
                        contentColor = if (selectedCategory == category) Color.White else Color.Black
                    )
                ) {
                    Text(text = category)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        tempatList.forEach { tempat ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(model = tempat.gambar),
                            contentDescription = "Tempat Image",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = tempat.id,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Blue3
                            )
                            Text(
                                text = tempat.address,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            Text(
                                text = tempat.deskripsi,
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Harga: Rp ${tempat.priceRange} /pax",
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Blue,
                            modifier = Modifier.clickable {
                                selectedPlaces = selectedPlaces + tempat
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow {
                        items(listOf("Specialty Coffee", "Desain Tropi")) { tag ->
                            Button(
                                onClick = { /* Handle tag click */ },
                                modifier = Modifier.padding(end = 4.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(text = tag, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
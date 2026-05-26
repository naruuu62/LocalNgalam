package com.example.localngalam.presentation.search

import Tempat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui_component.Navbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel(),
    onClick: (Tempat) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("Default") }
    var selectedCategory by remember { mutableStateOf("All") }

    // Load semua data saat pertama kali buka
    LaunchedEffect(Unit) {
        viewModel.getTempatFilter("All")
    }

    val rawList by viewModel.tempatList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val tempatList = remember(rawList, searchText, sortOption, selectedCategory) {
        val filtered = if (selectedCategory == "All") rawList
                       else rawList.filter { it.category == selectedCategory }
        val searched = if (searchText.isNotEmpty())
            filtered.filter { it.namaLokasi.lowercase().contains(searchText.lowercase()) }
        else filtered
        when (sortOption) {
            "Harga Rendah"    -> searched.sortedBy { it.priceRange }
            "Harga Tertinggi" -> searched.sortedByDescending { it.priceRange }
            else              -> searched
        }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search bar
            OutlinedTextField(
                singleLine = true,
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Pergi kemana hari ini?") },
                leadingIcon = {
                    Icon(painterResource(id = R.drawable.ic_navbar_search), contentDescription = "Search")
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter kategori
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(listOf("All", "Alam", "Cafe", "Restoran")) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Sort option
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(listOf("Default", "Harga Rendah", "Harga Tertinggi")) { option ->
                    FilterChip(
                        selected = sortOption == option,
                        onClick = { sortOption = option },
                        label = { Text(option, fontSize = 12.sp) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (tempatList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada tempat ditemukan", color = Color.Gray)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(tempatList) { tempat ->
                        Card(
                            colors = CardDefaults.cardColors(Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { onClick(tempat) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                AsyncImage(
                                    model = tempat.gambar,
                                    contentDescription = tempat.namaLokasi,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = tempat.namaLokasi.ifBlank { tempat.id },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Blue3
                                    )
                                    Text(
                                        text = tempat.address,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = viewModel.getHarga(tempat.priceRange),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = tempat.category,
                                        fontSize = 11.sp,
                                        color = Blue3
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

package com.example.localngalam.presentation.search

import Tempat
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.Blue3
import com.example.localngalam.presentation.ui.theme.Blue4
import com.example.localngalam.presentation.ui.theme.Blue5
import com.example.localngalam.presentation.ui_component.Navbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel = viewModel(), onClick : (Tempat) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("Default") }
    var selectedCategory by remember { mutableStateOf("All") }

    LaunchedEffect(selectedCategory) {
        viewModel.getTempatFilter(selectedCategory)
    }


    val tempatList = viewModel.tempatList.value?.let { list ->
        val filteredList = if (selectedCategory == "All") list else list.filter { it.category == selectedCategory }
        val searchedList = if (searchText.isNotEmpty()) {
            filteredList.filter { it.id.lowercase().contains(searchText.lowercase()) }
        } else {
            filteredList
        }
        when (sortOption) {
            "Harga Rendah" -> filteredList.sortedBy { it.priceRange }
            "Harga Tertinggi" -> filteredList.sortedByDescending { it.priceRange }
            else -> searchedList
        }
    } ?: emptyList()


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
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            OutlinedTextField(
                singleLine = true,
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Pergi kemana hari ini?") },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_navbar_search),
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(painterResource(id = R.drawable.search), contentDescription = "Location")
                Text(
                    text = "Sawojajar, Malang",
                    fontSize = 14.sp,
                    color = Blue3,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                items(listOf("All", "Alam", "Cafe", "Restoran", "Hotel")) { category ->
                    Button(
                        onClick = {
                            selectedCategory = category
                            viewModel.getTempatFilter(category)
                        },
                        colors = ButtonDefaults.buttonColors(if (selectedCategory == category) Blue5 else Blue4)
                    ) {
                        Text(category)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }


            LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                items(listOf("Default", "Harga Rendah", "Harga Tertinggi")) { option ->
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.padding(start = 8.dp).height(42.dp).width(170.dp),
                        onClick = { sortOption = option },
                        colors = ButtonDefaults.buttonColors(if (sortOption == option) Blue5 else Blue4)
                    ) {
                        Text(option)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }


            LazyColumn(modifier = Modifier.weight(1f)) {
                items(tempatList) { tempat ->
                    Card(
                        colors = CardDefaults.cardColors(Color.White),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            .clickable {
                                //detail
                                onClick(tempat)
                            },
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = tempat.gambar,
                                contentDescription = "Image",
                                modifier = Modifier.size(80.dp)
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = tempat.id,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Blue3
                                )
                                Text(text = tempat.address, fontSize = 14.sp, color = Color.Gray)
                                Text(
                                    text = "Harga: Rp ${viewModel.getHarga(tempat.priceRange)}/pax",
                                    fontSize = 14.sp
                                )
                                Text(text = tempat.deskripsi, fontSize = 14.sp, color = Color.Black)

                            }

                        }
                    }
                }
            }
        }
    }
}


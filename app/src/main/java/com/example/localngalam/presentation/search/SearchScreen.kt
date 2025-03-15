package com.example.localngalam.presentation.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.R
import com.example.localngalam.presentation.ui_component.Navbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = searchText,
                onQueryChange = { searchText = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = {"Pergi kemana hari ini?"},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_solo_trip),
                        contentDescription ="search icon"
                    )
                },
                trailingIcon = {
                        if (active) {
                            Icon(
                                modifier = Modifier.clickable {
                                    if (searchText.isNotEmpty()) {
                                        searchText = ""
                                    } else {
                                        active = false
                                    }
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "close icon"
                            )
                        }
                }
            )
             {
                // Tambahkan hasil pencarian di sini jika ada
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Tempatkan item hasil pencarian di sini jika ada
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSearchScreen() {
    val navController = rememberNavController()
    SearchScreen(navController = navController)
}

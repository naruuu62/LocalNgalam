package com.example.localngalam.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.localngalam.presentation.ui.theme.poppinsFont

@Composable
fun HomeScreen (navController: NavController) {
    Row {
        Text(
            text = "KOSOMNGAN DULU",
            fontSize = 12.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
    }
}
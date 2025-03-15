package com.example.localngalam.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.autentikasi.autentikasiViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LogoutScreen(navController: NavController, authViewModel: autentikasiViewModel = viewModel()) {
    val loginState = authViewModel.loginState.collectAsState()

    LaunchedEffect(Unit) {
        if(loginState.value == false){
            navController.navigate("login") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Apakah Anda yakin ingin logout?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Batal")
            }

            Button(
                onClick = {
                    authViewModel.signOut()
                          },


                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogoutScreen() {
    val navController = rememberNavController()
    LogoutScreen(navController = navController)
}

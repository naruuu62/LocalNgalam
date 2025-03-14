package com.example.localngalam.BackEnd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.localngalam.autentikasiViewModel.autentikasi
import com.example.localngalam.presentation.resetPassword.ResetPasswordScreen2
import kotlinx.coroutines.launch

class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val deepLink: Uri? = intent?.data

        setContent {
            val authViewModel: autentikasi = viewModel()
            val scope = rememberCoroutineScope()

            LaunchedEffect(deepLink) {
                deepLink?.let {
                    scope.launch {

                    }
                }
            }

            ResetPasswordScreen2(
                navController = object : NavController(this@ResetPasswordActivity) {}, // Dummy NavController
                authViewModel = authViewModel
            )
        }
    }
}

package com.example.localngalam.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.localngalam.data.local.SessionManager
import kotlinx.coroutines.delay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.localngalam.presentation.ui.theme.poppinsFont

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionManager = SessionManager(this)

        setContent {
            val navController = rememberNavController()
            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreenHandler(onFinished = { showSplash = false })
            } else {
                MainScreen(navController = navController, sessionManager = sessionManager)
            }
        }
    }
}

@Composable
fun SplashScreenHandler(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 1000
        )
    )
    val scaleAnim by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (startAnimation) 1.05f else 0.85f,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF365EBD)), // Same blue color as native splash screen
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "DolanNgalam",
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = 42.sp,
            color = Color.White,
            modifier = Modifier
                .graphicsLayer(
                    alpha = alphaAnim,
                    scaleX = scaleAnim,
                    scaleY = scaleAnim
                )
        )
    }
}

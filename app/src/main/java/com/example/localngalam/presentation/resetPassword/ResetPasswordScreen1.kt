package com.example.localngalam.presentation.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.presentation.ui_component.GreenButtonRegisterLogin
import com.example.localngalam.presentation.ui_component.TextFieldRegisterLoginScreen
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.poppinsFont

@Composable
fun ResetPasswordScreen1(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        .fillMaxSize()
        .background(Color.White)
    )
    {
        Spacer(modifier = Modifier.height(331.dp))

        Text(
            text = "Reset Kata Sandi",
            fontSize = 20.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Bold,
            color = Blue,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Silahkan masukkan Email anda untuk\nmeminta kata sandi",
            fontSize = 13.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Blue,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextFieldRegisterLoginScreen(
            value = email,
            onValueChange = { email = it },
            placeholderText = "Masukkan Email",

            //TEMPAT MASUKIN EMAIL BUAT DIKIRIMIN KODE OTENTIKASI CUK
        )

        Spacer(modifier = Modifier.height(20.dp))

        GreenButtonRegisterLogin(
            text = "Kirim",
            isEnabled = email.isNotEmpty(),
            onClick = {
                //KE PAGE SELANJUTNYA
                // HARUS MASUKIN DULU EMAIL KE DALAM TEXT FIELD
            }
        )


    }
}


@Preview (device = "spec:width=412dp,height=917dp",showSystemUi = false)
@Composable
private fun Screen() {
    ResetPasswordScreen1()
}
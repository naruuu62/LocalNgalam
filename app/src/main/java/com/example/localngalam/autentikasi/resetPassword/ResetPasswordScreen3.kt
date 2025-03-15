package com.example.localngalam.presentation.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.Blue2
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.GreenButtonRegisterLogin
import com.example.localngalam.presentation.ui_component.OtpComponent

@Composable
fun ResetPasswordScreen3(modifier: Modifier = Modifier) {
    var isOtpCorrect by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        .fillMaxSize()
        .background(Color.White)
    )
    {
        Spacer(modifier = Modifier.height(258.dp))

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
            text = "Masukkan kode OTP yang telah dikirimkan\nmelalui Email",
            fontSize = 13.sp,
            fontFamily = poppinsFont,
            fontWeight = FontWeight.Normal,
            color = Blue,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(57.dp))

        OtpComponent(
            correctOtp = "9157",//DISINI MASUKIN VARIABEL KODE OTP

            onOtpEntered = { isCorrect ->
                isOtpCorrect = isCorrect
            }
        )

        Spacer(modifier = Modifier.height(77.dp))

        GreenButtonRegisterLogin(
            text = "Masuk",
            isEnabled = isOtpCorrect,
            onClick = {
                //MENUJU PAGE BERIKUTNYA
            }
        )

        Spacer(modifier = Modifier.height(21.dp))

        Row {
            Text(
                text = "Kode tidak muncul?",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Klik disini",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Blue2,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        //KIRIM ULANG KODE OTP
                    }
            )
        }
    }
}


@Preview (device = "spec:width=412dp,height=917dp",showSystemUi = false)
@Composable
private fun Screen() {
    ResetPasswordScreen3()
}
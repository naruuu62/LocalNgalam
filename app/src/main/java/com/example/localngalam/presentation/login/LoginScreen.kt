package com.example.localngalam.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.localngalam.R
import com.example.localngalam.autentikasi
import com.example.localngalam.presentation.GoogleSignUpButton
import com.example.localngalam.presentation.GreenButtonRegisterLogin
import com.example.localngalam.presentation.OrDivider
import com.example.localngalam.presentation.TextFieldRegisterLoginScreen
import com.example.localngalam.presentation.TextFieldRegisterLoginScreenWithEye
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.Warning
import com.example.localngalam.presentation.ui.theme.poppinsFont

@Composable
fun LoginScreen(navController: NavController,modifier: Modifier = Modifier,  authViewModel: autentikasi = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordWrong by remember { mutableStateOf(false) }
    val loginState by authViewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus(force = true)
            })
        }) {
        Image(
            painter = painterResource(id = R.drawable.background_login_register),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
                .align(Alignment.TopCenter)
        )
        Image(
            painter = painterResource(id = R.drawable.shadow_login_register_screen),
            contentDescription = ""
        )
        Image(
            painter = painterResource(id = R.drawable.logo_localngalam),
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = 90.dp)
                .padding(horizontal = 113.dp)
        )
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.rectangle_login_register_screen),
                contentDescription = "",
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
            ) {
                Spacer(modifier = Modifier.height(69.dp))

                Text(
                    text = "Masuk\nSelamat Datang!",
                    fontSize = 20.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = Blue,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(46.dp))

                TextFieldRegisterLoginScreen(
                    value = email,
                    onValueChange = { email = it },
                    placeholderText = "Email",
                    leadingIcon = R.drawable.ic_email
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldRegisterLoginScreenWithEye(
                    value = password,
                    onValueChange = { password = it },
                    placeholderText = "Kata Sandi",
                    leadingIcon = R.drawable.ic_password
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Lupa Kata Sandi?",
                    fontSize = 10.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Normal,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navController.navigate("register")
                    }
                )

                Spacer(modifier = Modifier.height(42.dp))

                //WARNING JIKA PASSWORD SALAH
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(24.dp)
                        .alpha(if (isPasswordWrong) 1f else 0f)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_warning),
                        contentDescription = ""
                    )
                    Text(
                        text = "Pengguna atau Kata Sandi Tidak Valid",
                        color = Warning,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                GreenButtonRegisterLogin(
                    text = "Masuk",
                    onClick = {
                        /* login akun biasa */
                        if (password.isEmpty() || email.isEmpty()) {
                            isPasswordWrong = true
                        } else {
                            authViewModel.login(email, password)

                            if (loginState == true) {
                                navController.navigate("home")
                            } else {
                                isPasswordWrong = true
                            }
                        }
                    }
                )


                Spacer(modifier = Modifier.height(8.dp))

                OrDivider()

                Spacer(modifier = Modifier.height(8.dp))

                GoogleSignUpButton(
                    onClick = {
                        /* login gugel */
                    }
                )

                Spacer(modifier = Modifier.height(34.dp))

                Row {
                    Text(
                        text = "Belum memiliki akun?",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Daftar",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Blue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable {
                                // ke page register
                                navController.navigate("register")
                            }
                    )
                }


            }
        }
    }
}

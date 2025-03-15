package com.example.localngalam.presentation.register

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.localngalam.R
import com.example.localngalam.autentikasi.autentikasiViewModel
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.Warning
import com.example.localngalam.presentation.ui.theme.poppinsFont
import com.example.localngalam.presentation.ui_component.GoogleSignUpButton
import com.example.localngalam.presentation.ui_component.GreenButtonRegisterLogin
import com.example.localngalam.presentation.ui_component.OrDivider
import com.example.localngalam.presentation.ui_component.TextFieldRegisterLoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier, authViewModel:  autentikasiViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var namaLengkap by remember { mutableStateOf("") }
    var noTelepon by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by authViewModel.loginState.collectAsState()
    val context = LocalContext.current
    var isRegistWrong by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("497053976903-62igeq8ktk2iqoa06mp68vh94gihgg1v.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val GoogleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    authViewModel.signInWithGoogle(it.idToken!!)
                }
            } catch (e: ApiException) {
                println("Google sign in failed: ${e.statusCode}")
            }
        }
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

//=======
    Box(modifier = modifier.fillMaxSize()
        .pointerInput(Unit){
        detectTapGestures(onTap = {
        focusManager.clearFocus(force = true)
        })
//>>>>>>> main
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
                Spacer(modifier = Modifier.height(42.dp))

                Text(
                    text = "Daftar\nSelamat Datang!",
                    fontSize = 20.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Bold,
                    color = Blue,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(26.dp))

                TextFieldRegisterLoginScreen(
                    value = email,
                    onValueChange = { email = it },
                    placeholderText = "Email",
                    leadingIcon = R.drawable.ic_email
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldRegisterLoginScreen(
                    value = namaLengkap,
                    onValueChange = { namaLengkap = it },
                    placeholderText = "Nama Lengkap",
                    leadingIcon = R.drawable.ic_phone
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldRegisterLoginScreen(
                    value = noTelepon,
                    onValueChange = { noTelepon = it },
                    placeholderText = "Nomor Telepon",
                    leadingIcon = R.drawable.ic_person
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextFieldRegisterLoginScreen(
                    value = password,
                    onValueChange = { password = it },
                    placeholderText = "Kata Sandi",
                    leadingIcon = R.drawable.ic_password
                )
                Spacer(modifier = Modifier.height(51.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(24.dp)
                        .alpha(if (isRegistWrong) 1f else 0f)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_warning),
                        contentDescription = ""
                    )
                    Text(
                        text = "Password minimal 8 karakter",
                        color = Warning,
                        fontSize = 12.sp,
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))


                GreenButtonRegisterLogin(
                    isEnabled = email.isNotEmpty(),
                    text = "Sign Up",
                    onClick = {
                        if (password.isEmpty() || email.isEmpty() || password.length < 8) {
                            isRegistWrong = true
                        } else {
                            authViewModel.register(email, password, namaLengkap, noTelepon)
                            /* DAFTAR AKUN */
                        }
                    }
                )

                LaunchedEffect(loginState) {
                    if (loginState == false) {
                        isRegistWrong = true
                        navController.navigate("home")
                    } else if(loginState == true) {
                        navController.navigate("home")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OrDivider()

                Spacer(modifier = Modifier.height(8.dp))

                GoogleSignUpButton(
                    onClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                        /* login gugel */
                    }
                )

                LaunchedEffect(loginState) {
                    if (loginState == true) {
                        navController.navigate("home")
                    } else if (loginState == false) {
                        isRegistWrong = true
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Text(
                        text = "Sudah punya akun?",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Masuk",
                        fontSize = 12.sp,
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Normal,
                        color = Blue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("login")
                                // ke page Login

                            }
                    )
                }
            }
        }
    }
}
package com.example.localngalam.presentation.ui_component


import android.R.attr.onClick
import android.R.attr.text
import android.os.Trace.isEnabled
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.*
import com.google.firebase.annotations.concurrent.Background

@Composable
fun TextFieldRegisterLoginScreen(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: Int? = null,
    modifier: Modifier = Modifier
) { val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        placeholder = {
            Text(
                text = placeholderText,
                color = NotFilledText,
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier
                        .padding(vertical = 15.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(17.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        modifier = modifier
            .width(351.dp)
            .height(54.dp)
            .shadow(4.dp, shape = RoundedCornerShape(17.dp))
            .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp))
    )
}

@Composable
fun TextFieldRegisterLoginScreenWithEye(
    value: String = "text",
    onValueChange: (String) -> Unit = {},
    placeholderText: String = "text",
    leadingIcon: Int? = null,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {focusManager.moveFocus(FocusDirection.Down)}
        ),
        placeholder = {
            Text(
                text = placeholderText,
                color = NotFilledText,
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = "Leading Icon",
                    tint = Blue,
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp)
                )
            }
        },
        trailingIcon = {
            val iconRes = if (passwordVisible) R.drawable.ic_open_eye else R.drawable.ic_close_eye

            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    tint = Color.Unspecified
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        shape = RoundedCornerShape(17.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        modifier = modifier
            .width(351.dp)
            .height(54.dp)
            .shadow(4.dp, shape = RoundedCornerShape(17.dp))
            .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp))
    )
}

@Composable
fun GreenButtonRegisterLogin(
    text: String = "Button",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Green,
            disabledContainerColor = NotFilled
        ),
        modifier = modifier
            .width(351.dp)
            .height(54.dp)
            .shadow(4.dp, shape = RoundedCornerShape(17.dp)),
        enabled = isEnabled
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Blue3
        )
    }
}

@Composable
fun OrDivider() {
    Row(
        modifier = Modifier.width(136.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            color = DividerColor,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "OR",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        HorizontalDivider(
            color = DividerColor,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun GoogleSignUpButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .border(width = 1.dp, color = Blue, shape = RoundedCornerShape(17.dp))
            .width(351.dp)
            .height(54.dp)
            .shadow(4.dp, shape = RoundedCornerShape(17.dp))
            .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp))
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(48.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "google logo",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(35.dp))
            Text(
                text = "Sign Up with Google",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Blue
            )
        }
    }
}

@Composable
fun OtpComponent(
    correctOtp: String,
    onOtpEntered: (Boolean) -> Unit
) {
    var otpValues = remember { mutableStateListOf("", "", "", "") }
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(otpValues.size) { FocusRequester() } }

    Row(horizontalArrangement = Arrangement.spacedBy(29.dp)) {
        otpValues.forEachIndexed { index, value ->
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    val intValue = newValue.toIntOrNull()

                    when {
                        intValue != null && newValue.length == 1 && value.isEmpty() -> {
                            otpValues[index] = newValue
                            if (index < otpValues.lastIndex) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                        newValue.isEmpty() -> {
                            otpValues[index] = ""
                            if (index > 0) {
                                focusRequesters[index - 1].requestFocus()
                            }
                        }
                    }

                    val enteredOtp = otpValues.joinToString("")
                    if (enteredOtp.length == correctOtp.length) {
                        onOtpEntered(enteredOtp == correctOtp)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == otpValues.lastIndex) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { if (index < otpValues.lastIndex) focusRequesters[index + 1].requestFocus() },
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = poppinsFont,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    letterSpacing = 4.sp,
                    lineHeight = 70.sp

                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = NotFilled,
                    unfocusedContainerColor = NotFilled,
                    focusedIndicatorColor = NotFilled,
                    unfocusedIndicatorColor = Color.White
                ),
                modifier = Modifier
                    .width(59.dp)
                    .height(90.dp)
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && index > 0 && otpValues[index - 1].isEmpty()) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(20.dp, CircleShape)
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_button),
            contentDescription = "Back Icon",
            tint = Color.Unspecified,
            modifier = modifier
                .size(24.dp)
        )
    }
}

@Composable
fun TextFieldCreatePlan(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier
) { val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        placeholder = {
            Text(
                text = placeholderText,
                color = NotFilledText,
                fontSize = 15.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Light
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(17.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(
                width = 1.dp, color = Color.Black, shape = RoundedCornerShape(size = 16.dp)
            )
    )
}

@Composable
fun ButtonPrevCreatePlan(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(93.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Green2)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_back_createplan),
                contentDescription = "Back Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = Blue3,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ButtonNextCreatePlan(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .width(93.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (enabled) Green2 else Color.LightGray)
            .clickable(enabled = enabled) { onClick() }, // Disable klik jika tidak aktif
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(29.dp))

            Text(
                text = "Next",
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = if (enabled) Blue3 else Color.Gray // Warna berubah sesuai enabled
            )

            Spacer(modifier = Modifier.width(11.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_next_createplan),
                contentDescription = "Next Icon",
                tint = if (enabled) Color.Unspecified else Color.Gray, // Warna berubah sesuai enabled
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ButtonTypeTripPlan(
    text: String,
    iconResId: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .width(149.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable { if (enabled) onClick() }, // Hanya bisa diklik jika enabled
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = "Plan Icon",
                tint = if (enabled) Color.Unspecified else Color.Gray, // Warna berubah sesuai enabled
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp)) // Jarak antara ikon dan teks

            Text(
                text = text,
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal,
                color = if (enabled) Blue3 else Color.Gray // Warna berubah sesuai enabled
            )
        }
    }
}



@Preview
@Composable
fun Preview() {
    ButtonTypeTripPlan(
        text = "Solo Trip",
        iconResId = R.drawable.ic_close_eye,
        onClick = { /* Aksi ketika tombol diklik */ }
    )
}

@Composable
fun SettingList(onItemClick: (String) -> Unit){
    listOf(
        R.drawable.ic_notification to "Notifikasi",
        R.drawable.ic_bucket_list to "Bucket List",
        R.drawable.ic_history_plan to "History Trip Plan",
        R.drawable.ic_posting to "Posting",
        R.drawable.ic_about to "Tentang",
        R.drawable.ic_languange to "Bahasa",
        R.drawable.ic_logout to "logout" //ruteNavController sob
    ).forEach { (icon, route) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick(route) }
                .padding(vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = route,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = route, fontSize = 16.sp, fontWeight = FontWeight.Normal, modifier = Modifier.width(270.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_next_createplan),
                contentDescription = "Arrow Right",
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Composable
fun settingIcon(onItemClick: (String) -> Unit){
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_setting__1_),
            contentDescription = "Setting",
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun BackgroundImage(modifier: Modifier = Modifier){
       Image(painterResource(id = R.drawable.background_create_plan), contentDescription = "background", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
}




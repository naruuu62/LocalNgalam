package com.example.localngalam.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localngalam.R
import com.example.localngalam.presentation.ui.theme.Blue
import com.example.localngalam.presentation.ui.theme.DividerColor
import com.example.localngalam.presentation.ui.theme.Green
import com.example.localngalam.presentation.ui.theme.NotFilled
import com.example.localngalam.presentation.ui.theme.poppinsFont

@Composable
fun TextFieldRegisterLoginScreen(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: Int,
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
                color = NotFilled,
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = null,
                tint = Blue,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .padding(horizontal = 20.dp)
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
    leadingIcon: Int=R.drawable.ic_password,
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
                color = NotFilled,
                fontSize = 12.sp,
                fontFamily = poppinsFont,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                contentDescription = "Password Icon",
                tint = Blue,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp)
            )
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
fun GreenButttonRegisterLogin(
    text: String = "Button",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(17.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Green,
        ),
        modifier = modifier
            .width(351.dp)
            .height(54.dp)
            .shadow(4.dp, shape = RoundedCornerShape(17.dp))
            .background(Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
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
            fontWeight = FontWeight.Bold,
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
            containerColor = Blue
        ),
        modifier = modifier
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
                color = Color.White
            )


        }

    }
}



@Preview
@Composable
fun Preview() {
    TextFieldRegisterLoginScreenWithEye()
}

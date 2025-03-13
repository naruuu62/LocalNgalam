package com.example.localngalam.presentation.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.localngalam.R

@Composable
fun Navbar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onPlusClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.navigation_bar),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            BadgedBox(
                badge = {},
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onPlusClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_plus),
                    contentDescription = "Add Plan Icon",
                    tint = Color.Unspecified
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp, start = 35.dp, end = 35.dp)
        ) {
            // Home Icon
            BadgedBox(
                badge = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onHomeClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_home),
                    contentDescription = "Home Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(50.dp)
                )
            }

            // Search Icon
            BadgedBox(
                badge = {},
                modifier = Modifier
                    .padding(start = 36.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onSearchClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_search),
                    contentDescription = "Search Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // History Icon
            BadgedBox(
                badge = {},
                modifier = Modifier
                    .padding(end = 36.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onHistoryClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_history),
                    contentDescription = "History Icon",
                    tint = Color.Unspecified
                )
            }

            // Profile Icon
            BadgedBox(
                badge = {},
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onProfileClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_profile),
                    contentDescription = "Profile Icon",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun Preview() {
    Navbar()
}

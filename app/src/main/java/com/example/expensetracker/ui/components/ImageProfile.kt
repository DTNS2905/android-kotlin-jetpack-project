package com.example.expensetracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageProfile(
    imageRes: Int,
    modifier: Modifier,
    onClick: () -> Unit = {},
    size: Dp = 40.dp
) {
    Image(
        contentScale = ContentScale.Crop,
        contentDescription = "Profile",
        painter = painterResource(id = imageRes),
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable{onClick()}
    )
}
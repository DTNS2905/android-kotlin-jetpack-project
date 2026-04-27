package com.example.expensetracker.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ImageProfile(
    imageRes: Int,
    modifier: Modifier,
    onClick: () -> Unit = {},
    size: Dp = 40.dp,
    filePath: String? = null,
) {
    val bitmap by produceState<ImageBitmap?>(null, filePath) {
        value = if (filePath != null) {
            withContext(Dispatchers.IO) {
                try { BitmapFactory.decodeFile(filePath)?.asImageBitmap() }
                catch (e: Exception) { null }
            }
        } else null
    }

    if(bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .clickable { onClick() }
        )
    }
    else {
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
}
package com.example.expensetracker.ui.screens.home.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.AppButton
import com.example.expensetracker.ui.components.AppTextField
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.utils.ImageUtils

@Composable
fun EditProfileDialog(
    currentImagePath: String?,
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {

    val context = LocalContext.current
    var name by remember { mutableStateOf(currentName) }
    var imagePath by remember { mutableStateOf(currentImagePath) }

    val bitmap by produceState<ImageBitmap?>(null, imagePath) {
        value = ImageUtils.loadBitmap(imagePath)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagePath = ImageUtils.copyToInternalStorage(context, uri)
        }
    }

    CustomDialog(
        title = "Edit Profile",
        onDismiss = onDismiss,
        onConfirm = { onConfirm(name, imagePath) },
        icon = Icons.Default.Person,
        confirmText = "Save"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                if(bitmap != null) {
                    Image(
                        bitmap = bitmap!!,
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).clip(CircleShape)
                    )
                }

                else {
                    Image(
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile",
                        painter = painterResource(R.drawable.sang),
                        modifier = Modifier.size(80.dp).clip(CircleShape)
                    )
                }
            }
            AppButton(
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                icon = Icons.Default.PhotoCamera
            )

            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Display Name",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
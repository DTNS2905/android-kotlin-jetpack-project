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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditProfileDialog(
    currentImagePath: String?,
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf(currentName) }
    var imagePath by remember { mutableStateOf(currentImagePath) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isProcessingImage by remember { mutableStateOf(false) }

    // Initial load of the existing profile image.
    LaunchedEffect(Unit) {
        bitmap = ImageUtils.loadBitmap(currentImagePath)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                isProcessingImage = true
                // Copy off the main thread, then decode, so the UI stays responsive
                // and Save stays disabled until the new image is ready to display.
                val path = withContext(Dispatchers.IO) {
                    ImageUtils.copyToInternalStorage(context, uri)
                }
                if (path != null) {
                    imagePath = path
                    bitmap = ImageUtils.loadBitmap(path)
                }
                isProcessingImage = false
            }
        }
    }

    CustomDialog(
        title = "Edit Profile",
        onDismiss = onDismiss,
        onConfirm = { onConfirm(name, imagePath) },
        confirmEnabled = !isProcessingImage,
        icon = Icons.Default.Person,
        confirmText = "Save"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(80.dp)
            ) {
                when {
                    isProcessingImage -> {
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                    }
                    bitmap != null -> {
                        Image(
                            bitmap = bitmap!!,
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp).clip(CircleShape)
                        )
                    }
                    else -> {
                        Image(
                            contentScale = ContentScale.Crop,
                            contentDescription = "Profile",
                            painter = painterResource(R.drawable.sang),
                            modifier = Modifier.size(80.dp).clip(CircleShape)
                        )
                    }
                }
            }
            AppButton(
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                enabled = !isProcessingImage,
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
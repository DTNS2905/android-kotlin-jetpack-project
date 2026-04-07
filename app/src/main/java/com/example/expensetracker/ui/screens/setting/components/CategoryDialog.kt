package com.example.expensetracker.ui.screens.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.colorsForPicker
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun CategoryDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (String, Color) -> Unit = { _, _ -> }
) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(colorsForPicker.first()) }

    CustomDialog(
        title = "Add Category",
        icon = Icons.Rounded.Category,
        onDismiss = onDismiss,
        confirmText = "Confirm",
        confirmEnabled = name.isNotBlank(),
        onConfirm = {
            onConfirm(name, selectedColor)
            onDismiss()
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ColorPicker(
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                CategoryChip(
                    name = name.ifBlank { "Category" },
                    color = selectedColor
                )
            }
        }
    }
}

@Composable
fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colorsForPicker.forEach { color ->
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (color == selectedColor) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryDialogPreview() {
    ExpenseTrackerTheme {
        CategoryDialog()
    }
}

package com.example.expensetracker.ui.screens.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.expensetracker.ui.components.AppTextField
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
import com.example.expensetracker.utils.CategoryIcon
import com.example.expensetracker.utils.categoryIconList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (String, Color, String) -> Unit = { _, _, _ -> }
) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(colorsForPicker.first()) }
    var selectedIcon by remember { mutableStateOf(categoryIconList.first()) }

    CustomDialog(
        title = "Add Category",
        icon = Icons.Rounded.Category,
        onDismiss = onDismiss,
        confirmText = "Confirm",
        confirmEnabled = name.isNotBlank(),
        onConfirm = {
            onConfirm(name, selectedColor, selectedIcon.key)
            onDismiss()
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AppTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                modifier = Modifier.fillMaxWidth()
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Icon",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconPicker(
                    selectedIcon = selectedIcon,
                    selectedColor = selectedColor,
                    onIconSelected = { selectedIcon = it }
                )
            }

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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconPicker(
    selectedIcon: CategoryIcon,
    selectedColor: Color,
    onIconSelected: (CategoryIcon) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryIconList.forEach { iconItem ->
            val isSelected = iconItem.key == selectedIcon.key
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) selectedColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) selectedColor else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { onIconSelected(iconItem) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconItem.vector,
                    contentDescription = iconItem.label,
                    tint = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
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

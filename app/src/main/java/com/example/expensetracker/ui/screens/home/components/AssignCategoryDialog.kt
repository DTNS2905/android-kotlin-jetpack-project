package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.components.CustomDialog

@Composable
fun AssignCategoryDialog(
    categories: List<Category>,
    currentCategoryId: Int?,
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    var selectedId by remember { mutableStateOf(currentCategoryId) }

    CustomDialog(
        title = "Assign Category",
        icon = Icons.Rounded.Category,
        onDismiss = onDismiss,
        onConfirm = { onConfirm(selectedId) },
        confirmEnabled = selectedId != currentCategoryId
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    name = category.title,
                    color = Color(category.color.toInt()),
                    selected = selectedId == category.id,
                    onClick = {
                        selectedId = if (selectedId == category.id) null else category.id
                    }
                )
            }
        }
    }
}
package com.example.expensetracker.ui.screens.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingContent(
    categories: List<Category>,
    showCategoryDialog: Boolean,
    categoryToDelete: Category?,
    onShowCategoryDialog: () -> Unit,
    onDismissCategoryDialog: () -> Unit,
    onAddCategory: (String, Long) -> Unit,
    onRequestDeleteCategory: (Category) -> Unit,
    onConfirmDeleteCategory: (Category) -> Unit,
    onDismissDeleteDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)

        SettingSection(title = "Profile") {
            SettingItem(icon = Icons.Default.Person, title = "Sang", subtitle = "Edit profile")
        }

        SettingSection(title = "Categories") {
            if (categories.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { category ->
                            val color = Color(category.color.toInt())
                            CategoryChip(
                                name = category.title,
                                color = color,
                                onClick = { onRequestDeleteCategory(category) },
                                trailingIcon = {
                                    Icon(imageVector = Icons.Default.Close,
                                        contentDescription = "Delete ${category.title}",
                                        modifier = Modifier.size(16.dp), tint = color)
                                }
                            )
                        }
                    }
                }
            }
            SettingItem(icon = Icons.Default.Add, title = "Add Category",
                showArrow = false, onClick = onShowCategoryDialog)
        }

        SettingSection(title = "Budget") {
            SettingItem(icon = Icons.Default.Wallet, title = "Monthly Budget", subtitle = "$0.00")
            SettingItem(icon = Icons.Default.Notifications, title = "Budget Alert", subtitle = "80%")
        }

        SettingSection(title = "Data") {
            SettingItem(icon = Icons.Outlined.FileUpload, title = "Export to CSV")
            SettingItem(icon = Icons.Default.DeleteOutline, title = "Clear all expenses",
                tint = MaterialTheme.colorScheme.error, showArrow = false)
        }

        Spacer(Modifier)
    }

    categoryToDelete?.let { category ->
        CustomDialog(
            title = "Delete category?",
            icon = Icons.Default.DeleteOutline,
            onDismiss = onDismissDeleteDialog,
            confirmText = "Delete",
            onConfirm = { onConfirmDeleteCategory(category) }
        ) {
            Text(
                text = "\"${category.title}\" will be removed. Expenses using this category will be unassigned.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (showCategoryDialog) {
        CategoryDialog(
            onDismiss = onDismissCategoryDialog,
            onConfirm = { name, color -> onAddCategory(name, color.toArgb().toLong()) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingContentPreview() {
    ExpenseTrackerTheme {
        SettingContent(
            categories = listOf(
                Category(id = 1, title = "Food", color = 0xFF4CAF50),
                Category(id = 2, title = "Transport", color = 0xFF2196F3)
            ),
            showCategoryDialog = false,
            categoryToDelete = null,
            onShowCategoryDialog = {}, onDismissCategoryDialog = {},
            onAddCategory = { _, _ -> }, onRequestDeleteCategory = {},
            onConfirmDeleteCategory = {}, onDismissDeleteDialog = {}
        )
    }
}

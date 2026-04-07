package com.example.expensetracker.ui.screens.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.screens.setting.components.SettingContent
import com.example.expensetracker.viewmodel.CategoryViewModel

@Composable
fun SettingScreen(categoryViewModel: CategoryViewModel) {
    val categories by categoryViewModel.getAllCategories.collectAsState()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }

    SettingContent(
        categories = categories,
        showCategoryDialog = showCategoryDialog,
        categoryToDelete = categoryToDelete,
        onShowCategoryDialog = { showCategoryDialog = true },
        onDismissCategoryDialog = { showCategoryDialog = false },
        onAddCategory = { name, color -> categoryViewModel.addCategory(name, color) },
        onRequestDeleteCategory = { categoryToDelete = it },
        onConfirmDeleteCategory = {
            categoryViewModel.deleteCategory(it)
            categoryToDelete = null
        },
        onDismissDeleteDialog = { categoryToDelete = null }
    )
}

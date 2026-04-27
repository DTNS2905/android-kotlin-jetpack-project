package com.example.expensetracker.ui.screens.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.expensetracker.ui.screens.setting.components.SettingContent
import com.example.expensetracker.ui.screens.setting.components.SettingDialog
import com.example.expensetracker.ui.screens.setting.components.SettingUiActions
import com.example.expensetracker.ui.screens.setting.components.SettingUiState
import com.example.expensetracker.viewmodel.SettingViewModel
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun SettingScreen(
    categoryViewModel: CategoryViewModel,
    settingViewModel: SettingViewModel,
    expenseViewModel: ExpenseViewModel
) {
    val categories by categoryViewModel.getAllCategories.collectAsState()
    val budgetState by settingViewModel.budgetState.collectAsState()
    val currencySymbol by settingViewModel.currencySymbol.collectAsState()
    var activeDialog by remember { mutableStateOf<SettingDialog?>(null) }

    SettingContent(
        state = SettingUiState(
            categories = categories,
            budgetState = budgetState,
            activeDialog = activeDialog,
            currencySymbol = currencySymbol
        ),
        actions = SettingUiActions(
            onShowDialog = { activeDialog = it },
            onDismissDialog = { activeDialog = null },
            onAddCategory = { name, color -> categoryViewModel.addCategory(name, color) },
            onConfirmDeleteCategory = { category ->
                categoryViewModel.deleteCategory(category)
                activeDialog = null
            },
            onConfirmBudget = {
                settingViewModel.updateSettings(budget = it)
                activeDialog = null
            },
            onConfirmAlert = {
                settingViewModel.updateSettings(alertThreshold = it)
                activeDialog = null
            },
            onClearAllExpenses = {
                expenseViewModel.deleteAllExpenses()
                activeDialog = null
            },
            onConfirmCurrency = {
                settingViewModel.updateSettings(currencySymbol = it)
                activeDialog = null
            }
        )
    )
}

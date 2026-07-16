package com.example.expensetracker.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.expensetracker.ui.components.MessageType
import com.example.expensetracker.ui.screens.home.components.HomeContent
import com.example.expensetracker.ui.screens.home.components.HomeUiActions
import com.example.expensetracker.ui.screens.home.components.HomeUiState
import com.example.expensetracker.viewmodel.SettingViewModel
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.UiEvent

@Composable
fun HomeScreen(
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel,
    settingViewModel: SettingViewModel,
    navController: NavHostController,
) {
    val expenses by expenseViewModel.getAllFilteredExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val categories by categoryViewModel.getAllCategories.collectAsState()
    val selectedFilters by expenseViewModel.selectedFilter.collectAsState()
    val currencySymbol by settingViewModel.currencySymbol.collectAsState()
    val budgetState by settingViewModel.budgetState.collectAsState()
    val name by settingViewModel.username.collectAsState()
    val imagePath by settingViewModel.imagePath.collectAsState()
    val isAddingExpense by expenseViewModel.isAddingExpense.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf(MessageType.INFO) }

    val homeUiState = HomeUiState(
        expenses = expenses,
        total = total,
        categories = categories,
        selectedFilters = selectedFilters,
        showDialog = showDialog,
        isAddingExpense = isAddingExpense,
        showFilter = showFilter,
        showMessage = showMessage,
        messageText = messageText,
        messageType = messageType,
        budgetState = budgetState,
        currencySymbol = currencySymbol,
        name = name,
        imagePath = imagePath,
    )

    val homeUiActions = HomeUiActions(
        onDismissDialog = { showDialog = false },
        onAddExpense = { title, amount, categoryId ->
            expenseViewModel.addExpense(title, amount, categoryId)
        },
        onShowFilter = { showFilter = true },
        onDismissFilter = { showFilter = false },
        onFilterUpdate = { expenseViewModel.setFilter { _ -> it } },
        onDismissMessage = { showMessage = false },
        onNavigateToExpense = { id -> navController.navigate("expense/$id") },
        onShowDialog = { showDialog = true },
        onSaveProfile = { name, path ->
            settingViewModel.updateSettings(
                username = name,
                imagePath = path
            )
        },
        onNavigateToSettings = { navController.navigate("setting") }
    )

    LaunchedEffect(Unit) {
        expenseViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    messageText = event.message
                    messageType = event.type
                    showMessage = true
                }
            }
        }
    }

    HomeContent(
        homeUiState,
        homeUiActions
    )
}

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
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.UiEvent

@Composable
fun HomeScreen(
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController
) {
    val expenses by expenseViewModel.getAllFilteredExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val categories by categoryViewModel.getAllCategories.collectAsState()
    val selectedFilters by expenseViewModel.selectedFilter.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf(MessageType.INFO) }

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
        expenses = expenses,
        total = total,
        categories = categories,
        selectedFilters = selectedFilters,
        showDialog = showDialog,
        showFilter = showFilter,
        showMessage = showMessage,
        messageText = messageText,
        messageType = messageType,
        onShowDialog = { showDialog = true },
        onDismissDialog = { showDialog = false },
        onAddExpense = { title, amount, categoryId ->
            expenseViewModel.addExpense(title, amount, categoryId)
        },
        onShowFilter = { showFilter = true },
        onDismissFilter = { showFilter = false },
        onFilterUpdate = { expenseViewModel.setFilter { _ -> it } },
        onDismissMessage = { showMessage = false },
        onNavigateToExpense = { id -> navController.navigate("expense/$id") }
    )
}

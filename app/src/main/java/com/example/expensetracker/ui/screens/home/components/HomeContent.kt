package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.components.AnimatedFloatingCard
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.components.MessageType
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.viewmodel.BudgetState
import com.example.expensetracker.viewmodel.Filters

data class HomeUiState(
    val expenses: List<Expense>,
    val total: Double,
    val budgetState: BudgetState,
    val categories: List<Category>,
    val selectedFilters: Filters,
    val showDialog: Boolean,
    val showFilter: Boolean,
    val showMessage: Boolean,
    val messageText: String,
    val messageType: MessageType,
    val currencySymbol: String,
    val name: String,
    val imagePath: String?,
)


data class  HomeUiActions(
    val onShowDialog: () -> Unit,
    val onDismissDialog: () -> Unit,
    val onAddExpense: (String, Double, Int?) -> Unit,
    val onShowFilter: () -> Unit,
    val onDismissFilter: () -> Unit,
    val onFilterUpdate: (Filters) -> Unit,
    val onDismissMessage: () -> Unit,
    val onNavigateToExpense: (Int) -> Unit,
    val onSaveProfile: (String, String?) -> Unit,
    val onNavigateToSettings: () -> Unit = {}
)

@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    homeUiActions: HomeUiActions,
) {
    var showEditProfile by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Welcome back,", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = homeUiState.name, style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground)
                }
                ImageProfile(
                    imageRes = R.drawable.sang,
                    filePath = homeUiState.imagePath,
                    modifier = Modifier,
                    onClick = { showEditProfile = true }
                )
            }

            TotalCard(homeUiState.total, homeUiState.currencySymbol)

            BudgetCard(
                budgetState = homeUiState.budgetState,
                currencySymbol = homeUiState.currencySymbol,
                onSetBudget = homeUiActions.onNavigateToSettings
            )

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "RECENT EXPENSES", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BadgedBox(badge = {
                            if (homeUiState.selectedFilters.time != TimeFilter.ALL ||
                                homeUiState.selectedFilters.categoryId != null
                                ) Badge()
                        }) {
                            IconButton(onClick = homeUiActions.onShowFilter) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                        }
                        AddExpenseButton(showSheet = { homeUiActions.onShowDialog() }, modifier = Modifier)
                    }
                }

                CustomList(
                    expenses = homeUiState.expenses,
                    modifier = Modifier.weight(1f),
                    categories = homeUiState.categories,
                    currencySymbol = homeUiState.currencySymbol,
                    onClick = homeUiActions.onNavigateToExpense
                )
            }
        }

        AnimatedFloatingCard(
            message = homeUiState.messageText,
            type = homeUiState.messageType,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            show = homeUiState.showMessage,
            onDismiss = homeUiActions.onDismissMessage
        )
    }

    if (homeUiState.showFilter) {
        FilterBottomSheet(
            selectedFilters = homeUiState.selectedFilters,
            onFilterUpdate = homeUiActions.onFilterUpdate,
            onDismiss = homeUiActions.onDismissFilter,
            categories = homeUiState.categories
        )
    }

    if (homeUiState.showDialog) {
        AddExpenseDialog(
            categories = homeUiState.categories,
            showDialog = { homeUiActions.onDismissDialog() },
            onAdd = homeUiActions.onAddExpense
        )
    }

    if (showEditProfile) {
        EditProfileDialog(
            currentName = homeUiState.name,
            currentImagePath = homeUiState.imagePath,
            onDismiss = { showEditProfile = false },
            onConfirm = { name, path ->
                homeUiActions.onSaveProfile(name, path)
                showEditProfile = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    ExpenseTrackerTheme {
        HomeContent(
            homeUiState = HomeUiState(
                expenses = emptyList(),
                total = 1240.0,
                categories = listOf(
                    Category(id = 1, title = "Food", color = 0xFF4CAF50),
                    Category(id = 2, title = "Transport", color = 0xFF2196F3)
                ),
                selectedFilters = Filters(TimeFilter.ALL),
                showDialog = false,
                showFilter = false,
                showMessage = false,
                messageText = "",
                messageType = MessageType.INFO,
                budgetState = BudgetState(budget = 2000.0, spent = 1240.0, alertThreshold = 80),
                currencySymbol = "$",
                name = "Sang",
                imagePath = null
            ),
            homeUiActions = HomeUiActions(
                onShowDialog = {}, onDismissDialog = {}, onAddExpense = { _, _, _ -> },
                onShowFilter = {}, onDismissFilter = {}, onFilterUpdate = {},
                onDismissMessage = {},
                onNavigateToExpense = {},
                onSaveProfile = { _, _ -> }
            ),
        )
    }
}
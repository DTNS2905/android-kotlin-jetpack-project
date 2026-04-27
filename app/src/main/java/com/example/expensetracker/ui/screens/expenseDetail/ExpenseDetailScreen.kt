package com.example.expensetracker.ui.screens.expenseDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.screens.expenseDetail.components.ExpenseDetailContent
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.ValidationField
import com.example.expensetracker.utils.amountRules
import com.example.expensetracker.utils.titleRules
import com.example.expensetracker.utils.validate
import com.example.expensetracker.utils.validateAllFields
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseDetailState
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun ExpenseDetailScreen(
    expenseId: Int,
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel,
    currencySymbol: String = "$"
) {
    val state by expenseViewModel.expenseDetailState.collectAsState()
    val categories by categoryViewModel.getAllCategories.collectAsState()
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var titleError = validate(title, titleRules)
    var amountError = validate(amount, amountRules)
    var isEditing by remember { mutableStateOf(false) }
    var showAssignDialog by remember { mutableStateOf(false) }

    LaunchedEffect(expenseId) {
        expenseViewModel.loadExpense(expenseId)
    }

    LaunchedEffect(state) {
        if (state is ExpenseDetailState.Success && !isEditing) {
            val expense = (state as ExpenseDetailState.Success).expense
            title = expense.title
            amount = expense.amount.toString()
        }
    }

    when (val s = state) {
        is ExpenseDetailState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ExpenseDetailState.NotFound -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Expense not found", style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        is ExpenseDetailState.Success -> {
            val expense = s.expense
            ExpenseDetailContent(
                expense = expense,
                assignedCategory = categories.find { it.id == expense.categoryId },
                categories = categories,
                isEditing = isEditing,
                title = title,
                amount = amount,
                titleError = titleError,
                amountError = amountError,
                showAssignDialog = showAssignDialog,
                onTitleChange = { title = it; titleError = null },
                onAmountChange = { amount = it; amountError = null },
                onEditToggle = { isEditing = true },
                onCancelEdit = { isEditing = false },
                onSaveEdit = {
                    val isValid = validateAllFields(listOf(
                        ValidationField(title, titleRules) { titleError = it },
                        ValidationField(amount, amountRules) { amountError = it }
                    ))
                    if (isValid) {
                        expenseViewModel.updateExpense(expense.copy(title = title, amount = amount.toDouble()))
                        isEditing = false
                    }
                },
                onRemoveCategory = {
                    expenseViewModel.updateExpense(expense.copy(categoryId = null))
                },
                onShowAssignDialog = { showAssignDialog = true },
                onDismissAssignDialog = { showAssignDialog = false },
                onConfirmAssign = { categoryId ->
                    expenseViewModel.updateExpense(expense.copy(categoryId = categoryId))
                    showAssignDialog = false
                },
                onDelete = { expenseViewModel.deleteExpense(expense) },
                currencySymbol = currencySymbol
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpenseDetailContentPreview() {
    val fakeExpense = Expense(id = 1, title = "Coffee", amount = 4.50,
        date = System.currentTimeMillis(), categoryId = 1)
    val fakeCategories = listOf(
        Category(id = 1, title = "Food", color = 0xFF4CAF50),
        Category(id = 2, title = "Transport", color = 0xFF2196F3),
        Category(id = 3, title = "Shopping", color = 0xFFE91E63)
    )
    ExpenseTrackerTheme {
        ExpenseDetailContent(
            expense = fakeExpense,
            assignedCategory = fakeCategories.first(),
            categories = fakeCategories,
            isEditing = false,
            title = fakeExpense.title,
            amount = fakeExpense.amount.toString(),
            titleError = null,
            amountError = null,
            showAssignDialog = false,
            onTitleChange = {}, onAmountChange = {}, onEditToggle = {},
            onCancelEdit = {}, onSaveEdit = {}, onRemoveCategory = {},
            onShowAssignDialog = {}, onDismissAssignDialog = {}, onConfirmAssign = {},
            onDelete = {}
        )
    }
}
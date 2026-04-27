package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.screens.home.components.ExpenseItem

@Composable
fun CustomList(
    expenses: List<Expense>,
    modifier: Modifier,
    categories: List<Category> = emptyList(),
    currencySymbol: String = "$",
    onClick: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(expenses, key = { it.id }) { expense ->
            ExpenseItem(
                item = expense,
                onClick = onClick,
                category = categories.find { it.id == expense.categoryId },
                currencySymbol = currencySymbol
            )
        }
    }
}

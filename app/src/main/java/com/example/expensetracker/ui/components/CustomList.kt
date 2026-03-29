package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.exampleExpenseList
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.screens.home.components.ExpenseItem

@Composable
fun CustomList(
    expenses: List<Expense>,
    itemSpacing: Dp = 20.dp,
    modifier: Modifier,
    onClick: (Int) -> Unit = {}

) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(expenses, key = { it.id }) { expense ->
            ExpenseItem(expense, onClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseItemListPreview() {
    CustomList(expenses = exampleExpenseList, modifier = Modifier)
}

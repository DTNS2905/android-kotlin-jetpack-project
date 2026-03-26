package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import com.example.expensetracker.utils.formatDate

@Composable
fun ExpenseDetailScreen(
    expenseId: Int,
    expenseViewModel: ExpenseViewModel
) {
    val expenseDetail by expenseViewModel.getExpense(expenseId).collectAsState()

    expenseDetail?.let { expense ->
        Column() {
            Text(expense.title)
            Text("${expense.amount}")
            Text(formatDate(expense.date))
            Button(
                onClick = {}
            ) {
                Text("Edit")
            }

            Button(
                onClick = {}
            ) {
                Text("Delete")
            }
        }
    }
}

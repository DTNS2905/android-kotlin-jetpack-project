package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.expensetracker.R
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.GeneralLayout
import com.example.expensetracker.ui.components.AddExpenseButton
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.components.TotalCard
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel

@Composable
fun HomeScreen(expenseViewModel: ExpenseViewModel) {

    val expenses by expenseViewModel.allExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    GeneralLayout(fabAction = setShowDialog) {
        Column {
            Text(
                text = "Welcome. Sang",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(10.dp))
            ImageProfile(imageRes = R.drawable.sang, modifier = Modifier)
        }
        TotalCard(total)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            AddExpenseButton(
                showSheet = setShowDialog,
                modifier = Modifier.fillMaxWidth()
            )
        }
        ExpenseList(expenses)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )
        ) {
            AddExpenseDialog(
                showDialog = { setShowDialog(false) },
                onAdd = { title, amount -> expenseViewModel.addExpense(title, amount) }
            )
        }
    }
}

@Composable
fun AddExpenseDialog(showDialog: (Boolean) -> Unit, onAdd: (String, Double) -> Unit = { _, _ -> }) {
    val (title, setTitle) = remember { mutableStateOf("") }
    val (amount, setAmount) = remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                label = { Text("Title") },
                value = title,
                onValueChange = { setTitle(it) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                label = { Text("Amount") },
                value = amount,
                onValueChange = { setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotBlank() && parsedAmount > 0) {
                            onAdd(title, parsedAmount)
                            showDialog(false)
                        }
                    }
                ) {
                    Text(text = "Add")
                }
                Button(
                    onClick = { showDialog(false) }
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    AddExpenseDialog({})
}

@Composable
fun ExpenseList(expenses: List<Expense>) {
    CustomList(expenses, modifier = Modifier)
}

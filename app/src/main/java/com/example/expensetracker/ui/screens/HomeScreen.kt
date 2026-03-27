package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.AddExpenseButton
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.components.TotalCard
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel

@Composable
fun HomeScreen(
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController
) {
    val expenses by expenseViewModel.allExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    // Header: welcome text left, avatar right
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Sang",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        ImageProfile(imageRes = R.drawable.sang, modifier = Modifier)
    }

    Spacer(modifier = Modifier.height(20.dp))

    TotalCard(total)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Expenses",
            style = MaterialTheme.typography.titleMedium,
        )
        AddExpenseButton(
            showSheet = setShowDialog,
            modifier = Modifier
        )
    }

    CustomList(expenses, modifier = Modifier, onClick = { id ->
        navController.navigate("expense/$id")
    })

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
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .wrapContentHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Add Expense",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            OutlinedTextField(
                label = { Text("Title") },
                value = title,
                onValueChange = { setTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                label = { Text("Amount") },
                value = amount,
                onValueChange = { setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("$") }
            )
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = { showDialog(false) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val parsedAmount = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotBlank() && parsedAmount > 0) {
                            onAdd(title, parsedAmount)
                            showDialog(false)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add")
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
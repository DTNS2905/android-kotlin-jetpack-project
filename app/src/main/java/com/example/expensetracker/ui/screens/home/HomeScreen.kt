package com.example.expensetracker.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.ui.screens.home.components.AddExpenseButton
import com.example.expensetracker.ui.components.AnimatedFloatingCard
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.components.MessageType
import com.example.expensetracker.ui.screens.home.components.TotalCard
import com.example.expensetracker.utils.ValidationField
import com.example.expensetracker.utils.amountRules
import com.example.expensetracker.utils.titleRules
import com.example.expensetracker.utils.validate
import com.example.expensetracker.utils.validateAllFields
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.UiEvent

@Composable
fun HomeScreen(
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController
) {
    val expenses by expenseViewModel.allExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf(MessageType.INFO) }

    LaunchedEffect(Unit) {
        expenseViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowMessage -> {
                    messageText = event.message
                    messageType = event.type
                    showMessage = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
        }

        AnimatedFloatingCard(
            message = messageText,
            type = messageType,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            show = showMessage,
            onDismiss = { showMessage = false }
        )
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
                onAdd = { title, amount ->
                    expenseViewModel.addExpense(title, amount)
                }
            )
        }
    }
}

@Composable
fun AddExpenseDialog(showDialog: (Boolean) -> Unit, onAdd: (String, Double) -> Unit = { _, _ -> }) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

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
                onValueChange = { newTitle -> title = newTitle; titleError = null },
                isError = titleError != null,
                supportingText = {
                    titleError?.let { Text(it, color = MaterialTheme.colorScheme.error)}
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                label = { Text("Amount") },
                value = amount,
                onValueChange = { newAmount -> amount = newAmount; amountError = null },
                isError = amountError != null,
                supportingText = {
                    amountError?.let { Text(it, color = MaterialTheme.colorScheme.error)}
                },
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
                        val isValid = validateAllFields(
                            listOf(
                                ValidationField(title, titleRules) { titleError = it },
                                ValidationField(amount, amountRules) { amountError = it }
                            )
                        )
                        if (isValid) {
                            onAdd(title, amount.toDouble())
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
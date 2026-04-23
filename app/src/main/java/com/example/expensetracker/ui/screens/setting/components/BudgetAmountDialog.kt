package com.example.expensetracker.ui.screens.setting.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetracker.ui.components.AppTextField
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.budgetRules
import com.example.expensetracker.utils.validate

@Composable
fun BudgetAmountDialog(
    currentBudget: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var input by remember { mutableStateOf(if(currentBudget > 0) currentBudget.toString() else "") }
    val error = validate(input, budgetRules)


    CustomDialog(
        title = "Set Monthly Budget",
        onDismiss = { onDismiss },
        onConfirm = { input.toDoubleOrNull()?.let { onConfirm(it) }},
        confirmEnabled = error == null && input.isNotEmpty()
    ) {
        AppTextField(
            value = input,
            onValueChange = {input = it},
            label = "Amount",
            prefix = "$",
            keyboardType = KeyboardType.Decimal,
            error =  if (input.isNotEmpty()) error else null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun BudgetAmountDialogPreview() {
    ExpenseTrackerTheme {
        BudgetAmountDialog(currentBudget = 2000.0, onDismiss = {}, onConfirm = {})
    }
}
package com.example.expensetracker.ui.screens.setting.components

import com.example.expensetracker.utils.alertRules
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
import com.example.expensetracker.utils.validate

@Composable
fun BudgetAlertDialog(
    currentThreshold: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var input by remember { mutableStateOf(currentThreshold.toString()) }
    val error = validate(input, alertRules)


    CustomDialog(
        title = "Budget Alert",
        onDismiss = onDismiss,
        onConfirm = { input.toIntOrNull()?.let { onConfirm(it) }},
        confirmEnabled = error == null && input.isNotEmpty()
    ) {
        AppTextField(
            value = input,
            onValueChange = {input = it},
            label = "Alert at (%)",
            suffix = "%",
            keyboardType = KeyboardType.Decimal,
            error =  if (input.isNotEmpty()) error else null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun BudgetAlertDialogPreview() {
    ExpenseTrackerTheme {
        BudgetAlertDialog(currentThreshold = 99, onDismiss = {}, onConfirm = {})
    }
}
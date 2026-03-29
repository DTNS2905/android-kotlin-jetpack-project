package com.example.expensetracker.ui.screens.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddExpenseButton(
    modifier: Modifier,
    showSheet: (Boolean) -> Unit,
) {
    IconButton(
        onClick = {showSheet(true)},
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = modifier
    ) {
        Icon(
            contentDescription = "Add Expense Button",
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
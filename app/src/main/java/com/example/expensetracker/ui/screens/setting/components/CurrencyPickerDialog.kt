package com.example.expensetracker.ui.screens.setting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.components.CustomDialog

private val currencies = listOf("$", "€", "£", "¥", "₫", "₩", "฿")

@Composable
fun CurrencyPickerDialog(
    current: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedCurrency by remember { mutableStateOf(current) }
    CustomDialog(
        title = "Currency Symbol",
        onDismiss = onDismiss,
        onConfirm = { onConfirm(selectedCurrency) }
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            currencies.forEach { symbol ->
                FilterChip(
                    selected = selectedCurrency == symbol,
                    onClick = { selectedCurrency = symbol },
                    label = { Text(symbol) }
                )
            }
        }
    }

}


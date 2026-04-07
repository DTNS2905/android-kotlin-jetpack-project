package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.utils.ValidationField
import com.example.expensetracker.utils.amountRules
import com.example.expensetracker.utils.titleRules
import com.example.expensetracker.utils.validateAllFields

@Composable
fun AddExpenseDialog(
    showDialog: (Boolean) -> Unit,
    categories: List<Category> = emptyList(),
    onAdd: (String, Double, Int?) -> Unit = { _, _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    CustomDialog(
        title = "Add Expense",
        icon = Icons.Rounded.AddCircle,
        confirmText = "Add",
        onDismiss = { showDialog(false) },
        onConfirm = {
            val isValid = validateAllFields(
                listOf(
                    ValidationField(title, titleRules) { titleError = it },
                    ValidationField(amount, amountRules) { amountError = it }
                )
            )
            if (isValid) {
                onAdd(title, amount.toDouble(), selectedCategoryId)
                showDialog(false)
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                label = { Text("Title") },
                value = title,
                onValueChange = { title = it; titleError = null },
                isError = titleError != null,
                supportingText = {
                    titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                label = { Text("Amount") },
                value = amount,
                onValueChange = { amount = it; amountError = null },
                isError = amountError != null,
                supportingText = {
                    amountError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text("$") }
            )
            if (categories.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories, key = { it.id }) { category ->
                        CategoryChip(
                            name = category.title,
                            color = Color(category.color.toInt()),
                            selected = selectedCategoryId == category.id,
                            onClick = {
                                selectedCategoryId =
                                    if (selectedCategoryId == category.id) null else category.id
                            }
                        )
                    }
                }
            }
        }
    }
}

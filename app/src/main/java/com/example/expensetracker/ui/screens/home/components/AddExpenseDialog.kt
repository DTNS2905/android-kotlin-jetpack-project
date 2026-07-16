package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.expensetracker.ui.components.AppTextField
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
    currencySymbol: String,
    isLoading: Boolean = false,
    onAdd: (String, Double, Int?) -> Unit = { _, _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }
    var awaitingClose by remember { mutableStateOf(false) }

    // Close the dialog once the insert we submitted has finished (loading true -> false).
    LaunchedEffect(isLoading) {
        if (isLoading) {
            awaitingClose = true
        } else if (awaitingClose) {
            showDialog(false)
        }
    }

    CustomDialog(
        title = "Add Expense",
        icon = Icons.Rounded.AddCircle,
        confirmText = "Add",
        confirmLoading = isLoading,
        onDismiss = { if (!isLoading) showDialog(false) },
        onConfirm = {
            if (isLoading) return@CustomDialog
            val isValid = validateAllFields(
                listOf(
                    ValidationField(title, titleRules) { titleError = it },
                    ValidationField(amount, amountRules) { amountError = it }
                )
            )
            if (isValid) {
                onAdd(title, amount.toDouble(), selectedCategoryId)
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = title,
                onValueChange = { title = it; titleError = null },
                label = "Title",
                error = titleError,
                modifier = Modifier.fillMaxWidth()
            )
            AppTextField(
                value = amount,
                onValueChange = { amount = it; amountError = null },
                label = "Amount",
                error = amountError,
                prefix = currencySymbol,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.fillMaxWidth()
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

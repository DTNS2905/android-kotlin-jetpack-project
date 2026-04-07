package com.example.expensetracker.ui.screens.expenseDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.components.AppButton
import com.example.expensetracker.ui.components.ButtonVariant
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.screens.home.components.AssignCategoryDialog
import com.example.expensetracker.utils.formatDate
import com.example.expensetracker.utils.formatDollar

@Composable
fun ExpenseDetailContent(
    expense: Expense,
    assignedCategory: Category?,
    categories: List<Category>,
    isEditing: Boolean,
    title: String,
    amount: String,
    titleError: String?,
    amountError: String?,
    showAssignDialog: Boolean,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onRemoveCategory: () -> Unit,
    onShowAssignDialog: () -> Unit,
    onDismissAssignDialog: () -> Unit,
    onConfirmAssign: (Int?) -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Rounded.Receipt, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(26.dp))
                    }
                    Column {
                        Text(expense.title, style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("- ${formatDollar(expense.amount)}", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(2.dp))
                        Text(formatDate(expense.date), style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Category row
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Category", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    if (assignedCategory != null) {
                        CategoryChip(
                            name = assignedCategory.title,
                            color = Color(assignedCategory.color.toInt()),
                            trailingIcon = {
                                Icon(Icons.Default.Close, "Remove category",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(assignedCategory.color.toInt()))
                            },
                            onClick = onRemoveCategory
                        )
                    } else {
                        AppButton(text = "Assign category", icon = Icons.Default.Add,
                            onClick = onShowAssignDialog, variant = ButtonVariant.SECONDARY)
                    }
                }
            }
        }

        // Edit form
        if (isEditing) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Edit Expense", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(label = { Text("Title") }, value = title, onValueChange = onTitleChange,
                        isError = titleError != null,
                        supportingText = { titleError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(label = { Text("Amount") }, value = amount, onValueChange = onAmountChange,
                        isError = amountError != null,
                        supportingText = { amountError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true, prefix = { Text("$") })
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppButton(text = "Cancel", onClick = onCancelEdit,
                            variant = ButtonVariant.SECONDARY, modifier = Modifier.weight(1f))
                        AppButton(text = "Save", onClick = onSaveEdit,
                            variant = ButtonVariant.PRIMARY, modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            AppButton(text = "Edit Expense", onClick = onEditToggle,
                variant = ButtonVariant.PRIMARY, modifier = Modifier.fillMaxWidth())
        }

        Spacer(Modifier.weight(1f))

        AppButton(text = "Delete Expense", onClick = onDelete,
            variant = ButtonVariant.DANGER, modifier = Modifier.fillMaxWidth())
    }

    if (showAssignDialog) {
        AssignCategoryDialog(
            categories = categories,
            currentCategoryId = expense.categoryId,
            onDismiss = onDismissAssignDialog,
            onConfirm = onConfirmAssign
        )
    }
}
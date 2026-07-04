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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Receipt
import com.example.expensetracker.utils.iconVectorFor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.components.AppButton
import com.example.expensetracker.ui.components.AppTextField
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
    notes: String,
    titleError: String?,
    amountError: String?,
    showAssignDialog: Boolean,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveEdit: () -> Unit,
    onRemoveCategory: () -> Unit,
    onShowAssignDialog: () -> Unit,
    onDismissAssignDialog: () -> Unit,
    onConfirmAssign: (Int?) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit = {},
    currencySymbol: String = "$"
) {
    val categoryColor = assignedCategory?.let { Color(it.color.toInt()) }
        ?: MaterialTheme.colorScheme.primary

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                if (!isEditing) {
                    IconButton(onClick = onEditToggle) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }

            // Scrollable centered content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // Category-tinted icon circle
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(categoryColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVectorFor(assignedCategory?.icon ?: "receipt"),
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "-${formatDollar(expense.amount, currencySymbol)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = formatDate(expense.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // Category row
                if (assignedCategory != null) {
                    CategoryChip(
                        name = assignedCategory.title,
                        color = categoryColor,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove category",
                                modifier = Modifier.size(16.dp),
                                tint = categoryColor
                            )
                        },
                        onClick = onRemoveCategory
                    )
                } else {
                    AppButton(
                        text = "Assign category",
                        icon = Icons.Default.Add,
                        onClick = onShowAssignDialog,
                        variant = ButtonVariant.SECONDARY
                    )
                }

                // Notes display (view mode)
                if (!isEditing && !expense.notes.isNullOrBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                "Notes",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(expense.notes, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // Edit form
                if (isEditing) {
                    Spacer(Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Edit Expense", style = MaterialTheme.typography.titleMedium)
                            AppTextField(
                                value = title,
                                onValueChange = onTitleChange,
                                label = "Title",
                                error = titleError,
                                modifier = Modifier.fillMaxWidth()
                            )
                            AppTextField(
                                value = amount,
                                onValueChange = onAmountChange,
                                label = "Amount",
                                error = amountError,
                                prefix = currencySymbol,
                                keyboardType = KeyboardType.Decimal,
                                modifier = Modifier.fillMaxWidth()
                            )
                            AppTextField(
                                value = notes,
                                onValueChange = onNotesChange,
                                label = "Notes (optional)",
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                AppButton(
                                    text = "Cancel",
                                    onClick = onCancelEdit,
                                    variant = ButtonVariant.SECONDARY,
                                    modifier = Modifier.weight(1f)
                                )
                                AppButton(
                                    text = "Save",
                                    onClick = onSaveEdit,
                                    variant = ButtonVariant.PRIMARY,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(80.dp))
            }
        }

        // Delete anchored at bottom
        AppButton(
            text = "Delete Expense",
            onClick = onDelete,
            variant = ButtonVariant.DANGER,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
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

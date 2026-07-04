package com.example.expensetracker.ui.screens.setting.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.ExpenseTemplate
import com.example.expensetracker.ui.components.AppTextField
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.components.CustomDialog
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.screens.home.components.EditProfileDialog
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.ValidationField
import com.example.expensetracker.utils.amountRules
import com.example.expensetracker.utils.formatDollar
import com.example.expensetracker.utils.titleRules
import com.example.expensetracker.utils.validateAllFields
import com.example.expensetracker.viewmodel.BudgetState

sealed class SettingDialog {
    object AddCategory : SettingDialog()
    object Budget : SettingDialog()
    object Alert : SettingDialog()
    object ClearAllExpense : SettingDialog()
    object Currency : SettingDialog()
    object EditProfile : SettingDialog()
    object AddExpenseTemplate : SettingDialog()
    data class DeleteCategory(val category: Category) : SettingDialog()
    data class DeleteExpenseTemplate(val template: ExpenseTemplate) : SettingDialog()
}

data class SettingUiState(
    val categories: List<Category> = emptyList(),
    val budgetState: BudgetState = BudgetState(),
    val activeDialog: SettingDialog? = null,
    val currencySymbol: String,
    val username: String = "",
    val imagePath: String? = null,
    val darkMode: Boolean = false,
    val dailyReminders: Boolean = false,
    val templates: List<ExpenseTemplate> = emptyList(),
)

data class SettingUiActions(
    val onAddCategory: (String, Long, String) -> Unit = { _, _, _ -> },
    val onConfirmDeleteCategory: (Category) -> Unit = {},
    val onConfirmBudget: (Double) -> Unit = {},
    val onConfirmAlert: (Int) -> Unit = {},
    val onShowDialog: (SettingDialog) -> Unit = {},
    val onDismissDialog: () -> Unit = {},
    val onClearAllExpenses: () -> Unit = {},
    val onConfirmCurrency: (String) -> Unit = {},
    val onSaveProfile: (String, String?) -> Unit = { _, _ -> },
    val onDarkModeChange: (Boolean) -> Unit = {},
    val onDailyRemindersChange: (Boolean) -> Unit = {},
    val onAddExpenseTemplate: (String, Double, Int?) -> Unit = { _, _, _ -> },
    val onTemplateEnabledChange: (ExpenseTemplate, Boolean) -> Unit = { _, _ -> },
    val onAddTemplateExpenseToday: (ExpenseTemplate) -> Unit = {},
    val onConfirmDeleteTemplate: (ExpenseTemplate) -> Unit = {}
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingContent(
    state: SettingUiState,
    actions: SettingUiActions
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.headlineMedium)

        // Profile card
        Card(
            onClick = { actions.onShowDialog(SettingDialog.EditProfile) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ImageProfile(
                    imageRes = R.drawable.sang,
                    modifier = Modifier,
                    size = 64.dp,
                    filePath = state.imagePath
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = state.username.ifBlank { "Your Name" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Edit profile",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        SettingSection(title = "Categories") {
            if (state.categories.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.categories.forEach { category ->
                            val color = Color(category.color.toInt())
                            CategoryChip(
                                name = category.title,
                                color = color,
                                onClick = { actions.onShowDialog(SettingDialog.DeleteCategory(category)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Delete ${category.title}",
                                        modifier = Modifier.size(16.dp),
                                        tint = color
                                    )
                                }
                            )
                        }
                    }
                }
            }
            SettingItem(
                icon = Icons.Default.Add,
                title = "Add Category",
                showArrow = false,
                onClick = { actions.onShowDialog(SettingDialog.AddCategory) }
            )
        }

        SettingSection(title = "Budget") {
            SettingItem(
                icon = Icons.Default.Wallet,
                title = "Monthly Budget",
                subtitle = if (state.budgetState.budget > 0)
                    formatDollar(state.budgetState.budget, state.currencySymbol)
                else "Not set",
                onClick = { actions.onShowDialog(SettingDialog.Budget) }
            )
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Budget Alert",
                subtitle = "${state.budgetState.alertThreshold}%",
                onClick = { actions.onShowDialog(SettingDialog.Alert) }
            )
        }

        SettingSection(title = "Expense Templates") {
            if (state.templates.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.templates.forEach { template ->
                        ExpenseTemplateItem(
                            template = template,
                            category = state.categories.firstOrNull { it.id == template.categoryId },
                            currencySymbol = state.currencySymbol,
                            onAddToday = { actions.onAddTemplateExpenseToday(template) },
                            onEnabledChange = { actions.onTemplateEnabledChange(template, it) },
                            onDelete = {
                                actions.onShowDialog(SettingDialog.DeleteExpenseTemplate(template))
                            }
                        )
                    }
                }
            }

            SettingItem(
                icon = Icons.Default.Add,
                title = "Add expense template",
                showArrow = false,
                onClick = { actions.onShowDialog(SettingDialog.AddExpenseTemplate) }
            )
        }

        SettingSection(title = "Appearance") {
            SettingItem(
                icon = Icons.Filled.DarkMode,
                title = "Dark mode",
                checked = state.darkMode,
                onCheckedChange = { actions.onDarkModeChange(it) }
            )
        }

        SettingSection(title = "Notifications") {
            SettingItem(
                icon = Icons.Outlined.Notifications,
                title = "Daily reminders",
                checked = state.dailyReminders,
                onCheckedChange = { actions.onDailyRemindersChange(it) }
            )
        }

        SettingSection(title = "Data & Security") {
            SettingItem(
                icon = Icons.Default.AttachMoney,
                title = "Currency symbol",
                subtitle = state.currencySymbol,
                onClick = { actions.onShowDialog(SettingDialog.Currency) }
            )
            SettingItem(
                icon = Icons.Outlined.FileUpload,
                title = "Export data",
                onClick = {
                    Toast.makeText(context, "Export coming soon", Toast.LENGTH_SHORT).show()
                }
            )
            SettingItem(
                icon = Icons.Default.DeleteOutline,
                title = "Clear all expenses",
                tint = MaterialTheme.colorScheme.error,
                showArrow = false,
                onClick = { actions.onShowDialog(SettingDialog.ClearAllExpense) }
            )
        }

        Spacer(Modifier)
    }

    when (val dialog = state.activeDialog) {
        is SettingDialog.EditProfile -> EditProfileDialog(
            currentImagePath = state.imagePath,
            currentName = state.username,
            onDismiss = actions.onDismissDialog,
            onConfirm = { name, path -> actions.onSaveProfile(name, path) }
        )
        is SettingDialog.Budget -> BudgetAmountDialog(
            currentBudget = state.budgetState.budget,
            onDismiss = actions.onDismissDialog,
            onConfirm = { actions.onConfirmBudget(it) }
        )
        is SettingDialog.Alert -> BudgetAlertDialog(
            currentThreshold = state.budgetState.alertThreshold,
            onDismiss = actions.onDismissDialog,
            onConfirm = { actions.onConfirmAlert(it) }
        )
        is SettingDialog.AddCategory -> CategoryDialog(
            onDismiss = actions.onDismissDialog,
            onConfirm = { name, color, icon -> actions.onAddCategory(name, color.toArgb().toLong(), icon) }
        )
        is SettingDialog.AddExpenseTemplate -> ExpenseTemplateDialog(
            categories = state.categories,
            currencySymbol = state.currencySymbol,
            onDismiss = actions.onDismissDialog,
            onConfirm = { title, amount, categoryId ->
                actions.onAddExpenseTemplate(title, amount, categoryId)
                actions.onDismissDialog()
            }
        )
        is SettingDialog.DeleteCategory -> CustomDialog(
            title = "Delete category?",
            icon = Icons.Default.DeleteOutline,
            onDismiss = actions.onDismissDialog,
            confirmText = "Delete",
            onConfirm = { actions.onConfirmDeleteCategory(dialog.category) }
        ) {
            Text(
                text = "\"${dialog.category.title}\" will be removed. Expenses using this category will be unassigned.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        is SettingDialog.DeleteExpenseTemplate -> CustomDialog(
            title = "Delete template?",
            icon = Icons.Default.DeleteOutline,
            onDismiss = actions.onDismissDialog,
            confirmText = "Delete",
            onConfirm = {
                actions.onConfirmDeleteTemplate(dialog.template)
                actions.onDismissDialog()
            }
        ) {
            Text(
                text = "\"${dialog.template.title}\" will stop adding recurring expenses.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        is SettingDialog.ClearAllExpense -> CustomDialog(
            title = "Clear all expenses?",
            icon = Icons.Default.DeleteOutline,
            onDismiss = actions.onDismissDialog,
            confirmText = "Clear",
            onConfirm = { actions.onClearAllExpenses() }
        ) {
            Text(
                text = "All expenses will be removed. This action cannot be undone.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        is SettingDialog.Currency -> CurrencyPickerDialog(
            current = state.currencySymbol,
            onDismiss = actions.onDismissDialog,
            onConfirm = { actions.onConfirmCurrency(it) }
        )
        null -> Unit
    }
}

@Composable
private fun ExpenseTemplateItem(
    template: ExpenseTemplate,
    category: Category?,
    currencySymbol: String,
    onAddToday: () -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = buildString {
                        append(formatDollar(template.amount, currencySymbol))
                        append(" daily")
                        category?.let { append(" - ${it.title}") }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onAddToday) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Add today",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete template",
                    tint = MaterialTheme.colorScheme.error
                )
            }

            Switch(
                checked = template.enabled,
                onCheckedChange = onEnabledChange
            )
        }
    }
}

@Composable
private fun ExpenseTemplateDialog(
    categories: List<Category>,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Int?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    CustomDialog(
        title = "Add Template",
        icon = Icons.Default.ReceiptLong,
        confirmText = "Save",
        onDismiss = onDismiss,
        onConfirm = {
            val isValid = validateAllFields(
                listOf(
                    ValidationField(title, titleRules) { titleError = it },
                    ValidationField(amount, amountRules) { amountError = it }
                )
            )

            if (isValid) {
                onConfirm(title, amount.toDouble(), selectedCategoryId)
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = null
                },
                label = "Title",
                error = titleError,
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = null
                },
                label = "Daily amount",
                error = amountError,
                prefix = currencySymbol,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.fillMaxWidth()
            )

            CategorySelector(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = { selectedCategoryId = it }
            )
        }
    }
}

@Composable
private fun CategorySelector(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    if (categories.isNotEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories, key = { it.id }) { category ->
                CategoryChip(
                    name = category.title,
                    color = Color(category.color.toInt()),
                    selected = selectedCategoryId == category.id,
                    onClick = {
                        onCategorySelected(
                            if (selectedCategoryId == category.id) null else category.id
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingContentPreview() {
    ExpenseTrackerTheme {
        SettingContent(
            state = SettingUiState(
                categories = listOf(
                    Category(id = 1, title = "Food", color = 0xFF4CAF50),
                    Category(id = 2, title = "Transport", color = 0xFF2196F3)
                ),
                budgetState = BudgetState(budget = 2000.0, spent = 1240.0, alertThreshold = 80),
                currencySymbol = "$",
                username = "Sang",
                imagePath = null
            ),
            actions = SettingUiActions()
        )
    }
}

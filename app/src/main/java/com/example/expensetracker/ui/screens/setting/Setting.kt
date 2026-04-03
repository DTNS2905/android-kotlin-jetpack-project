package com.example.expensetracker.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.data.colorsForPicker
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.ui.screens.setting.components.CategoryDialog
import com.example.expensetracker.ui.screens.setting.components.SettingItem
import com.example.expensetracker.ui.screens.setting.components.SettingSection
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

private data class CategoryItem(val name: String, val color: Color)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingScreen() {
    var showCategoryDialog by remember { mutableStateOf(false) }
    var categories by remember {
        mutableStateOf(
            listOf(
                CategoryItem("Food", colorsForPicker[2]),
                CategoryItem("Transport", colorsForPicker[0]),
                CategoryItem("Bills", colorsForPicker[1])
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        // Profile
        SettingSection(title = "Profile") {
            SettingItem(
                icon = Icons.Default.Person,
                title = "Sang",
                subtitle = "Edit profile"
            )
        }

        // Categories
        SettingSection(title = "Categories") {
            if (categories.isNotEmpty()) {
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
                        categories.forEach { category ->
                            CategoryChip(name = category.name, color = category.color)
                        }
                    }
                }
            }
            SettingItem(
                icon = Icons.Default.Add,
                title = "Add Category",
                showArrow = false,
                onClick = { showCategoryDialog = true }
            )
        }

        // Budget
        SettingSection(title = "Budget") {
            SettingItem(
                icon = Icons.Default.Wallet,
                title = "Monthly Budget",
                subtitle = "$0.00"
            )
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "Budget Alert",
                subtitle = "80%"
            )
        }

        // Data
        SettingSection(title = "Data") {
            SettingItem(
                icon = Icons.Outlined.FileUpload,
                title = "Export to CSV"
            )
            SettingItem(
                icon = Icons.Default.DeleteOutline,
                title = "Clear all expenses",
                tint = MaterialTheme.colorScheme.error,
                showArrow = false
            )
        }

        Spacer(Modifier)
    }

    if (showCategoryDialog) {
        CategoryDialog(
            onDismiss = { showCategoryDialog = false },
            onConfirm = { name, color ->
                categories = categories + CategoryItem(name, color)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingScreen() {
    ExpenseTrackerTheme {
        SettingScreen()
    }
}
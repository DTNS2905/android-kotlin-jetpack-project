package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.components.CategoryChip
import com.example.expensetracker.utils.toLabel
import com.example.expensetracker.viewmodel.Filters

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    selectedFilters: Filters,
    categories: List<Category>,
    onFilterUpdate: (Filters) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Filter",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Time",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TimeFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = selectedFilters.time == filter,
                        onClick = { onFilterUpdate(selectedFilters.copy(time = filter)) },
                        label = { Text(filter.toLabel()) }
                    )
                }
            }

            if (categories.isNotEmpty()) {
                Text(
                    "Category",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = selectedFilters.categoryId == null,
                        onClick = { onFilterUpdate(selectedFilters.copy(categoryId = null)) },
                        label = { Text("All") }
                    )
                    categories.forEach { category ->
                        val color = Color(category.color.toInt())
                        CategoryChip(
                            name = category.title,
                            color = color,
                            selected = selectedFilters.categoryId == category.id,
                            onClick = { onFilterUpdate(selectedFilters.copy(categoryId = category.id)) }
                        )
                    }
                }
            }

        }
    }
}


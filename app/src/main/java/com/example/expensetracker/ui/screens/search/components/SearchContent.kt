package com.example.expensetracker.ui.screens.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun SearchContent(
    query: String,
    results: List<Expense>,
    categories: List<Category>,
    currencySymbol: String = "$",
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onNavigateToExpense: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search expenses...") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearQuery) {
                        Icon(Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp)
        )

        when {
            query.isBlank() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Type to search expenses", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            results.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No expenses found for \"$query\"", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            else -> {
                CustomList(expenses = results, modifier = Modifier,
                    categories = categories, currencySymbol = currencySymbol, onClick = onNavigateToExpense)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchContentPreview() {
    ExpenseTrackerTheme {
        SearchContent(
            query = "",
            results = emptyList(),
            categories = emptyList(),
            onQueryChange = {}, onClearQuery = {}, onNavigateToExpense = {}
        )
    }
}

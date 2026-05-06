package com.example.expensetracker.ui.screens.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.room.model.Expense
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchContent(
    query: String,
    results: List<Expense>,
    categories: List<Category>,
    currencySymbol: String = "$",
    recentSearches: List<String> = emptyList(),
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onRecentSearchClick: (String) -> Unit = {},
    onNavigateToExpense: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search expenses, categories…") },
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
                if (recentSearches.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "RECENT SEARCHES",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recentSearches.forEach { recent ->
                                SuggestionChip(
                                    onClick = { onRecentSearchClick(recent) },
                                    label = { Text(recent) }
                                )
                            }
                        }
                    }
                }

                if (categories.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "FILTER BY CATEGORY",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { category ->
                                val catColor = Color(category.color.toInt())
                                FilterChip(
                                    selected = false,
                                    onClick = { onRecentSearchClick(category.title) },
                                    label = { Text(category.title) },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(catColor),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = category.title.take(1).uppercase(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = catColor.copy(alpha = 0.08f),
                                        labelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }

                if (recentSearches.isEmpty() && categories.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Type to search expenses",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            results.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No expenses found for \"$query\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                CustomList(
                    expenses = results,
                    modifier = Modifier,
                    categories = categories,
                    currencySymbol = currencySymbol,
                    onClick = onNavigateToExpense
                )
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
            categories = listOf(
                Category(id = 1, title = "Food", color = 0xFF4CAF50),
                Category(id = 2, title = "Transport", color = 0xFF2196F3),
                Category(id = 3, title = "Shopping", color = 0xFFFF6B35),
            ),
            recentSearches = listOf("Groceries", "Lyft", "Coffee"),
            onQueryChange = {}, onClearQuery = {}, onNavigateToExpense = {}
        )
    }
}

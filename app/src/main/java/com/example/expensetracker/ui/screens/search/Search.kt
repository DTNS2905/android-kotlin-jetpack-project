package com.example.expensetracker.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.expensetracker.ui.screens.search.components.SearchContent
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun SearchScreen(
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController,
    currencySymbol: String = "$"
) {
    val query by expenseViewModel.searchQuery.collectAsState()
    val results by expenseViewModel.searchResults.collectAsState()
    val categories by categoryViewModel.getAllCategories.collectAsState()

    var recentSearches by remember { mutableStateOf(listOf<String>()) }

    fun addToRecent(q: String) {
        if (q.isBlank()) return
        recentSearches = (listOf(q) + recentSearches.filter { it != q }).take(5)
    }

    SearchContent(
        query = query,
        results = results,
        categories = categories,
        currencySymbol = currencySymbol,
        recentSearches = recentSearches,
        onQueryChange = {
            if (it.isBlank() && query.isNotBlank()) addToRecent(query)
            expenseViewModel.setSearchQuery(it)
        },
        onClearQuery = {
            addToRecent(query)
            expenseViewModel.setSearchQuery("")
        },
        onRecentSearchClick = { expenseViewModel.setSearchQuery(it) },
        onNavigateToExpense = { id -> navController.navigate("expense/$id") }
    )
}

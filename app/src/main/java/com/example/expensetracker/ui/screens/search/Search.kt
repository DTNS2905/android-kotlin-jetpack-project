package com.example.expensetracker.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.expensetracker.ui.screens.search.components.SearchContent
import com.example.expensetracker.viewmodel.CategoryViewModel
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun SearchScreen(
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController
) {
    val query by expenseViewModel.searchQuery.collectAsState()
    val results by expenseViewModel.searchResults.collectAsState()
    val categories by categoryViewModel.getAllCategories.collectAsState()

    SearchContent(
        query = query,
        results = results,
        categories = categories,
        onQueryChange = { expenseViewModel.setSearchQuery(it) },
        onClearQuery = { expenseViewModel.setSearchQuery("") },
        onNavigateToExpense = { id -> navController.navigate("expense/$id") }
    )
}

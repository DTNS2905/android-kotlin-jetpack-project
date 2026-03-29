package com.example.expensetracker.ui.layouts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.expensetracker.Routes
import com.example.expensetracker.ui.components.MenuBar
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun GeneralLayout(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            if (currentRoute in Routes.bottomBarRoutes) {
                MenuBar(navController, currentRoute)
            }
        },
        containerColor = MaterialTheme.colorScheme.background

    ) { paddingValues ->
        ExpenseTrackerTheme {
            content(paddingValues)
        }
    }
}
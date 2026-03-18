package com.example.expensetracker.ui.screens

import androidx.compose.runtime.Composable
import com.example.expensetracker.ui.GeneralLayout
import com.example.expensetracker.ui.components.TotalCard

@Composable
fun HomeScreen() {
    GeneralLayout() {
        TotalCard(100.00)
    }
}
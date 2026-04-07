package com.example.expensetracker.data

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.room.model.Expense

val exampleExpenseList = listOf(
    Expense(1, "Groceries", 45.50, 1742428800000L, null),
    Expense(2, "Coffee", 3.25, 1742428800000L, null),
    Expense(3, "Transport", 15.00, 1742342400000L, null),
    Expense(4, "Internet Bill", 30.0, 1742256000000L, null),
    Expense(5, "Movie", 12.0, 1742169600000L, null),
    Expense(6, "Snacks", 8.50, 1742169600000L, null)
)

val colorsForPicker = listOf(
    Color(0xFF4CAF50),  // Green
    Color(0xFF2196F3),  // Blue
    Color(0xFFFF5722),  // Deep Orange
    Color(0xFF9C27B0),  // Purple
    Color(0xFFFF9800),  // Orange
    Color(0xFF00BCD4),  // Cyan
    Color(0xFFE91E63),  // Pink
    Color(0xFF607D8B),  // Blue Grey
)

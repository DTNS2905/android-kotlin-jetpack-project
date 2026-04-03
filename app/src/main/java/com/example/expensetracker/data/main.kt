package com.example.expensetracker.data

import androidx.compose.ui.graphics.Color
import com.example.expensetracker.room.model.Expense

val exampleExpenseList = listOf(
    Expense(1, "Groceries", 45.50, 1742428800000L),
    Expense(2, "Coffee", 3.25, 1742428800000L),
    Expense(3, "Transport", 15.00, 1742342400000L),
    Expense(4, "Internet Bill", 30.0, 1742256000000L),
    Expense(5, "Movie", 12.0, 1742169600000L),
    Expense(6, "Snacks", 8.50, 1742169600000L)
)

val colorsForPicker = listOf(
    Color.Blue,
    Color.Red,
    Color.Green,
    Color.Cyan,
    Color.DarkGray,
    Color.Magenta
)
package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.room.database.DatabaseProvider
import com.example.expensetracker.room.repository.ExpenseRepository
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.ui.screens.HomeScreen
import com.example.expensetracker.ui.viewmodel.ExpenseViewModel
import com.example.expensetracker.ui.viewmodel.ExpenseViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }
    val repo = remember { ExpenseRepository(db.expenseDao()) }

    val expenseViewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModelFactory(repo)
    )

    HomeScreen(expenseViewModel)
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    ExpenseTrackerTheme {
        App()
    }
}
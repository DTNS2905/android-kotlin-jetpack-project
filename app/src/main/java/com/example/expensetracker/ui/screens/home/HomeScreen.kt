package com.example.expensetracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.AnimatedFloatingCard
import com.example.expensetracker.ui.components.CustomList
import com.example.expensetracker.ui.components.ImageProfile
import com.example.expensetracker.ui.components.MessageType
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.ui.screens.home.components.AddExpenseButton
import com.example.expensetracker.ui.screens.home.components.AddExpenseDialog
import com.example.expensetracker.ui.screens.home.components.FilterBottomSheet
import com.example.expensetracker.ui.screens.home.components.TotalCard
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.example.expensetracker.viewmodel.UiEvent

@Composable
fun HomeScreen(
    expenseViewModel: ExpenseViewModel,
    navController: NavHostController
) {
    val expenses by expenseViewModel.getAllFilteredExpenses.collectAsState()
    val total by expenseViewModel.totalAmount.collectAsState()
    val selectedFilters by expenseViewModel.selectedFilter.collectAsState()
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf(MessageType.INFO) }

    LaunchedEffect(Unit) {
        expenseViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowMessage -> {
                    messageText = event.message
                    messageType = event.type
                    showMessage = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Welcome back,",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Sang",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                ImageProfile(imageRes = R.drawable.sang, modifier = Modifier)
            }

            TotalCard(total)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT EXPENSES",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BadgedBox(
                            badge = {
                                if (selectedFilters.time != TimeFilter.ALL) Badge()
                            }
                        ) {
                            IconButton(onClick = { showFilter = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filter")
                            }
                        }
                        AddExpenseButton(showSheet = setShowDialog, modifier = Modifier)
                    }
                }

                CustomList(
                    expenses,
                    modifier = Modifier.weight(1f),
                    onClick = { id -> navController.navigate("expense/$id") }
                )
            }
        }

        AnimatedFloatingCard(
            message = messageText,
            type = messageType,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            show = showMessage,
            onDismiss = { showMessage = false }
        )
    }

    if (showFilter) {
        FilterBottomSheet(
            selectedFilters = selectedFilters,
            onFilterUpdate = { expenseViewModel.setFilter { _ -> it } },
            onDismiss = { showFilter = false }
        )
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            )
        ) {
            AddExpenseDialog(
                showDialog = { setShowDialog(false) },
                onAdd = { title, amount ->
                    expenseViewModel.addExpense(title, amount)
                }
            )
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    AddExpenseDialog({})
}
package com.example.expensetracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.components.MenuBar

@Composable
fun GeneralLayout(
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {},
        bottomBar = { MenuBar() },
        floatingActionButton = {},

    ) { paddingValues -> Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            content()
        }
    }
}
package com.example.expensetracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.components.MenuBar

@Composable
fun GeneralLayout(
    contentSpacing: Dp = 20.dp,
    fabAction: (Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = { MenuBar() },
        containerColor = MaterialTheme.colorScheme.background

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(contentSpacing)

        ) {
            content()
        }
    }
}
package com.example.expensetracker.ui.layouts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.expensetracker.Routes
import com.example.expensetracker.ui.components.MenuBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralLayout(
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    val topBarConfig = Routes.topBarRoutes.entries
        .firstOrNull { (prefix, _) -> currentRoute.startsWith(prefix) }
        ?.value

    Scaffold(
        topBar = {
            topBarConfig?.let { config ->
                TopAppBar(
                    title = {
                        Text(
                            text = config.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        if (config.showBack) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            if (currentRoute in Routes.bottomBarRoutes) {
                MenuBar(navController, currentRoute)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        content(paddingValues)
    }
}

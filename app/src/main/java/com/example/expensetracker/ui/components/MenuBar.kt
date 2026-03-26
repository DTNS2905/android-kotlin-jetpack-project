package com.example.expensetracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

data class MenuItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

object Routes {
    const val HOME = "home"
    const val PROFILE = "profile"
}
@Composable
fun MenuBar(
    navController: NavHostController,
    currentRoute: String
) {

    val items = listOf(
        MenuItem(Routes.HOME, "home", Icons.Filled.Home),
        MenuItem(Routes.PROFILE, "profile",Icons.Filled.Person),
    )
    val (indexedItem, setIndexedItem) = remember { mutableIntStateOf(0) }
    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Routes.HOME)
                        launchSingleTop = true
                    }
                },
            )
        }

    }
}
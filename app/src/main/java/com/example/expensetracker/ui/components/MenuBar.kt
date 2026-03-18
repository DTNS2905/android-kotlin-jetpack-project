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

data class MenuItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MenuBar() {
    val items = listOf(
        MenuItem("Home", Icons.Filled.Home),
        MenuItem("Profile", Icons.Filled.Person),
    )
    val (indexedItem, setIndexedItem) = remember { mutableIntStateOf(0) }
    NavigationBar() {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = indexedItem == index,
                onClick = {setIndexedItem(index)},
            )
        }

    }
}
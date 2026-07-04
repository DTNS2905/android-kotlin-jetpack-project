package com.example.expensetracker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryIcon(val key: String, val label: String, val vector: ImageVector)

val categoryIconList = listOf(
    CategoryIcon("receipt",        "Receipt",     Icons.Rounded.Receipt),
    CategoryIcon("restaurant",     "Food",        Icons.Rounded.Restaurant),
    CategoryIcon("local_cafe",     "Cafe",        Icons.Rounded.LocalCafe),
    CategoryIcon("local_grocery",  "Groceries",   Icons.Rounded.LocalGroceryStore),
    CategoryIcon("shopping_cart",  "Shopping",    Icons.Rounded.ShoppingCart),
    CategoryIcon("directions_car", "Transport",   Icons.Rounded.DirectionsCar),
    CategoryIcon("train",          "Transit",     Icons.Rounded.Train),
    CategoryIcon("flight",         "Travel",      Icons.Rounded.Flight),
    CategoryIcon("local_hospital", "Health",      Icons.Rounded.LocalHospital),
    CategoryIcon("fitness_center", "Gym",         Icons.Rounded.FitnessCenter),
    CategoryIcon("school",         "Education",   Icons.Rounded.School),
    CategoryIcon("work",           "Work",        Icons.Rounded.Work),
    CategoryIcon("home",           "Home",        Icons.Rounded.Home),
    CategoryIcon("movie",          "Entertainment", Icons.Rounded.Movie),
    CategoryIcon("sports_esports", "Gaming",      Icons.Rounded.SportsEsports),
    CategoryIcon("pets",           "Pets",        Icons.Rounded.Pets),
    CategoryIcon("phone",          "Phone",       Icons.Rounded.Phone),
    CategoryIcon("attach_money",   "Finance",     Icons.Rounded.AttachMoney),
    CategoryIcon("wallet",         "Bills",       Icons.Rounded.Wallet),
)

fun iconVectorFor(key: String): ImageVector =
    categoryIconList.find { it.key == key }?.vector ?: Icons.Rounded.Receipt

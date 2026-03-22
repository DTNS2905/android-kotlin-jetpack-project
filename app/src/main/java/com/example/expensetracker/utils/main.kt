package com.example.expensetracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDollar(num: Double): String {
    return "$" + "%, .2f".format(num)
}

fun formatDate(timeStamp: Long): String {
    return SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(timeStamp))
}
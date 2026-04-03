package com.example.expensetracker.utils

import com.example.expensetracker.constants.TimeFilter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDollar(num: Double): String {
    return "$" + "%, .2f".format(num)
}

fun formatDate(timeStamp: Long): String {
    return SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(timeStamp))
}

fun TimeFilter.toTimeRange(): Pair<Long, Long> {
    val now = System.currentTimeMillis()
    if (this == TimeFilter.ALL) return Pair(0, now)
    val cal = Calendar.getInstance().apply {
        when(this@toTimeRange) {
            TimeFilter.TODAY -> Unit
            TimeFilter.THIS_WEEK -> set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
            TimeFilter.THIS_MONTH -> set(Calendar.DAY_OF_MONTH, 1)
            TimeFilter.THIS_YEAR -> set(Calendar.DAY_OF_YEAR, 1)
        }
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return Pair(cal.timeInMillis, now)
}

fun TimeFilter.toLabel() = when (this) {
    TimeFilter.ALL -> "All"
    TimeFilter.TODAY -> "Today"
    TimeFilter.THIS_WEEK -> "This Week"
    TimeFilter.THIS_MONTH -> "This Month"
    TimeFilter.THIS_YEAR -> "This Year"
}
package com.example.expensetracker.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.expensetracker.constants.TimeFilter
import com.example.expensetracker.viewmodel.YearMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDollar(num: Double, symbol: String = "$"): String {
    return "$symbol%,.2f".format(num)
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

fun YearMonth.label(): String {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    return "$year ${months[month - 1]}"
}

fun YearMonth.next(): YearMonth {
    val now = Calendar.getInstance()
    val current = YearMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1)
    if (this === current) return this
    return if(month == 12) YearMonth(year + 1, 1) else YearMonth(year, month + 1)
}

fun YearMonth.previous(): YearMonth {
    return if(month == 1) YearMonth(year - 1, 12) else YearMonth(year, month - 1)
}

fun YearMonth.toRange(): Pair<Long, Long> {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1, 0, 0, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val from = cal.timeInMillis
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    val to = cal.timeInMillis
    return from to to
}

fun YearMonth.daysInMonth(): Int {
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
}

object ImageUtils {
    suspend fun loadBitmap(imagePath: String?): ImageBitmap? {
        return if (imagePath != null) {
            withContext(Dispatchers.IO) {
                try { BitmapFactory.decodeFile(imagePath)?.asImageBitmap() }
                catch (e: Exception) { null }
            }
        } else null
    }

    fun copyToInternalStorage(context: Context, uri: Uri): String? {
        val temp = File(context.filesDir, "profile_image_temp.jpg")
        val final = File(context.filesDir, "profile_image.jpg")
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            input.use { it.copyTo(temp.outputStream()) }
            temp.renameTo(final)
            final.absolutePath
        } catch (e: Exception) {
            temp.delete()
            null
        }
    }
}

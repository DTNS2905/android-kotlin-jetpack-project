package com.example.expensetracker.ui.screens.statistic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.room.model.Category
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.utils.iconVectorFor
import com.example.expensetracker.utils.formatDollar
import com.example.expensetracker.utils.label
import com.example.expensetracker.viewmodel.CategoryStat
import com.example.expensetracker.viewmodel.DailyStat
import com.example.expensetracker.viewmodel.StatisticState
import com.example.expensetracker.viewmodel.StatisticViewModel
import com.example.expensetracker.viewmodel.YearMonth
import java.util.Calendar

@Composable
fun StatisticScreen(viewModel: StatisticViewModel) {
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val statisticState by viewModel.statisticState.collectAsState()
    StatisticContent(
        state = statisticState,
        selectedMonth = selectedMonth,
        actions = StatisticUiActions(
            previousMonth = viewModel::previousMonth,
            nextMonth = viewModel::nextMonth,
            onSelectMonth = viewModel::selectMonth
        )
    )
}

data class StatisticUiActions(
    val previousMonth: () -> Unit = {},
    val nextMonth: () -> Unit = {},
    val onSelectMonth: (YearMonth) -> Unit = {}
)

@Composable
fun StatisticContent(
    state: StatisticState,
    selectedMonth: YearMonth,
    actions: StatisticUiActions
) {
    var showMonthPicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Statistics", style = MaterialTheme.typography.headlineMedium)

        MonthSelector(
            label = selectedMonth.label(),
            onPrevious = actions.previousMonth,
            onNext = actions.nextMonth,
            onLabelClick = { showMonthPicker = true }
        )

        if (state.transactionCount == 0) {
            EmptyState()
        } else {
            if (state.categoryStats.isNotEmpty()) {
                DonutChartCard(
                    categoryStats = state.categoryStats,
                    totalSpent = state.totalSpent,
                    currencySymbol = state.currencySymbol,
                    selectedMonth = selectedMonth
                )
            }

            if (state.dailyStats.any { it.amount > 0 }) {
                DailyChartCard(
                    dailyStats = state.dailyStats,
                    totalSpent = state.totalSpent,
                    avgPerDay = state.avgPerDay,
                    currencySymbol = state.currencySymbol,
                    selectedMonth = selectedMonth
                )
            }

            state.categoryStats.firstOrNull()?.let { top ->
                TopCategoryCard(top, state.currencySymbol)
            }
        }

        Spacer(Modifier)
    }

    if (showMonthPicker) {
        MonthPickerDialog(
            current = selectedMonth,
            onDismiss = { showMonthPicker = false },
            onConfirm = { ym ->
                actions.onSelectMonth(ym)
                showMonthPicker = false
            }
        )
    }
}

@Composable
private fun MonthSelector(
    label: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onLabelClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous month")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onLabelClick() }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Pick month",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onNext) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next month")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonthPickerDialog(
    current: YearMonth,
    onDismiss: () -> Unit,
    onConfirm: (YearMonth) -> Unit
) {
    val now = Calendar.getInstance()
    val currentYear = now.get(Calendar.YEAR)
    val currentMonth = now.get(Calendar.MONTH) + 1

    var year by remember { mutableIntStateOf(current.year) }
    var selectedMonth by remember { mutableIntStateOf(current.month) }

    val monthLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                             "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Month") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Year row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { year-- }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous year")
                    }
                    Text(
                        year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { year++ },
                        enabled = year < currentYear
                    ) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Next year")
                    }
                }

                // Month grid: 3 rows × 4 columns
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    (0 until 3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            (1..4).forEach { col ->
                                val month = row * 4 + col
                                val isFuture = year == currentYear && month > currentMonth
                                FilterChip(
                                    modifier = Modifier.weight(1f),
                                    selected = month == selectedMonth,
                                    enabled = !isFuture,
                                    onClick = { selectedMonth = month },
                                    label = {
                                        Text(
                                            monthLabels[month - 1],
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(YearMonth(year, selectedMonth)) }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun DonutChartCard(
    categoryStats: List<CategoryStat>,
    totalSpent: Double,
    currencySymbol: String,
    selectedMonth: YearMonth
) {
    val segmentColors = categoryStats.map { stat ->
        stat.category?.let { Color(it.color.toInt()) } ?: MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text("By category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(selectedMonth.label(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 34.dp.toPx()
                        val inset = strokeWidth / 2
                        val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                        val topLeft = Offset(inset, inset)
                        var startAngle = -90f
                        val gap = 3f

                        categoryStats.forEachIndexed { index, stat ->
                            val sweep = (stat.percentage * 360.0).toFloat()
                            val actualSweep = (sweep - gap).coerceAtLeast(0.5f)
                            drawArc(
                                color = segmentColors[index],
                                startAngle = startAngle,
                                sweepAngle = actualSweep,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                                topLeft = topLeft,
                                size = arcSize
                            )
                            startAngle += sweep
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(formatDollar(totalSpent, currencySymbol), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    categoryStats.take(5).forEachIndexed { index, stat ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(segmentColors[index]))
                                Text(stat.category?.title ?: "Other", style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                "${(stat.percentage * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyChartCard(
    dailyStats: List<DailyStat>,
    totalSpent: Double,
    avgPerDay: Double,
    currencySymbol: String,
    selectedMonth: YearMonth
) {
    val now = Calendar.getInstance()
    val todayDay = now.get(Calendar.DAY_OF_MONTH)
    val isCurrentMonth = selectedMonth.year == now.get(Calendar.YEAR) &&
            selectedMonth.month == now.get(Calendar.MONTH) + 1

    val primaryColor = MaterialTheme.colorScheme.primary
    val barTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    val barActiveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("Daily spending", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(selectedMonth.label(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(formatDollar(totalSpent, currencySymbol), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = primaryColor)
                    Text("avg ${formatDollar(avgPerDay, currencySymbol)}/day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                val maxAmount = dailyStats.maxOfOrNull { it.amount }?.takeIf { it > 0 } ?: 1.0
                val unitWidth = size.width / dailyStats.size
                val barWidth = unitWidth * 0.65f
                val barOffset = (unitWidth - barWidth) / 2

                dailyStats.forEachIndexed { index, stat ->
                    val barHeight = (stat.amount / maxAmount * size.height).toFloat()
                    val x = index * unitWidth + barOffset
                    val isToday = isCurrentMonth && stat.day == todayDay
                    val cr = CornerRadius(barWidth / 2, barWidth / 2)

                    drawRoundRect(color = barTrackColor, topLeft = Offset(x, 0f), size = Size(barWidth, size.height), cornerRadius = cr)
                    if (barHeight > 0f) {
                        drawRoundRect(
                            color = if (isToday) primaryColor else barActiveColor,
                            topLeft = Offset(x, size.height - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = cr
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                dailyStats.forEachIndexed { index, stat ->
                    if (index == 0 || stat.day % 5 == 0) {
                        val isToday = isCurrentMonth && stat.day == todayDay
                        Text(
                            stat.day.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Spacer(Modifier.width(0.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopCategoryCard(top: CategoryStat, currencySymbol: String) {
    val color = top.category?.let { Color(it.color.toInt()) } ?: MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVectorFor(top.category?.icon ?: "receipt"),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text("TOP CATEGORY", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        top.category?.title ?: "Uncategorized",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${formatDollar(top.amount, currencySymbol)} · ${top.count} transaction${if (top.count == 1) "" else "s"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                "${(top.percentage * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
        Text("No expenses this month", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticContentPreview() {
    ExpenseTrackerTheme {
        StatisticContent(
            selectedMonth = YearMonth(2025, 4),
            state = StatisticState(
                totalSpent = 1771.0,
                transactionCount = 18,
                avgPerDay = 59.03,
                currencySymbol = "$",
                categoryStats = listOf(
                    CategoryStat(Category(1, "Shopping", 0xFFFF6B35), 885.5, 0.50),
                    CategoryStat(Category(2, "Food", 0xFF4CAF50), 495.88, 0.28),
                    CategoryStat(Category(3, "Health", 0xFFE91E63), 230.23, 0.13),
                    CategoryStat(Category(4, "Transport", 0xFF2196F3), 141.68, 0.08),
                    CategoryStat(null, 17.71, 0.01)
                ),
                dailyStats = (1..30).map { day ->
                    DailyStat(day, when {
                        day % 7 == 0 -> day * 8.5
                        day % 3 == 0 -> day * 3.7
                        day % 5 == 0 -> day * 2.1
                        else -> 0.0
                    })
                }
            ),
            actions = StatisticUiActions()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MonthPickerDialogPreview() {
    ExpenseTrackerTheme {
        MonthPickerDialog(
            current = YearMonth(2025, 4),
            onDismiss = {},
            onConfirm = {}
        )
    }
}
package com.example.expensetracker.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.ErrorRed
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.ui.theme.WarningAmber
import com.example.expensetracker.utils.formatDollar
import com.example.expensetracker.viewmodel.BudgetState

@Composable
fun BudgetCard(
    budgetState: BudgetState,
    currencySymbol: String
) {
    val barColor = when {
        budgetState.isOverBudget -> ErrorRed
        budgetState.isNearLimit  -> WarningAmber
        else                     -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Monthly Budget",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = when {
                        budgetState.isOverBudget -> "Over budget!"
                        budgetState.isNearLimit  -> "Near limit"
                        else                     -> "${(budgetState.progress * 100).toInt()}%"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = barColor
                )
            }

            LinearProgressIndicator(
                progress = { budgetState.progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = barColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatDollar(budgetState.spent, currencySymbol),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "of ${formatDollar(budgetState.budget, currencySymbol)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetCardPreview() {
    ExpenseTrackerTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BudgetCard(
                BudgetState(
                    budget = 2000.0,
                    spent = 1200.0,
                    alertThreshold = 80),
                "$"
            )
            BudgetCard(
                BudgetState(
                    budget = 2000.0,
                    spent = 1700.0,
                    alertThreshold = 80),
                "$")
            BudgetCard(
                BudgetState(
                    budget = 2000.0,
                    spent = 2300.0,
                    alertThreshold = 80),
                "$")
        }
    }
}

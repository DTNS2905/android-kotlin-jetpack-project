package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChip(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    FilterChip(
        selected = selected,
        onClick = { onClick?.invoke() },
        label = { Text(name) },
        modifier = modifier,
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
        },
        trailingIcon = trailingIcon,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = color.copy(alpha = 0.12f),
            labelColor = color,
            selectedContainerColor = color.copy(alpha = 0.25f),
            selectedLabelColor = color
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = color.copy(alpha = 0.3f),
            selectedBorderColor = color
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryChipPreview() {
    CategoryChip(name = "Food", color = Color(0xFF4CAF50))
}

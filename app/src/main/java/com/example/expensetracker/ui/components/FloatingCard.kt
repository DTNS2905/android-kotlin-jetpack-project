package com.example.expensetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensetracker.ui.theme.InfoBlue
import com.example.expensetracker.ui.theme.SuccessGreen
import com.example.expensetracker.ui.theme.WarningAmber
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class MessageType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

@Composable
fun FloatingMessageCard(
    modifier: Modifier = Modifier,
    message: String,
    type: MessageType = MessageType.INFO
) {
    val (icon, tint) = when (type) {
        MessageType.SUCCESS -> Icons.Default.CheckCircle to SuccessGreen
        MessageType.ERROR -> Icons.Default.Error to MaterialTheme.colorScheme.error
        MessageType.WARNING -> Icons.Default.Warning to WarningAmber
        MessageType.INFO -> Icons.Default.Info to InfoBlue
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun AnimatedFloatingCard(
    modifier: Modifier = Modifier,
    message: String,
    type: MessageType = MessageType.INFO,
    show: Boolean,
    onDismiss: () -> Unit,
    durationMs: Long = 3000L
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        if (show) {
            visible = true
            delay(durationMs)
            visible = false
            delay(300)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn() + slideInVertically { -it },
        exit = fadeOut() + slideOutVertically { -it }
    ) {
        FloatingMessageCard(message = message, type = type)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFloatingCard() {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingMessageCard(
            message = "Expense added successfully",
            type = MessageType.SUCCESS,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

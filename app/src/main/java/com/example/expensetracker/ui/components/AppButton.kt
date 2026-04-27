package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

enum class ButtonVariant {
    PRIMARY,    // filled — confirm, save, add
    SECONDARY,  // tonal — cancel, back
    DANGER,     // outlined error — delete, remove
    GHOST       // text only — subtle action
}

@Composable
fun AppButton(
    text: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val colors = when (variant) {
        ButtonVariant.PRIMARY -> ButtonDefaults.buttonColors()
        ButtonVariant.SECONDARY -> ButtonDefaults.filledTonalButtonColors()
        ButtonVariant.DANGER -> ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        )
        ButtonVariant.GHOST -> ButtonDefaults.textButtonColors()
    }

    val border = if (variant == ButtonVariant.DANGER)
        ButtonDefaults.outlinedButtonBorder(enabled = enabled).copy(
            brush = SolidColor(MaterialTheme.colorScheme.error)
        )
    else null

    val elevation = if (variant == ButtonVariant.GHOST || variant == ButtonVariant.DANGER)
        ButtonDefaults.buttonElevation(0.dp)
    else ButtonDefaults.buttonElevation()

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        border = border,
        elevation = elevation
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun AppButtonPreview() {
    ExpenseTrackerTheme {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            modifier = Modifier.then(Modifier)
        ) {
            AppButton(text = "Confirm", onClick = {}, variant = ButtonVariant.PRIMARY)
            AppButton(text = "Cancel", onClick = {}, variant = ButtonVariant.SECONDARY)
            AppButton(text = "Delete", onClick = {}, variant = ButtonVariant.DANGER)
            AppButton(text = "Skip", onClick = {}, variant = ButtonVariant.GHOST)
        }
    }
}
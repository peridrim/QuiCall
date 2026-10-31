package com.example.quicall.screens.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun CombinedActionDialButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondDigit: String = "",
    shape: Shape = ButtonDefaults.shape,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Surface(
        modifier = modifier,
        shape = shape,
        color = MaterialTheme.colorScheme.primary,
        ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = {
                        haptic.performHapticFeedback(
                            hapticFeedbackType = HapticFeedbackType.LongPress
                        )
                        onLongClick()
                    },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
            Text(secondDigit)
        }
    }
}


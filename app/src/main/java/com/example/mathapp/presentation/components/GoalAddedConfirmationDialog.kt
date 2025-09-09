package com.example.mathapp.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun GoalAddedConfirmationDialog(
    dialog: String,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animation: icon blooms outward and stops in 1 second
            val scale = remember { Animatable(0.1f) }
            LaunchedEffect(Unit) {
                scale.animateTo(
                    targetValue = 0.8f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                )
            }
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color(0xFF4CAF50), // Green
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(dialog)
        }
    }
}

package com.example.mathapp.ui.effects

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun GoalItemShimmer(
) {
    val shimmerColors = listOf(
        Color(0xFF2A2A2A).copy(alpha = 0.6f),
        Color(0xFF3A3A3A).copy(alpha = 0.9f),
        Color(0xFF2A2A2A).copy(alpha = 0.6f)
    )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 200f, 200f)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .background(
                    color = Color(0xFF0D0B1E),
                    shape = RoundedCornerShape(12.dp)
                ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 50.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xDFFF5722)
            )
        ) {
            // Title placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(28.dp)
                    .padding(vertical = 3.dp, horizontal = 5.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Description placeholder
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )

            Spacer(Modifier.width(8.dp))

            // EndBy placeholder
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(16.dp)
                    .background(brush, RoundedCornerShape(6.dp))
            )
        }
    }
}

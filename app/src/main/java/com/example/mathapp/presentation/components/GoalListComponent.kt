package com.example.mathapp.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.ui.theme.GoalCardColor


@Composable
fun GoalListComponent(
    modifier: Modifier = Modifier,
    goals: List<GoalModel>,
    listState: LazyListState,
    onClick: ((String) -> Unit)? = null
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            state = listState
        ) {
            items(items = goals) { goal -> // if I give key here, the scroll position changes when sorted, so it lags a little when scrolled after sorting
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    color = Color(0xFF0D0B1E)
                )
                GoalItem(
                    goalTitle = goal.title,
                    goalDescription = goal.description,
                    endBy = goal.endBy,
                    clickable = onClick != null,
                    onClick = { onClick?.invoke(goal.id) }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    color = Color(0xFF0D0B1E)
                )
            }
        }

        // Overlay scrollbar
        val totalItems = goals.size
        val firstVisible = listState.firstVisibleItemIndex
        val offset = listState.firstVisibleItemScrollOffset

        // Use viewport fraction instead of hard-coded dp
        val scrollProgress = remember(firstVisible, offset, totalItems) {
            if (totalItems == 0) 0f
            else {
                // Approximate fractional scroll progress
                val visibleItems = listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1)
                (firstVisible + offset / 1000f) / (totalItems - visibleItems).coerceAtLeast(1)
            }
        }

        val animatedProgress by animateFloatAsState(
            targetValue = scrollProgress.coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        )

        // Scrollbar track
        var trackHeightPx by remember { mutableStateOf(0) }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .width(8.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .onGloballyPositioned { coordinates ->
                    trackHeightPx = coordinates.size.height
                }
        ) {
            // Scrollbar thumb
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(2.dp)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (animatedProgress * (trackHeightPx - 80.dp.roundToPx())).toInt()
                        )
                    }
                    .height(80.dp)
                    .background(GoalCardColor, RoundedCornerShape(4.dp))
            )
        }
    }
}


@Composable
private fun GoalItem(
    goalTitle: String,
    goalDescription: String,
    endBy: String,
    clickable: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = onClick, enabled = clickable)
    ) {
        ElevatedCard(
            modifier = Modifier.background(
                color = GoalCardColor,
                shape = RoundedCornerShape(12.dp)
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 50.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent, // to make the background color visible
                contentColor = Color(0xDFFF5722)
            )
        ) {
            Text(
                text = goalTitle,
                modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = goalDescription,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF972EFD)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = endBy,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                color = Color.Red
            )
        }
    }
}
package com.example.mathapp.ui.goal.dashboard_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.ui.components.BlackBackGround
import com.example.mathapp.ui.components.GoalTopAppBarComp
import com.example.mathapp.ui.navigation.Routes.AddGoalScreen
import com.example.mathapp.ui.navigation.Routes.SpecificGoalScreen
import com.example.mathapp.ui.theme.GoalCardColor
import com.example.mathapp.utils.FAB_EXPLODE_BOUNDS_KEY


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DashBoardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    navHostController: NavHostController
) {

    val state = dashboardViewModel.goalState.collectAsStateWithLifecycle()
    val dashboardEvent by dashboardViewModel.dashboardEvent.collectAsState(DashboardEvent.Idle)
    val onEvent = dashboardViewModel::onEvent

    LaunchedEffect(dashboardEvent) {
        when(val event = dashboardEvent) { // To avoid casting using 'as'
            is DashboardEvent.NavigateToSpecificGoal -> {
                navHostController.navigate(SpecificGoalScreen(event.goalId))
            }
            DashboardEvent.NavigateBack -> {
                navHostController.popBackStack()
            }
            else -> Unit
        }
    }

    LaunchedEffect(state) {
        Log.d("Goal-Refresh", "DashBoardScreen: Refresh")
        onEvent(DashboardEvent.Refresh)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GoalTopAppBarComp(title = "Goals",
                onClick = {
                    onEvent(DashboardEvent.NavigateBack)
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(AddGoalScreen)
                },
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = FAB_EXPLODE_BOUNDS_KEY
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                containerColor = Color(0xFF0D0B1E)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item"
                )
            }
        }
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.goals != null -> {
                    if (state.value.goals != null) {
                        GoalList(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            goals = state.value.goals!!,
                            onClick = {
                                onEvent(DashboardEvent.NavigateToSpecificGoal(it))
                            }
                        )
                    }
                }

                state.value.isLoading -> {
                    LoadingList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                state.value.error != null -> {
                    val error = state.value.error
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (error != null) {
                            Text(error)

                            Button(
                                onClick = {
                                    onEvent(DashboardEvent.Refresh)
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun GoalList(
    modifier: Modifier = Modifier,
    goals: List<GoalModel>,
    onClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            state = listState
        ) {
            items(items = goals, key = { it.id }) { goal ->
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF0D0B1E)
                )
                GoalItem(
                    goalTitle = goal.title,
                    goalDescription = goal.description,
                    endBy = goal.endBy,
                    onClick = { onClick(goal.id) }
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
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
fun GoalItem(
    goalTitle: String,
    goalDescription: String,
    endBy: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = onClick)
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


@Composable
private fun LoadingList(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(11) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0D0B1E))
            GoalItemShimmer()
            HorizontalDivider(modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0D0B1E))
        }
    }
}




















package com.example.mathapp.ui.goal.specific_goal_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.domain.model.GoalModel
import com.example.mathapp.ui.components.BlackBackGround
import com.example.mathapp.ui.components.GoalTopAppBarComp
import com.example.mathapp.ui.theme.GoalCardColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificGoalScreen(
    specificGoalViewModel: SpecificGoalViewModel = hiltViewModel(),
    goalId: String,
    navHostController: NavHostController
) {
    val state = specificGoalViewModel.specificGoalState.collectAsStateWithLifecycle()
    val uiEventState by specificGoalViewModel.specificGoalEvent.collectAsStateWithLifecycle(
        SpecificGoalEvent.Idle
    )
    val onEvent = specificGoalViewModel::onEvent
    LaunchedEffect(Unit) {
        onEvent(SpecificGoalEvent.GetSpecificGoal(goalId = goalId))
    }

    LaunchedEffect(uiEventState) {
        when (uiEventState) {
            SpecificGoalEvent.NavigateBack -> {
                navHostController.popBackStack()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            GoalTopAppBarComp(
                title = state.value.goalModel?.title ?: "",
                onClick = {
                    onEvent(SpecificGoalEvent.NavigateBack)
                }
            )
        }
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.isLoading -> {
                    GoalDetailsLoading(
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
                        }
                    }
                }

                state.value.goalModel != null -> {
                    val goalModel = state.value.goalModel
                    if (goalModel != null) {
                        GoalDetails(
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            goalModel = goalModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalDetails(
    modifier: Modifier = Modifier,
    goalModel: GoalModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GoalCardColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 50.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent, // to make the background color visible
                contentColor = Color(0xFFF44336)
            )
        ) {
            Text(
                text = goalModel.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Start
            )
        }

        Spacer(Modifier.height(70.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Green)) {
                    append("Goal created: ")
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(goalModel.createdAt)
                }

            }
        )
        Spacer(Modifier.height(24.dp))

        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("Goal dead by: ")
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(goalModel.endBy)
                }
            }
        )
    }
}

@Composable
private fun GoalDetailsLoading(
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2E1A47),
            Color(0xFF0D0B1E),
            Color(0xFF1A1335)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Gradient Card Placeholder
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GoalCardColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 50.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
                contentColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(brush = gradientBrush)
            )
        }

        Spacer(Modifier.height(70.dp))

        // Goal created placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(20.dp)
                .background(brush = gradientBrush, shape = RoundedCornerShape(6.dp))
        )

        Spacer(Modifier.height(24.dp))

        // Goal endBy placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(20.dp)
                .background(brush = gradientBrush, shape = RoundedCornerShape(6.dp))
        )
    }
}
















































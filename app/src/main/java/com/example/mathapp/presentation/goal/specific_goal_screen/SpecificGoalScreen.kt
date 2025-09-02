package com.example.mathapp.presentation.goal.specific_goal_screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.mathapp.presentation.components.BlackBackGround
import com.example.mathapp.presentation.components.GoalTopAppBarComp
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
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            goalModel = goalModel,
                            isAlertBoxOpen = state.value.alertBoxState,
                            onMarkAsCompleteClick = {
                                onEvent(
                                    SpecificGoalEvent.MarkAsCompleteStateChange(
                                        true
                                    )
                                )
                            },
                            onConfirm = { onEvent(SpecificGoalEvent.OnCompletedConfirmClick(goalId)) },
                            onDismissRequest = {
                                onEvent(
                                    SpecificGoalEvent.MarkAsCompleteStateChange(
                                        false
                                    )
                                )
                            }
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
    goalModel: GoalModel,
    isAlertBoxOpen: Boolean,
    onMarkAsCompleteClick: () -> Unit,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
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

        Spacer(Modifier.height(56.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = { onMarkAsCompleteClick() },
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Green
                )
            ) {
                Text("I ACHIEVED THIS GOAL")
            }
        }
    }

    MarkAsCompletedAlertBox(
        isOpen = isAlertBoxOpen,
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirm,
        onDismiss = onDismissRequest
    )
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

@Composable
private fun MarkAsCompletedAlertBox(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text("REALLY?") },
            text = { Text("Are you sure you completed the goal and not just being lazy to complete it and mark as completed?") },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Magenta
                    )
                ) {
                    Text("Yes, I completed!")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("No, I will complete it for sure!")
                }
            },
            containerColor = GoalCardColor
        )
    }
}















































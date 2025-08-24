package com.example.mathapp.ui.goal.insert_goal_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.ui.components.TextFieldComponent
import com.example.mathapp.utils.SelectableDatesImpl
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ
import com.example.mathapp.utils.SupabaseTimeCast.toTimestampZ
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    modifier: Modifier,
    insertGoalViewModel: InsertGoalViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state by insertGoalViewModel.insertGoalState.collectAsStateWithLifecycle()
    val eventState by insertGoalViewModel.eventState.collectAsStateWithLifecycle(AddGoalScreenEvent.Idle)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = SelectableDatesImpl
    )
    val onEvent = insertGoalViewModel::onEvent

    LaunchedEffect(eventState) {
        when(eventState) {
            AddGoalScreenEvent.OnAddedSuccess -> {
                navHostController.popBackStack()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        DarkGradientBackground {
            AddGoalLayer(
                modifier = Modifier
                    .padding(innerPadding),
                title = state.title,
                saveEnabled = !(state.title.isEmpty() || state.description.isEmpty() || state.supabaseSavableDate.isEmpty()),
                description = state.description,
                date = if (state.supabaseSavableDate.isEmpty()) "Please pick a date" else state.supabaseSavableDate.formattedTimestampZ(),
                onTitleChange = {
                    onEvent(AddGoalScreenEvent.EnterTitle(it))
                },
                onDescriptionChange = {
                    onEvent(AddGoalScreenEvent.EnterDescription(it))
                },
                onAddGoalClick = { onEvent(AddGoalScreenEvent.SaveGoal) },
                onPickDateClick = { onEvent(AddGoalScreenEvent.OnPickDateButtonClick) },
                onCancelClick = { navHostController.popBackStack() }
            )
            CustomDatePicker(
                datePickerState = datePickerState,
                isOpenState = state.isDatePickerOpen,
                onDismissRequest = { onEvent(AddGoalScreenEvent.OnDatePickerDismissRequest) },
                onConfirm = {
                    onEvent(
                        AddGoalScreenEvent.OnSupabaseDateSelected(
                            date = datePickerState.selectedDateMillis.toTimestampZ()
                        )
                    )
                },
                onCancel = {
                    onEvent(AddGoalScreenEvent.OnDatePickerDismiss)
                }
            )

            if (state.isDialogOpen) {
                ConfirmationDialog(
                    dialog = state.goalAddMessage,
                    onDismissRequest = { onEvent(AddGoalScreenEvent.OnDialogDismissRequest) }
                )
            }
        }
    }
}

@Composable
private fun ConfirmationDialog(
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


@Composable
private fun AddGoalLayer(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    date: String,
    saveEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPickDateClick: () -> Unit,
    onAddGoalClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        TextFieldComponent(
            value = title,
            onValueChange = { onTitleChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            label = "Title",
            labelColor = Color.White.copy(0.6f),
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Black
        )

        TextFieldComponent(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            label = "Description",
            labelColor = Color.White.copy(0.6f),
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Magenta,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .width(200.dp)
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            )

            Button(
                onClick = onPickDateClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Magenta,
                    contentColor = Color.Black
                )
            ) {
                Text("Complete by?", fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onAddGoalClick,
                enabled = saveEnabled,
                modifier = Modifier
                    .width(120.dp)
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFFF4081)),
                        shape = RoundedCornerShape(12.dp)
                    )

            ) {
                Text("A  I  M", color = Color(0xFFFFD54F))
            }

            TextButton(
                onClick = onCancelClick,
                modifier = Modifier
                    .width(120.dp)
                    .border(
                        border = BorderStroke(1.dp, Color(0xFFFFD54F)),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text("C  A  N  C  E  L", color = Color(0xFFFF4081))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun CustomDatePicker(
    datePickerState: DatePickerState,
    isOpenState: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (isOpenState) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }
            },
            colors = DatePickerDefaults.colors(
                Color(0xFF0D0B1E)
            )
        ) {

            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxSize(),
                title = {
                    Text(
                        text = "Set your goal's deadline",
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFFF4081),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = DatePickerDefaults.colors(
                    Color(0xFF0D0B1E)
                )
            )

        }
    }


}


@Composable
private fun DarkGradientBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D0B1E), // Deep dark purple
                        Color(0xFF1A1335), // Mid purple
                        Color(0xFF2E1A47)  // Slightly lighter purple
                    )
                )
            )
    ) {
        content()
    }
}

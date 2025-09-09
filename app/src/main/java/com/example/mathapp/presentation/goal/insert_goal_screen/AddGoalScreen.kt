package com.example.mathapp.presentation.goal.insert_goal_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.presentation.components.AddGoalComponent
import com.example.mathapp.presentation.components.GoalAddedConfirmationDialog
import com.example.mathapp.presentation.components.GoalDatePicker
import com.example.mathapp.utils.SelectableDatesImpl
import com.example.mathapp.utils.SupabaseTimeCast.formattedTimestampZ
import com.example.mathapp.utils.SupabaseTimeCast.toTimestampZ

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
            AddGoalComponent(
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
            GoalDatePicker(
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
                GoalAddedConfirmationDialog(
                    dialog = state.goalAddMessage,
                    onDismissRequest = { onEvent(AddGoalScreenEvent.OnDialogDismissRequest) }
                )
            }
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

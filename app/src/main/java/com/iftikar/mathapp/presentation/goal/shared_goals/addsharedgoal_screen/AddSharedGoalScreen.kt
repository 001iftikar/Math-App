package com.iftikar.mathapp.presentation.goal.shared_goals.addsharedgoal_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iftikar.mathapp.presentation.components.AddGoalComponent
import com.iftikar.mathapp.presentation.components.DatePickerComponent
import com.iftikar.mathapp.presentation.components.GroupBackGroundComponent
import com.iftikar.mathapp.shared.SharedEvent
import com.iftikar.mathapp.shared.SharedViewModel
import com.iftikar.mathapp.utils.SelectableDatesImpl
import com.iftikar.mathapp.utils.SupabaseTimeCast.formattedTimestampZ
import com.iftikar.mathapp.utils.SupabaseTimeCast.toTimestampZ

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSharedGoalScreen(
    sharedViewModel: SharedViewModel,
    viewModel: AddSharedGoalViewModel,
    groupId: String,
    onCancel: () -> Unit,
    backOnSuccess: () -> Unit
) {
    val state by viewModel.addSharedGoalState.collectAsStateWithLifecycle()
    val eventState by viewModel.addGoalEventState.collectAsStateWithLifecycle(
        AddSharedGoalScreenEvent.Idle
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = SelectableDatesImpl
    )

    LaunchedEffect(eventState) {
        when (eventState) {
            AddSharedGoalScreenEvent.OnSuccess -> {
                sharedViewModel.sendEvent(SharedEvent.SharedGoalModify)
                backOnSuccess()
            }

            else -> Unit
        }
    }

    val onEvent = viewModel::onEvent
    Scaffold { innerPadding ->
        GroupBackGroundComponent()
        AddGoalComponent(
            modifier = Modifier.padding(innerPadding),
            title = state.title,
            description = state.description,
            date = if (state.date.isEmpty()) "Please pick a date" else state.date.formattedTimestampZ(),
            saveEnabled = state.title.isNotEmpty() && state.description.isNotEmpty() && state.date.isNotEmpty(),
            onTitleChange = { onEvent(AddSharedGoalScreenEvent.OnTitleChange(title = it)) },
            onDescriptionChange = { onEvent(AddSharedGoalScreenEvent.OnDescriptionChange(it)) },
            onPickDateClick = { onEvent(AddSharedGoalScreenEvent.DatePickerStateChange(true)) },
            onAddGoalClick = { onEvent(AddSharedGoalScreenEvent.OnCreateGoal(groupId)) },
            onCancelClick = onCancel
        )

        DatePickerComponent(
            title = "Set your goal's deadline",
            datePickerState = datePickerState,
            isOpenState = state.isDatePickerOpen,
            onDismissRequest = { onEvent(AddSharedGoalScreenEvent.DatePickerStateChange(false)) },
            onConfirm = { onEvent(AddSharedGoalScreenEvent.OnDateSelectConfirm(date = datePickerState.selectedDateMillis.toTimestampZ())) },
            onCancel = { onEvent(AddSharedGoalScreenEvent.DatePickerStateChange(false)) })
    }
}
package com.example.mathapp.presentation.studysmart.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mathapp.presentation.components.DeleteDialog
import com.example.mathapp.presentation.components.SubjectListBottomSheet
import com.example.mathapp.presentation.components.TaskCheckBox
import com.example.mathapp.presentation.components.TaskDatePicker
import com.example.mathapp.utils.Priority
import com.example.mathapp.utils.changeMillisToDateString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel,
    navController: NavController
) {
    val state by taskViewModel.state.collectAsStateWithLifecycle()
    val onEvent = taskViewModel::onEvent
    var deleteDialogOpenState by remember { mutableStateOf(false) }
    var isTaskDatePickerDialogOpen by remember { mutableStateOf(false) }
    val taskDatePickerState: DatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var relatedToSubject by remember { mutableStateOf("Select a subject") }

    val sheetState = rememberModalBottomSheetState()

    var isSheetOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        taskViewModel.taskEvent.collectLatest { event ->
            when (event) {
                TaskEvent.OnSuccess -> {
                    navController.popBackStack()
                }

                else -> Unit
            }
        }
    }

    TaskDatePicker(
        state = taskDatePickerState,
        isOpen = isTaskDatePickerDialogOpen,
        onDismissRequest = { isTaskDatePickerDialogOpen = false },
        onConfirmButtonClicked = {
            val selectedDate = taskDatePickerState.selectedDateMillis
            onEvent(TaskEvent.OnDateChange(selectedDate))
            isTaskDatePickerDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = deleteDialogOpenState,
        title = "Delete Task",
        bodyText = "Are you sure? This action is non reversible",
        onDismissRequest = { deleteDialogOpenState = false }
    ) {
        onEvent(TaskEvent.DeleteTask)
        deleteDialogOpenState = false
    }

    SubjectListBottomSheet(
        sheetState = sheetState,
        isOpen = isSheetOpen,
        subjects = state.subjects,
        onSubjectClicked = { subject ->
            relatedToSubject = subject.name
            onEvent(TaskEvent.OnRelatedSubjectSelect(subject))
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isSheetOpen = false
            }
        },
        onDismissButtonClick = { isSheetOpen = false }
    )

    Scaffold(
        topBar = {
            TaskScreenTopBar(
                isTaskExists = state.currentTaskId != null,
                isCompleted = state.isTaskComplete,
                checkBoxBorderColor = state.priority.color,
                onDeleteButtonClick = { deleteDialogOpenState = true },
                onBackButtonClick = { navController.popBackStack() },
                onCheckBoxClick = { onEvent(TaskEvent.OnIsCompleteChange) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(state = rememberScrollState())
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { onEvent(TaskEvent.OnTitleChange(it)) },
                label = { Text("Title") },
                singleLine = true
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { onEvent(TaskEvent.OnDescriptionChange(it)) },
                label = { Text("Description") },
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(
                    onClick = {
                        isTaskDatePickerDialogOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        label = priority.title,
                        backGroundColor = priority.color,
                        borderColor = if (priority == Priority.MEDIUM) {
                            Color.White
                        } else Color.Transparent,
                        labelColor = if (priority == Priority.MEDIUM) {
                            Color.White
                        } else Color.White.copy(alpha = 0.7f),
                        onClick = {
                            onEvent(TaskEvent.OnPriorityChange(priority))
                        }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Related to Subject",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.relatedToSubject ?: relatedToSubject,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(
                    onClick = {
                        isSheetOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
            Button(
                onClick = {
                    onEvent(TaskEvent.SaveTask)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExists: Boolean,
    isCompleted: Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick: () -> Unit = {},
    onDeleteButtonClick: () -> Unit,
    onCheckBoxClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        title = { Text("Tasks", style = MaterialTheme.typography.headlineSmall) },
        actions = {
            if (isTaskExists) {
                TaskCheckBox(
                    isComplete = isCompleted,
                    borderColor = checkBoxBorderColor
                ) {
                    onCheckBoxClick()
                }

                IconButton(
                    onClick = onDeleteButtonClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backGroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(backGroundColor)
            .clickable { onClick() }
            .padding(5.dp) //this for between background and border
            .border(1.dp, borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp), // this for between the border and the text
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = labelColor)
    }
}
























package com.example.mathapp.presentation.goal.shared_goals.creategroup_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.presentation.components.TextFieldComponent
import com.example.mathapp.shared.SharedEvent
import com.example.mathapp.shared.SharedViewModel
import com.example.mathapp.ui.theme.GroupColor1

@Composable
fun CreateGroupScreen(
    sharedViewModel: SharedViewModel,
    viewModel: CreateGroupViewModel,
    onCreateSuccess: () -> Unit
) {
    val state by viewModel.createGoalScreenState.collectAsStateWithLifecycle()
    val eventState by viewModel.createGroupEventState.collectAsStateWithLifecycle(
        CreateGroupScreenEvent.Idle
    )
    val onEvent = viewModel::onEvent
    LaunchedEffect(eventState) {
        when (eventState) {
            CreateGroupScreenEvent.OnSuccess -> {
                sharedViewModel.sendEvent(SharedEvent.GroupListModifyEvent)
                onCreateSuccess()
            }
            else -> Unit
        }

    }
    Scaffold { innerPadding ->
        GroupBackGroundComponent()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldComponent(
                value = state.title,
                onValueChange = { onEvent(CreateGroupScreenEvent.OnTitleChange(it)) },
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
                unfocusedIndicatorColor = GroupColor1
            )

            TextFieldComponent(
                value = state.description,
                onValueChange = { onEvent(CreateGroupScreenEvent.OnDescriptionChange(it)) },
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
                unfocusedIndicatorColor = GroupColor1
            )

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    onEvent(CreateGroupScreenEvent.OnCreateClick)
                },
                enabled = state.title.isNotEmpty()
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(color = GroupColor1)
                    }

                    else -> {
                        Text("Create group")
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            when {
                state.error != null -> {
                    Text(state.error!!)
                }

                else -> {}
            }
        }
    }
}
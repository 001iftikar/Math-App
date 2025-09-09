package com.example.mathapp.presentation.goal.shared_goals.joingroup_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.mathapp.shared.SharedViewModel
import com.example.mathapp.ui.theme.GoalCardColor
import com.example.mathapp.ui.theme.GroupColor1

@Composable
fun JoinGroupScreen(
    viewModel: JoinGroupViewModel,
    onSuccess: () -> Unit
) {
    val state by viewModel.joinGroupScreenState.collectAsStateWithLifecycle()
    val eventState by viewModel.joinGroupEvent.collectAsStateWithLifecycle(JoinGroupScreenEvent.Idle)
    val onEvent = viewModel::onEvent

    LaunchedEffect(eventState) {
        when (eventState) {
            JoinGroupScreenEvent.OnSuccess -> {
                onSuccess()
            }
            else -> Unit
        }
    }

    Scaffold { innerPadding ->
        GroupBackGroundComponent()
        JoinGroup(
            modifier = Modifier.padding(innerPadding),
            groupId = state.groupId,
            error = state.error,
            onValueChange = { onEvent(JoinGroupScreenEvent.OnGroupIdChange(it)) },
            onJoinButtonClick = { onEvent(JoinGroupScreenEvent.AlertDialogStateChange(true)) }
        )

        GroupJoinConfirmAlertBox(
            isOpen = state.isAlertBoxOpen,
            onConfirm = { onEvent(JoinGroupScreenEvent.OnConfirmClick) },
            onDismiss = { onEvent(JoinGroupScreenEvent.AlertDialogStateChange(false)) },
            onDismissRequest = { onEvent(JoinGroupScreenEvent.AlertDialogStateChange(false)) }
        )
    }

}

@Composable
private fun JoinGroup(
    modifier: Modifier = Modifier,
    groupId: String,
    error: String? = null,
    onValueChange: (String) -> Unit,
    onJoinButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter the group Id here to join a group")
        Spacer(Modifier.height(12.dp))
        TextFieldComponent(
            value = groupId,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            label = "group Id",
            labelColor = Color.White.copy(0.6f),
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.DarkGray.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            unfocusedIndicatorColor = GroupColor1
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onJoinButtonClick,
            enabled = groupId.isNotEmpty()
        ) {
            Text("Join")
        }

        Spacer(Modifier.height(12.dp))
        if (error != null) {
            Text(
                text = error,
                color = Color.Red
            )
        }
    }
}


@Composable
private fun GroupJoinConfirmAlertBox(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            text = { Text("Group members will be able to see your name and email.") },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GroupColor1,
                        contentColor = Color.White
                    )
                ) {
                    Text("I understand")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = GoalCardColor
        )
    }
}


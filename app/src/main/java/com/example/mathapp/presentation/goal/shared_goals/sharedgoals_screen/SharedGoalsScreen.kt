package com.example.mathapp.presentation.goal.shared_goals.sharedgoals_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mathapp.domain.model.SharedGoal
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.shared.SharedEvent
import com.example.mathapp.shared.SharedViewModel
import com.example.mathapp.ui.theme.GroupColor
import com.example.mathapp.ui.theme.GroupColor1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedGoalsScreen(
    sharedViewModel: SharedViewModel,
    viewModel: SharedGoalViewModel,
    groupInfoClick: (String) -> Unit,
    addGoal: (String) -> Unit,
    onChatClick: (String, String) -> Unit
) {
    val state by viewModel.sharedGoals.collectAsStateWithLifecycle()
    val sharedEventState by sharedViewModel.sharedEventState.collectAsStateWithLifecycle(SharedEvent.Idle)
    val onEvent = viewModel::onEvent

    LaunchedEffect(sharedEventState) {
        when (sharedEventState) {
            SharedEvent.SharedGoalModify -> onEvent(SharedGoalsScreenEvent.Refresh)
            else -> Unit
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(state.groupName) },
                actions = {
                    IconButton(
                        onClick = { groupInfoClick(state.groupId) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GroupColor
                )
            )
        },
        floatingActionButton = {
            if (!state.isLoading) {
                FloatingButtonSection(
                    onAddGoalClick = {
                        addGoal(it)
                    },
                    groupId = state.groupId,
                    isAdmin = state.isAdmin,
                    onChatClick = { onChatClick(state.groupId, state.groupName) }
                )
            }
        }
    ) { innerPadding ->
        GroupBackGroundComponent()
        when {
            state.isLoading -> {
                LoadingList(modifier = Modifier.padding(innerPadding))
            }

            state.error != null -> {
                Error(
                    modifier = Modifier.padding(innerPadding),
                    error = state.error!!
                )
            }

            state.goals != null -> {
                GoalsList(
                    modifier = Modifier.padding(innerPadding),
                    goals = state.goals!!,
                    onMarkAsCompleteClick = { isCompleted, sharedGoalId ->
                        viewModel.markAsComplete(isCompleted, sharedGoalId)
                    }
                )
            }
        }
    }
}

@Composable
private fun FloatingButtonSection(
    onAddGoalClick: (String) -> Unit,
    groupId: String,
    isAdmin: Boolean,
    onChatClick: () -> Unit
) {
    Row {
        if (isAdmin) {
            FloatingActionButton(
                onClick = { onAddGoalClick(groupId) },
                containerColor = GroupColor1
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }

        Spacer(Modifier.width(6.dp))

        FloatingActionButton(
            onClick = onChatClick,
            containerColor = GroupColor1
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }

}

@Composable
private fun GoalsList(
    modifier: Modifier,
    goals: List<SharedGoal>,
    onMarkAsCompleteClick: (Boolean, String) -> Unit
) {
    if (goals.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxWidth()
        ) {
            items(goals) { goal ->
                GoalItem(
                    modifier = Modifier.fillMaxWidth(),
                    sharedGoal = goal,
                    onMarkAsCompleteClick = {
                        onMarkAsCompleteClick(!goal.isCompleted, goal.id)
                    }
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This group currently has no goals",
                color = GroupColor1
            )
        }
    }
}

@Composable
private fun GoalItem(
    modifier: Modifier,
    sharedGoal: SharedGoal,
    onMarkAsCompleteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = GroupColor1.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sharedGoal.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                IconButton(
                    onClick = onMarkAsCompleteClick
                ) {
                    if (sharedGoal.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.ToggleOn,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ToggleOff,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }


            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sharedGoal.description,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Light)
                )

                Text(
                    text = sharedGoal.endBy,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Red
                )
            }
        }
    }
}


@Composable
private fun LoadingList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = GroupColor1
        )
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    error: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Red
        )
    }
}





















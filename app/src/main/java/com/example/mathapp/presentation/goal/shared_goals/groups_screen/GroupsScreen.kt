package com.example.mathapp.presentation.goal.shared_goals.groups_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mathapp.domain.model.Group
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.shared.SharedEvent
import com.example.mathapp.shared.SharedViewModel
import com.example.mathapp.ui.theme.GroupColor1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    sharedViewModel: SharedViewModel,
    viewModel: GroupViewModel,
    goToSharedGoalsScreen: (String, String) -> Unit,
    goToCreateGroupScreen: () -> Unit
) {
    val state by viewModel.groupsState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val sharedEvent by sharedViewModel.sharedEventState.collectAsStateWithLifecycle(SharedEvent.Idle)
    val expanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val onEvent = viewModel::onEvent

    LaunchedEffect(sharedEvent) {
        when(sharedEvent) {
            SharedEvent.GroupListModifyEvent -> {
                onEvent(GroupsScreenEvent.Refresh)
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                expanded = expanded,
                onClick = goToCreateGroupScreen
            )
        }
    ) { innerPadding ->
        GroupBackGroundComponent(modifier = Modifier.fillMaxSize())
        when {
            state.isLoading -> {
                LoadingList()
            }

            state.groups != null -> {
                Log.d("Shared-Screen", "GroupsScreen: ${state.groups}")
                if (state.groups!!.isNotEmpty()) {
                    GroupList(
                        modifier = Modifier.padding(innerPadding),
                        listState = listState,
                        groups = state.groups!!,
                        onClick = { id, name ->
                            goToSharedGoalsScreen(id, name) }
                    )
                } else {
                    EmptyList(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            state.error != null -> {
                Error(
                    modifier = Modifier.padding(innerPadding),
                    error = state.error!!
                )
            }
        }
    }
}


@Composable
private fun GroupItem(
    modifier: Modifier = Modifier,
    group: Group
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 10.dp)
    ) {
        Text(
            text = group.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = group.admin,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "12 goals",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun GroupList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    groups: List<Group>,
    onClick: (String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = listState
    ) {
        items(
            items = groups,
            key = { it.id }) { group ->
            GroupItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onClick(group.id, group.name) }),
                group = group
            )
        }
    }
}

@Composable
private fun FloatingActionButton(
    expanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        expanded = expanded,
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        },
        text = {
            Text("Add Group")
        },
        containerColor = GroupColor1
    )
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
private fun EmptyList(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You are not belonged to any group",
            style = MaterialTheme.typography.titleLarge,
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




















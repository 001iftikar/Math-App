package com.example.mathapp.presentation.goal.finished_goals_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.presentation.components.BasicAlertDialogComponent
import com.example.mathapp.presentation.components.BlackBackGround
import com.example.mathapp.presentation.components.GoalListComponent
import com.example.mathapp.presentation.components.GoalTopAppBarComp
import com.example.mathapp.presentation.components.LoadingListComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedGoalsScreen(
    viewModel: FinishedGoalsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state = viewModel.goalsState.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    var goalId by remember { mutableStateOf("") } // set the goal id later from the lambda

    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GoalTopAppBarComp(
                title = "Finished Goals",
                scrollBehavior = scrollBehavior,
                onClick = {
                    navHostController.popBackStack()
                })
        }
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.isLoading -> {
                    LoadingListComponent(modifier = Modifier.padding(innerPadding))
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

                            Button(
                                onClick = {
                                    onEvent(FinishedGoalsScreenEvent.Retry)
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                state.value.goals != null -> {
                    if (state.value.goals!!.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(innerPadding),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("You have not finished any goals yet")
                        }
                    }
                    GoalListComponent(
                        modifier = Modifier.padding(innerPadding),
                        goals = state.value.goals!!,
                        listState = listState,
                    ) {
                        IconButton(
                            onClick = {
                                onEvent(FinishedGoalsScreenEvent.OnDeleteButtonClick)
                                goalId = it // caught the goal id and then set the state by it
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null
                            )
                        }

                    }

                    BasicAlertDialogComponent(
                        text = "Once you delete the goal, \nthe action is non reversible",
                        state = state.value.alertDialogState,
                        onDismissRequest = {
                            onEvent(FinishedGoalsScreenEvent.OnDismissClick)
                        },
                        onConfirmClick = {
                            onEvent(FinishedGoalsScreenEvent.OnDeleteConfirmClick(goalId)) // pass the goal id finally here
                        }
                    )

                }
            }
        }
    }
}




















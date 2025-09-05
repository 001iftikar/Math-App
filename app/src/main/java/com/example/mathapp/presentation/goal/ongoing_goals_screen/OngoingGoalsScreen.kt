package com.example.mathapp.presentation.goal.ongoing_goals_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.presentation.components.BlackBackGround
import com.example.mathapp.presentation.components.GoalListComponent
import com.example.mathapp.presentation.components.GoalTopAppBarComp
import com.example.mathapp.presentation.components.LoadingListComponent
import com.example.mathapp.presentation.navigation.Routes.AddGoalScreen
import com.example.mathapp.presentation.navigation.Routes.SpecificGoalScreen
import com.example.mathapp.utils.FAB_EXPLODE_BOUNDS_KEY
import com.example.mathapp.utils.SupabaseTimeCast.toEpochMillisFromFormatted

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.OngoingGoalsScreen(
    ongoingGoalsViewModel: OngoingGoalsViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    navHostController: NavHostController
) {
    val state = ongoingGoalsViewModel.goalState.collectAsStateWithLifecycle()
    val ongoingGoalsScreenEvent by ongoingGoalsViewModel.dashboardEvent.collectAsState(OngoingGoalsScreenEvent.Idle)
    val onEvent = ongoingGoalsViewModel::onEvent
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listState = rememberLazyListState()

    LaunchedEffect(ongoingGoalsScreenEvent) {
        when(val event = ongoingGoalsScreenEvent) { // To avoid casting using 'as'
            is OngoingGoalsScreenEvent.NavigateToSpecificGoal -> {
                navHostController.navigate(SpecificGoalScreen(event.goalId))
            }
            OngoingGoalsScreenEvent.NavigateBack -> {
                navHostController.popBackStack()
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        Log.d("Goal-Refresh", "DashBoardScreen: Refresh")
        onEvent(OngoingGoalsScreenEvent.Refresh)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GoalTopAppBarComp(title = "Ongoing Goals",
                scrollBehavior = scrollBehavior,
                onClick = {
                    onEvent(OngoingGoalsScreenEvent.NavigateBack)
                },
                action = {
                    DropDown(
                        onClick = {
                            onEvent(OngoingGoalsScreenEvent.SortByEvent(sortBy = it))
                        }
                    )
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(AddGoalScreen)
                },
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = FAB_EXPLODE_BOUNDS_KEY
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                containerColor = Color(0xFF0D0B1E)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item"
                )
            }
        }
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.goals != null -> {
                    if (state.value.goals != null) {
                        val sortedList = when(state.value.sortBy) {
                            SortBy.NAMEASC -> {
                                state.value.goals!!.sortedBy {
                                    it.title.lowercase()
                                }
                            }
                            SortBy.NAMEDSC -> {
                                state.value.goals!!.sortedByDescending {
                                    it.title.lowercase()
                                }
                            }

                            SortBy.CREATEDAT -> {
                                state.value.goals!!.sortedBy {
                                    it.createdAt.toEpochMillisFromFormatted()
                                }
                            }
                            SortBy.ENDBY -> {
                                state.value.goals!!.sortedBy {
                                    it.endBy.toEpochMillisFromFormatted()
                                }
                            }
                        }
                        if (state.value.goals!!.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                                    .padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("You currently have no goals, add some")
                            }
                        }
                        GoalListComponent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            goals = sortedList,
                            listState = listState,
                            onClick = {
                                onEvent(OngoingGoalsScreenEvent.NavigateToSpecificGoal(it))
                            }
                        )
                    }
                }

                state.value.isLoading -> {
                    LoadingListComponent(
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

                            Button(
                                onClick = {
                                    onEvent(OngoingGoalsScreenEvent.Refresh)
                                }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDown(
    onClick: (SortBy) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.Sort,
            contentDescription = null,
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryNotEditable,
                enabled = true
            )
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .width(120.dp)
            ,
            expanded = isExpanded,
            onDismissRequest = {isExpanded = false}
        ) {
            SortBy.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.title)
                    },
                    onClick = {
                        onClick(it)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

























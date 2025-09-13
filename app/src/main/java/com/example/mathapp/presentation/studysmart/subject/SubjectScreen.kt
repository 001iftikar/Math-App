package com.example.mathapp.presentation.studysmart.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mathapp.presentation.components.AddSubjectDialog
import com.example.mathapp.presentation.components.CountCard
import com.example.mathapp.presentation.components.DeleteDialog
import com.example.mathapp.presentation.components.studySessionList
import com.example.mathapp.presentation.components.taskList
import com.example.mathapp.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    subjectViewModel: SubjectViewModel,
    navController: NavController
) {
    val subjectState by subjectViewModel.state.collectAsStateWithLifecycle()
    val onEvent = subjectViewModel::onEvent
    val listState = rememberLazyListState()
    val isFABExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(subjectState.studiedHours, subjectState.goalStudyHours) {
        onEvent(SubjectEvent.UpdateProgress)
    }


    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButton = {
            isAddSubjectDialogOpen = false
            onEvent(SubjectEvent.UpdateSubject)
        },
        selectedColors = subjectState.subjectCardColors,
        subjectName = subjectState.subjectName,
        goalHours = subjectState.goalStudyHours,
        onColorChange = { onEvent(SubjectEvent.OnSubjectCardColorChange(it)) },
        onSubjectNameChange = { onEvent(SubjectEvent.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onEvent(SubjectEvent.OnGoalStudyHoursChange(it)) }
    )

    DeleteDialog(
        isOpen = isDeleteSubjectDialogOpen,
        title = "Delete Subject?",
        bodyText = "Are you sure, you want to delete this subject? All related " +
                "tasks and study sessions will be permanently removed. This action can not be undone",
        onDismissRequest = {
            isDeleteSubjectDialogOpen = false
        },
        onConfirmButton = {
            onEvent(SubjectEvent.DeleteSubject)
            isDeleteSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure you don't want to keep track?",
        onDismissRequest = {
            isDeleteSessionDialogOpen = false
        },
        onConfirmButton = {
            onEvent(SubjectEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SubjectScreenTopAppBar(
                title = subjectState.subjectName,
                onBackButtonClick = { navController.popBackStack() },
                scrollBehaviour = scrollBehavior,
                onDeleteButtonClick = { isDeleteSubjectDialogOpen = true },
                onEditButtonClick = { isAddSubjectDialogOpen = true }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                expanded = isFABExpanded,
                onClick = {
                    navController.navigate(
                        Routes.TaskScreen(
                            null
                        )
                    )
                },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Task") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                SubjectOverviewSection(
                    studiedHours = subjectState.studiedHours.toString(),
                    goalHours = subjectState.goalStudyHours,
                    progress = subjectState.progress
                )
            }

            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = subjectState.upcomingTasks,
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = { navController.navigate(Routes.TaskScreen(it!!)) }
            )
            item {
                Spacer(Modifier.height(20.dp))
            }

            taskList(
                sectionTitle = "COMPLETED TASKS",
                tasks = subjectState.completedTasks,
                onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = { navController.navigate(Routes.TaskScreen(it!!)) }
            )

            item {
                Spacer(Modifier.height(20.dp))
            }

            studySessionList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "I see you haven't studied yet.\n START!!!",
                sessions = subjectState.recentSessions,
                onDeleteIconClick = {
                    isDeleteSubjectDialogOpen = true
                    onEvent(SubjectEvent.OnDeleteSessionButtonClick(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopAppBar(
    title: String,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
    scrollBehaviour: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehaviour,
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
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
        actions = {
            IconButton(
                onClick = onDeleteButtonClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete, contentDescription = null
                )
            }
            IconButton(
                onClick = onEditButtonClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit, contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    studiedHours: String,
    goalHours: String,
    progress: Float
) {
    val percentageProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0, 100)
        /*
        coerceIn {
        This is Kotlin's safety method:

    It makes sure the result stays between 0 and 100

    If it's less than 0, it becomes 0

    If it's more than 100, it becomes 100
        }
         */
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headLineText = "Goal Study Hour",
            count = goalHours
        )
        Spacer(Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headLineText = "Study Hours",
            count = studiedHours
        )
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")
        }
    }
}




































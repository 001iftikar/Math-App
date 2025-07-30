package com.example.mathapp.ui.studysmart.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mathapp.R
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.sessions
import com.example.mathapp.tasks
import com.example.mathapp.ui.components.AddSubjectDialog
import com.example.mathapp.ui.components.CountCard
import com.example.mathapp.ui.components.SubjectCard
import com.example.mathapp.ui.components.studySessionList
import com.example.mathapp.ui.components.taskList
import com.example.mathapp.ui.navigation.Routes

@Composable
fun StudySmartScreen(navHostController: NavHostController, dashBoardViewModel: DashBoardViewModel = hiltViewModel()) {
    val dashBoardState by dashBoardViewModel.state.collectAsState()
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    val onEvent = dashBoardViewModel::onEvent
    Scaffold(
        topBar = { TopBar { navHostController.popBackStack() } }
    ) { innerPadding ->

        AddSubjectDialog(
            isOpen = isAddSubjectDialogOpen,
            selectedColors = dashBoardState.subjectCardColors,
            subjectName = dashBoardState.subjectName,
            goalHours = dashBoardState.goalStudyHours,
            onColorChange = {onEvent(DashBoardEvent.onSubjectCardColorChange(it))},
            onSubjectNameChange = {onEvent(DashBoardEvent.onSubjectNameChange(it))},
            onGoalHoursChange = {onEvent(DashBoardEvent.onGoalStudyHoursChange(it))},
            onDismissRequest = {isAddSubjectDialogOpen = false}
        ) {
            onEvent(DashBoardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        }




        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                CountCardLayer(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    subjectCount = dashBoardState.totalSubjectCount,
                    studiedHours = dashBoardState.totalSubjectCount.toString(),
                    goalHours = dashBoardState.totalGoalStudyHours.toString()
                )
            }

            item {
                SubjectCardsLayer(
                    subjectList = dashBoardState.subjectList,
                    onAddIconClick = {isAddSubjectDialogOpen = true}
                ) {
                    if (it != null) {
                        navHostController.navigate(Routes.SubjectScreen(it))
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        navHostController.navigate(Routes.SessionScreen)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text("Start Study Session!")
                }
            }
            
            taskList(
                sectionTitle = "UPCOMING TASKS",
                tasks = tasks,
                onTaskCardClick = {},
                onCheckBoxClick = {onEvent(DashBoardEvent.onTaskIsCompleteChange(it))}
            )

            item {
                Spacer(Modifier.height(20.dp))
            }
            
            studySessionList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "So you haven't studied yet.\n START!!!",
                sessions = sessions,
                onDeleteIconClick = {onEvent(DashBoardEvent.onDeleteSessionButtonClick(it))}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text("Study Smart", style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun CountCardLayer(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        CountCard(
            modifier = Modifier.weight(1f),
            headLineText = "Subject Count",
            count = subjectCount.toString()
        )
        Spacer(Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headLineText = "Studied Hours",
            count = studiedHours
        )
        Spacer(Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headLineText = "Goal Study Hours",
            count = goalHours
        )
    }

}

@Composable
private fun SubjectCardsLayer(
    subjectList: List<Subject>,
    emptyListText: String = "You don't have any subjects yet. \n Add some!",
    onAddIconClick: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
            )
            IconButton(
                onClick = {
                    onAddIconClick()

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }

        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = null
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) {subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors.map {
                        Color(it)
                    }
                ) {
                    onSubjectCardClick(subject.subjectId)
                }
            }
        }
    }
}





















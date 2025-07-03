package com.example.mathapp.ui.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.navigation.Routes
import com.example.mathapp.utils.ResultState
import kotlinx.coroutines.delay

@Composable
fun TeacherScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val viewModel: TeacherViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val teachers = state.teachers

    Scaffold(
        topBar = {
            TopAppBarNavIcon(title = "Teachers", onClick = {
                navHostController.popBackStack()
            })
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator()
                    Spacer(Modifier.height(10.dp))
                    Text("Loading...")
                }
            }
            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error!!,
                        color = Color.Red
                    )
                    ElevatedButton(
                        onClick = {
                            viewModel.getAllTeachers()
                        }
                    ) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                LazyColumn(Modifier.padding(innerPadding)) {
                    items(teachers.sortedBy { it.teacherName }) {teacher ->
                        Teacher(
                            teacherName = teacher.teacherName,
                            profilePicture = teacher.profilePicture
                        ) {
                            navHostController.navigate(Routes.TeacherScreenByNameRoute(
                                name = teacher.teacherName
                            ))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Teacher(
    teacherName: String,
    profilePicture: String,
    onClick: () -> Unit
) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .clickable(
                    onClick = onClick
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SubcomposeAsyncImage(
                model = profilePicture,
                contentDescription = null,
                loading = {LoadingState()}
            )
            Spacer(Modifier.height(12.dp))
            Text(text = teacherName)
        }


}

@Composable
fun LoadingState() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(128.dp)
    )
}




























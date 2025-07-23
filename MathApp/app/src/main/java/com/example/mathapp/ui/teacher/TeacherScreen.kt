package com.example.mathapp.ui.teacher

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.ui.components.LinearLoader
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.effects.ImageAnimation
import com.example.mathapp.ui.navigation.Routes

@Composable
fun TeacherScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val viewModel: TeacherViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val teachers = state.teachers

    Scaffold(
        topBar = {
            TopAppBarNavIcon(title = "Teachers", navController = navHostController)
        }
    ) { innerPadding ->

        when {
            state.isLoading -> {
                LinearLoader(
                    loadingText = "Loading...",
                    padding = innerPadding
                )
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
                    items(teachers.sortedBy { it.teacherName }) { teacher ->
                        Teacher(
                            teacherName = teacher.teacherName,
                            profilePicture = teacher.profilePicture
                        ) {
                            navHostController.navigate(
                                Routes.TeacherScreenByNameRoute(
                                    name = teacher.teacherName
                                )
                            )
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

    val shimmerColors = listOf(
        Color.Red.copy(0.6f),
        Color.Green.copy(0.6f),
        Color.Blue.copy(0.6f)
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    val brushBorder = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(100f, 110f),
        end = Offset(translateAnimation.value, translateAnimation.value)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = teacherName,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .border(
                        brush = brushBorder,
                        width = 2.dp,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            )
            SubcomposeAsyncImage(
                model = profilePicture,
                contentDescription = null,
                loading = { ImageAnimation() },
            )
        }


    }
}



























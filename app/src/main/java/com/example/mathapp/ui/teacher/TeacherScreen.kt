package com.example.mathapp.ui.teacher

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.ui.components.LinearLoader
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.effects.ImageAnimation
import com.example.mathapp.ui.navigation.Routes

@Composable
fun TeacherScreen(navHostController: NavHostController) {
    val viewModel: TeacherViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val teachers = state.teachers

    val listState = rememberLazyListState()

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
                LazyColumn(
                    Modifier.padding(innerPadding),
                    state = listState
                ) {
                    items(
                        items = teachers.sortedBy { it.teacherName },
                        key = { it.teacherName }
                    ) { teacher ->
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = teacherName,
                modifier = Modifier
                    .padding(start = 18.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(vertical = 5.dp, horizontal = 10.dp),
                textAlign = TextAlign.Start
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1f)    // Keeps image square
                .clip(RoundedCornerShape(6.dp))
        ) {
            SubcomposeAsyncImage(
                model = profilePicture,
                contentDescription = null,
                loading = { ImageAnimation() },
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

}



























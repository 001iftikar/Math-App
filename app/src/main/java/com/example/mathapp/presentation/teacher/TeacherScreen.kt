package com.example.mathapp.presentation.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Teacher
import com.example.mathapp.presentation.components.GroupBackGroundComponent
import com.example.mathapp.presentation.components.LinearLoader
import com.example.mathapp.presentation.effects.ImageLoadingAnimation
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.ui.theme.TeacherBackgroundColor
import com.example.mathapp.utils.ColorHex.toColor

@Composable
fun TeacherScreen(navHostController: NavHostController) {
    val viewModel: TeacherViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState()
    val teachers = state.teachers

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopBar(navHostController)
        }
    ) { innerPadding ->

        GroupBackGroundComponent(color = TeacherBackgroundColor)

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
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        state = listState,
                        contentPadding = PaddingValues(bottom = 60.dp) // leave space so last item not hidden
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 46.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Our Faculty",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = "Meet our dedicated faculty shaping the future of mathematics education.",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Light,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(Modifier.height(12.dp))
                        }

                        items(
                            items = teachers.sortedBy { it.teacherName },
                            key = { it.teacherName }
                        ) { teacher ->
                            Teacher(
                                modifier = Modifier.padding(horizontal = 46.dp, vertical = 6.dp),
                                teacher = teacher,
                                loading = { ImageLoadingAnimation() },
                                onClick = {
                                    navHostController.navigate(
                                        Routes.TeacherScreenByNameRoute(
                                            teacher.teacherName
                                        )
                                    )
                                }
                            )
                        }
                    }

                    Text(
                        text = "*Temporary teachers are not included",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    navHostController: NavHostController
) {
    TopAppBar(
        title = { Text("Teachers") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TeacherBackgroundColor
        ),
        navigationIcon = {
            IconButton(
                onClick = { navHostController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        })
}

@Composable
private fun Teacher(
    modifier: Modifier,
    teacher: Teacher,
    loading: @Composable () -> Unit,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = "#140e24".toColor(),
        ),
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = teacher.profilePicture,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    loading = { loading() }
                )
            }

            Text(
                text = teacher.teacherName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = teacher.role,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light
            )

            Text(
                text = teacher.degrees,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light
            )
        }
    }
}

























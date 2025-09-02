package com.example.mathapp.presentation.teacher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.domain.model.Teacher
import com.example.mathapp.presentation.components.HomeButton
import com.example.mathapp.presentation.components.TopAppBarNavIcon
import com.example.mathapp.presentation.effects.ImageAnimation
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.utils.ColorHex.toColor

@Composable
fun TeacherScreenByName(
    teacherName: String,
    navHostController: NavHostController
) {
    val viewModel: TeacherViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTeacherByName(teacherName)
    }

    Scaffold(
        topBar = {

            TopAppBarNavIcon(
                title = teacherName, navController = navHostController,
                rightIcon = {
                    HomeButton { navHostController.navigate(Routes.HomeScreenRoute) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    } }
                },
            )
        }
    ) {
        innerPadding ->

        state.teacher?.let { teacher ->
            IndividualTeacher(teacher = teacher,
                degrees = teacher.degrees,
                role = teacher.role,
                modifier = Modifier.padding(innerPadding))
        } ?: run {
            // Show loading or fallback UI
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else if (state.error != null) {
                    Text("Error: ${state.error}", color = Color.Red)
                } else {
                    Text("No teacher found.")
                }
            }
        }
    }


}


@Composable
fun IndividualTeacher(teacher: Teacher, role: String, degrees: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(350.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            SubcomposeAsyncImage(
                model = teacher.profilePicture,
                contentDescription = null,
                loading = { ImageAnimation() },
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(50.dp))
        TeacherCard(
            degrees = degrees,
            role = role
        )
    }
}

@Composable
private fun TeacherCard(role: String, degrees: String) {
    ElevatedCard(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = degrees,
            style = MaterialTheme.typography.headlineLarge,
            color = "#db520d".toColor(),
            modifier = Modifier.padding(10.dp))
        Spacer(Modifier.height(10.dp))
        Text(text = role,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(10.dp))
    }
}






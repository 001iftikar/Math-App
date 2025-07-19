package com.example.mathapp.ui.teacher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mathapp.domain.model.Teacher
import com.example.mathapp.ui.components.TopAppBarNavIcon
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

            TopBar(teacherName = teacherName,
                onClick = {
                    navHostController.popBackStack()
                })
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
        .padding(12.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = teacher.profilePicture,
                contentDescription = null
            )
        }
        Spacer(Modifier.height(50.dp))
        TeacherCard(
            degrees = degrees,
            role = role
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(teacherName: String, onClick: () -> Unit) {
    TopAppBarNavIcon(title = teacherName, onClick = onClick)
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






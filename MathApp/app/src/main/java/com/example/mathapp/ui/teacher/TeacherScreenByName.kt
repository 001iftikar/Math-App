package com.example.mathapp.ui.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.mathapp.domain.model.Teacher

@Composable
fun TeacherScreenByName(
    modifier: Modifier
) {
    val viewModel: TeacherViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getTeacherByName("Debajit Nath")
    }

    state.teacher?.let { teacher ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = teacher.profilePicture,
                contentDescription = null
            )
            Spacer(Modifier.height(15.dp))
            Text(teacher.teacherName)
        }
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
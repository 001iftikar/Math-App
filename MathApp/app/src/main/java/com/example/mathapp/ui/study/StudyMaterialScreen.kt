package com.example.mathapp.ui.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.navigation.Routes

@Composable
fun StudyHomeScreen(navHostController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarNavIcon(title = "Study Here") {navHostController.popBackStack()} }
    ) { innerPadding ->
        val selectList = listOf(
            "1st", "2nd", "3rd", "4th", "5th", "6th"
        )

        var semesterState by remember { mutableStateOf<String?>(null) }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                SemesterSelectinRow(selectList) {
                    semesterState = it
                }
            }

            semesterState?.let {
                item {
                    ContentScreen(it)
                }
            }


        }
    }
}

@Composable
fun SemesterSelectinRow(
    semesters: List<String>,
    onClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(10.dp)
    ) {
        items(semesters) {
            Button(
                onClick = {
                    onClick(it)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(it)
            }
        }
    }
}

@Composable
fun ContentScreen(
    semester: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Res of semester $semester will be here")
    }

}
















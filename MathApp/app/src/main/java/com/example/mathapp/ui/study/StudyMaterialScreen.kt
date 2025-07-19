package com.example.mathapp.ui.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mathapp.ui.components.TopAppBarNavIcon
import com.example.mathapp.ui.components.bookList

@Composable
fun StudyHomeScreen(
    navHostController: NavHostController,
    paperViewModel: PaperViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarNavIcon(title = "Study Here") { navHostController.popBackStack() } }
    ) { innerPadding ->
        val semesterList = listOf(
            "1", "2", "3", "4", "5", "6"
        )


        val papersState = paperViewModel.paperState.collectAsState()

        var semesterState by rememberSaveable { mutableStateOf<String?>(null) }

        LaunchedEffect(semesterState) {
            semesterState?.let {
                paperViewModel.getPapers(it)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SemesterSelectinRow(semesterList) {
                semesterState = it
            }

            semesterState?.let { semesterState ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                        .padding(10.dp)
                ) {
                    bookList(
                        sem = semesterState,
                        papers = papersState.value.papers
                    )
                }
            }.let {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Select which semester you are on")
                }
            }

        }
    }
}

@Composable
fun SemesterSelectinRow(
    semesters: List<String>,
    onClick: (String) -> Unit,
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
                Text(text = when(it) {
                     "1" -> { "${it}st" }
                    "2" -> {"${it}nd"}
                    "3" -> {"3rd"}

                    else -> {"${it}th"}
                })
            }
        }
    }
}















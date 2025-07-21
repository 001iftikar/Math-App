package com.example.mathapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathapp.R
import com.example.mathapp.ui.navigation.Routes
import com.example.mathapp.utils.ColorHex.toColor


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Scaffold(
        topBar = { TopBar(title = "WELCOME") }
    ) {
        innerPadding ->

        LazyColumn(Modifier.padding(innerPadding)) {
            item { FirstLayer(
                goToTeacher = {navHostController.navigate(Routes.TeacherScreenRoute)},
                goToStudy = {navHostController.navigate(Routes.StudyScreenRoute)}

            ) }
            item { DepartmentAchievements() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(title: String) {

    CenterAlignedTopAppBar(
        title = { Text(title, style = MaterialTheme.typography.headlineLarge,
            color = "#9d0ddb".toColor()) }
    )
}

@Composable
fun FirstLayer(goToTeacher: () -> Unit, goToStudy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()
            .weight(1f)
            .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.teacher),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // Take up most of the vertical space
                        .clickable(
                            onClick = goToTeacher
                        )

                )
                Text(
                    text = "TEACHERS",
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Cyan,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                        .clickable(
                            onClick = goToTeacher
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Spacer(Modifier.width(10.dp))
        ElevatedCard(modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.study_res),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // Take up most of the vertical space
                        .clickable(
                            onClick = goToStudy
                        )
                )
                Text(
                    text = "STUDY TIME",
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Cyan,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                        .clickable(
                            onClick = goToStudy
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun DepartmentAchievements() {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = {
            Toast.makeText(context, "This feature will be added soon", Toast.LENGTH_LONG).show()
        }
    ) {
        Text("Department Achievements",
            modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}










































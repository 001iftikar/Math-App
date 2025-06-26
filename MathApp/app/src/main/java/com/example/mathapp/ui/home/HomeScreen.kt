package com.example.mathapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.compose.rememberNavController
import com.example.mathapp.R
import com.example.mathapp.ui.navigation.Routes


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar(title = "Welcome!!!") }
    ) {
        innerPadding ->

        LazyColumn(Modifier.padding(innerPadding)) {
            item { FirstLayer(
                onClick = {

                    navHostController.navigate(Routes.TeacherScreenRoute)
                }
            ) }
            item { DepartmentAchievements() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String) {

    CenterAlignedTopAppBar(
        title = { Text(title, style = MaterialTheme.typography.headlineLarge,
            color = Color.Cyan) }
    )
}

@Composable
fun FirstLayer(onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(onClick = onClick)
        ) {
            Image(
                painter = painterResource(id = R.drawable.teacher),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Text("TEACHERS", modifier = Modifier
                .align(Alignment.BottomCenter))
        }
        Box(modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .clickable(onClick = {

                Toast.makeText(context, "Will navigate to Study Resource Screen", Toast.LENGTH_LONG).show()

            })
        ) {

            Image(
                painter = painterResource(id = R.drawable.bookselv),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Text("STUDY RESOURCES", modifier = Modifier
                .align(Alignment.BottomCenter))
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










































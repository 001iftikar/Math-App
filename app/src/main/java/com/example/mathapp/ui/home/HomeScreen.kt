package com.example.mathapp.ui.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mathapp.R
import com.example.mathapp.ui.components.DrawerItem
import com.example.mathapp.ui.navigation.Routes
import com.example.mathapp.utils.ColorHex.toColor
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val urlHandler = LocalUriHandler.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .systemBarsPadding()
            ) {
                NavigationDrawerItem(
                    label = {
                        DrawerItem(
                            icon = R.drawable.chat_bot,
                            title = "Chat bot"
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = {
                        DrawerItem(
                            icon = R.drawable.source_code,
                            title = "Source Code"
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }

                        urlHandler.openUri("https://github.com/001ryu-ryu/Math-App")
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(title = "WELCOME") {
                    scope.launch { drawerState.open() }
                }
            }
        ) { innerPadding ->

            LazyColumn(Modifier.padding(innerPadding)) {
                item {
                    FirstLayer(
                        goToTeacher = { navHostController.navigate(Routes.TeacherScreenRoute) },
                        goToStudy = { navHostController.navigate(Routes.StudyScreenRoute) }

                    )
                }
                item {
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    SecondLayer(
                        goToStudySmart = {navHostController.navigate(Routes.StudySmartScreen)},
                        goToAiChat = {
                            navHostController.navigate(Routes.ChatBotScreen)
                        }
                    )
                }
                item { DepartmentAchievements() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(title: String, onClick: () -> Unit) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                title, style = MaterialTheme.typography.headlineLarge,
                color = "#9d0ddb".toColor()
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }
    )
}

@Composable
private fun FirstLayer(goToTeacher: () -> Unit, goToStudy: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                        .padding(8.dp)
                        .clickable(
                            onClick = goToTeacher
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Spacer(Modifier.width(10.dp))
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
private fun DepartmentAchievements() {
    val context = LocalContext.current
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        onClick = {
            Toast.makeText(context, "This feature will be added soon", Toast.LENGTH_LONG).show()
        }
    ) {
        Text(
            "Department Achievements",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun SecondLayer(goToStudySmart: () -> Unit,
                goToAiChat: () -> Unit
               ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.study_smart),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // Take up most of the vertical space
                        .clickable(
                            onClick = goToStudySmart
                        )

                )
                Text(
                    text = "STUDY SMART",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = goToStudySmart
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ai_assistance),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f) // Take up most of the vertical space
                        .clickable(
                            onClick = goToAiChat
                        )
                )
                Text(
                    text = "Ai",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = goToAiChat
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}







































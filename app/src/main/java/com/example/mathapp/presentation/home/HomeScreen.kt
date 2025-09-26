package com.example.mathapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.mathapp.R
import com.example.mathapp.presentation.components.DrawerItem
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.shared.SupabaseSessionViewModel
import com.example.mathapp.utils.ColorHex.toColor
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    supabaseSessionViewModel: SupabaseSessionViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val supabaseSession by supabaseSessionViewModel.userSessionState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val urlHandler = LocalUriHandler.current
    var isUpdateDialogOpen by remember { mutableStateOf(false) }
    val appDetails by homeScreenViewModel.appDetails.collectAsStateWithLifecycle()
    val updateAvailable by homeScreenViewModel.updateAvailable.collectAsStateWithLifecycle()

    val user = supabaseSession.userSession
    val route = if (user != null) Routes.GoalHomeScreen else Routes.GoalSignUpScreen

    LaunchedEffect(Unit) {
        supabaseSessionViewModel.loadUserSession() // state was not updating when logged in and then navigate back and then navigate to Goals Dashboard
    }

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
                            icon = R.drawable.downloads,
                            title = "Downloads"
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navHostController.navigate(Routes.DownloadsScreen)
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

                        urlHandler.openUri(appDetails.sourceCode)
                    }
                )

                NavigationDrawerItem(
                    label = {
                        DrawerItem(
                            icon = R.drawable.outline_update_24,
                            title = "Check for Update"
                        )
                    },
                    selected = false,
                    onClick = { isUpdateDialogOpen = true }
                )

                if (isUpdateDialogOpen) {
                    HorizontalDivider()
                    Box {
                        UpdateAlertDialog(
                            text = if (updateAvailable) "Update available, please download." else "The app is in the latest version",
                            updateAvailable = updateAvailable,
                            onDismissRequest = { isUpdateDialogOpen = false },
                            onDownloadClick = {
                                urlHandler.openUri(appDetails.urlToDownload)
                            }
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    onClick = { scope.launch { drawerState.open() } }
                )
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
                        goToStudySmart = { navHostController.navigate(Routes.StudySmartScreen) },
                        goToGoals = {
                            navHostController.navigate(route)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onClick: () -> Unit) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "WELCOME", style = MaterialTheme.typography.headlineLarge,
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
                SubcomposeAsyncImage(
                    model = R.drawable.teacher,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
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
                SubcomposeAsyncImage(
                    model = R.drawable.study_res,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
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
private fun SecondLayer(
    goToStudySmart: () -> Unit,
    goToGoals: () -> Unit
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
                SubcomposeAsyncImage(
                    model = R.drawable.study_smart,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
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
                SubcomposeAsyncImage(
                    model = R.drawable.goal_per,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .clickable(
                            onClick = goToGoals
                        )
                )
                Text(
                    text = "Goals",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = goToGoals
                        ),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UpdateAlertDialog(
    text: String,
    updateAvailable: Boolean,
    onDismissRequest: () -> Unit,
    onDownloadClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text)
        Spacer(Modifier.height(3.dp))

        if (!updateAvailable)
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("OK", fontSize = 16.sp)
            } else {
            Row {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(text = "Later", fontSize = 16.sp)
                }

                TextButton(
                    onClick = onDownloadClick
                ) {
                    Text("Download", fontSize = 16.sp)
                }
            }
        }
    }
}



























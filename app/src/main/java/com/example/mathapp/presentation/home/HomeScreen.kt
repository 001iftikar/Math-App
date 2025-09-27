package com.example.mathapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
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
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
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

    TopAppBar(
        title = {
            Text(
                text = "Hello, what shall I call you?", style = MaterialTheme.typography.titleLarge,
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
        HomeScreenItem(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            icon = R.drawable.teacher_3_,
            title = "Teachers",
            description = "Your mentors & guides",
            onClick = goToTeacher
        )

        HomeScreenItem(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            icon = R.drawable.bookstack,
            title = "Study time",
            description = "Your study resources",
            onClick = goToStudy
        )
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
        HomeScreenItem(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            icon = R.drawable.super_intelligence,
            title = "Study smart",
            description = "Track your daily grind",
            onClick = goToStudySmart
        )

        HomeScreenItem(
            modifier = Modifier.weight(1f).aspectRatio(1f),
            icon = R.drawable.target,
            title = "Goals",
            description = "Focus. Execute. Win",
            onClick = goToGoals
        )
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

@Composable
private fun HomeScreenItem(
    modifier: Modifier,
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(12.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.35f),
            contentColor = Color.White
        )
    ) {
        BoxWithConstraints {
            val paddingTop = maxWidth * 0.14f
            val paddingStart = maxWidth * 0.06f
            val paddingEnd = maxWidth * 0.08f
            val paddingBottom = maxWidth * 0.06f

            val imageSize = maxWidth * 0.3f
            val titleSize = maxWidth * 0.12f
            val descSize = maxWidth * 0.09f
            Column(
                modifier = Modifier.padding(top = paddingTop, start = paddingStart, end = paddingEnd, bottom = paddingBottom),
                horizontalAlignment = Alignment.Start
            ) {
                SubcomposeAsyncImage(
                    model = icon,
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )

                Text(
                    text = title,
                    fontSize = titleSize.value.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = description,
                    fontSize = descSize.value.sp,
                    fontWeight = FontWeight.Light,
                )
            }
        }

    }
}

























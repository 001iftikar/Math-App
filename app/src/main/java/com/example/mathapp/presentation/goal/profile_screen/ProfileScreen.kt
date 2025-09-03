package com.example.mathapp.presentation.goal.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.mathapp.domain.model.SupabaseUser
import com.example.mathapp.presentation.components.BasicAlertDialogComponent
import com.example.mathapp.presentation.components.BlackBackGround
import com.example.mathapp.presentation.navigation.Routes
import com.example.mathapp.ui.theme.GoalCardColor

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state = viewModel.userState.collectAsStateWithLifecycle()
    val eventState by viewModel.evenState.collectAsState(ProfileScreenEvent.Idle)
    val onEvent = viewModel::onEvent

    LaunchedEffect(eventState) {
        when(eventState) {
            ProfileScreenEvent.OnLogout -> {
                navHostController.navigate(Routes.HomeScreenRoute) {
                    popUpTo<Routes.ProfileScreen> {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.value.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.value.error ?: "Some error happened",
                            color = Color.Red
                        )
                    }
                }

                else -> {
                    UserDetails(
                        modifier = Modifier.padding(innerPadding),
                        user = state.value.user,
                        onLogoutClick = { onEvent(ProfileScreenEvent.LogoutClick) },
                        alertState = state.value.alertState,
                        onDismissRequest = { onEvent(ProfileScreenEvent.DismissRequest) },
                        onConfirmClick = {onEvent(ProfileScreenEvent.ConfirmClick)}
                    )
                }
            }
        }
    }
}

@Composable
private fun UserDetails(
    modifier: Modifier = Modifier,
    user: SupabaseUser,
    alertState: Boolean,
    onLogoutClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 34.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = GoalCardColor
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = user.name.uppercase(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Task completed: ${user.completedTasks}"
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Ongoing tasks: ${user.ongoingTasks}"
                )
            }
        }

        Spacer(Modifier.height(100.dp))
        ElevatedButton(
            onClick = onLogoutClick,
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.Red,
                contentColor = Color.Black
            )
        ) {
            Text("Log out")
        }
    }

    BasicAlertDialogComponent(
        state = alertState,
        onDismissRequest = onDismissRequest,
        onConfirmClick = onConfirmClick
    )
}







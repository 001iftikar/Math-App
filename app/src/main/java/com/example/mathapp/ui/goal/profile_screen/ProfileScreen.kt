package com.example.mathapp.ui.goal.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mathapp.domain.model.SupabaseUser
import com.example.mathapp.ui.components.BlackBackGround
import com.example.mathapp.ui.theme.GoalCardColor

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.userState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        BlackBackGround {
            when {
                state.value.isLoading -> {
                    Column(
                        modifier = Modifier.padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.value.error != null -> {
                    Column(
                        modifier = Modifier.padding(innerPadding),
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
                        completedCount = state.value.completedTasks.toString(),
                        ongoingCount = state.value.ongoingTasks.toString()
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
    completedCount: String,
    ongoingCount: String,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    text = "Task completed: $completedCount"
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Ongoing tasks: $ongoingCount"
                )
            }
        }
    }
}








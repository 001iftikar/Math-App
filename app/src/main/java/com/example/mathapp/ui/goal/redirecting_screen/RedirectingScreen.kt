package com.example.mathapp.ui.goal.redirecting_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mathapp.shared.SupabaseSessionViewModel

@Composable
fun RedirectingScreen(
    redirectingScreenViewModel: SupabaseSessionViewModel = hiltViewModel()
) {
    val state by redirectingScreenViewModel.userSessionState.collectAsState()

    val user = state.userSession?.user
    val userId = user?.id
    val email = user?.email

    Surface(
        modifier = Modifier.systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(userId ?: "No Id")
            Text(email ?: "No email")
        }
    }
}




























package com.example.mathapp.ui.drawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mathapp.ui.components.TopAppBarNavIcon

@Composable
fun ChatBot(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBarNavIcon(
                title = "Harnath",
                navController = navController
            )
        }
    ) { innerPadding ->
        Text(text = "Working on it!",
            modifier = Modifier.padding(innerPadding)) }
}
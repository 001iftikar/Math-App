package com.example.mathapp.ui.drawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mathapp.ui.components.TopAppBarNavIcon

@Composable
fun ChatBot() {
    Scaffold(
        topBar = {
            TopAppBarNavIcon(
                title = "Harnath"
            ) { }
        }
    ) { innerPadding ->
        Text(text = "A chat bot will be added when I learn to integrate AI",
            modifier = Modifier.padding(innerPadding)) }
}
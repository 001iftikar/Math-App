package com.example.mathapp.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
        
        AiScreen(modifier = Modifier.padding(innerPadding))
    }


}

@Composable
fun AiScreen(
    modifier: Modifier = Modifier,
    viewModel: AiViewModel = hiltViewModel()) {
    val response by viewModel.response.collectAsState()
    var question by remember { mutableStateOf("") }

    Column(modifier.padding(16.dp)) {
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Ask a math question") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.askQuestion(question) }) {
            Text("Ask Gemini")
        }

        Spacer(Modifier.height(16.dp))

        Text(text = response)
    }
}

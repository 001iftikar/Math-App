package com.example.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.model.Task
import com.example.mathapp.ui.navigation.NavApp
import com.example.mathapp.ui.studysmart.subject.SubjectScreen
import com.example.mathapp.ui.theme.MathAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MathAppTheme {
                NavApp()
            }
        }
    }
}

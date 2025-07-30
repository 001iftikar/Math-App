package com.example.mathapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Task
import com.example.mathapp.ui.navigation.NavApp
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

val tasks = listOf(
    Task(
        title = "Prepare Notes",
        description = "Desc",
        dueDate = 0L,
        priority = 1,
        relatedToSubject = "Eng",
        isComplete = false,
        taskId = 0,
        taskSubjectId = 4
    ),
    Task(
        title = "Finishing Chapter 2",
        description = "Desc",
        dueDate = 0L,
        priority = 2,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 1,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),
    Task(
        title = "Finishing Chapter 3",
        description = "Desc",
        dueDate = 0L,
        priority = 0,
        relatedToSubject = "Math",
        isComplete = true,
        taskId = 2,
        taskSubjectId = 1
    ),


    )

val sessions = listOf<Session>(
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "English",
        date = 12L,
        duration = 8L,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "Mathematics",
        date = 12L,
        duration = 8L,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "Computer Science",
        date = 12L,
        duration = 8L,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "Computer Science",
        date = 12L,
        duration = 8L,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "Computer Science",
        date = 12L,
        duration = 8L,
        sessionId = 0
    ),
    Session(
        sessionSubjectId = 1,
        relatedToSubject = "Computer Science",
        date = 12L,
        duration = 8L,
        sessionId = 0
    )

)
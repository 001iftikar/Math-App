package com.example.mathapp.ui.studysmart.subject

import androidx.compose.ui.graphics.Color
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject
import com.example.mathapp.domain.model.Task

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val studiedHours: Float = 0f,
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val recentSessions: List<Session> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val session: Session? = null,
    val progress: Float = 0f
)

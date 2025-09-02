package com.example.mathapp.presentation.studysmart.dashboard

import androidx.compose.ui.graphics.Color
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Subject

data class DashBoardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalStudyHours: Float = 0f,
    val subjectList: List<Subject> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = Subject.Companion.subjectCardColors.random(),
    val session: Session? = null
)
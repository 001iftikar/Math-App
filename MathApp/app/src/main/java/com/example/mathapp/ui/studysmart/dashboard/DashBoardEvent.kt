package com.example.mathapp.ui.studysmart.dashboard

import androidx.compose.ui.graphics.Color
import com.example.mathapp.domain.model.Session
import com.example.mathapp.domain.model.Task

sealed class DashBoardEvent {
    data object SaveSubject : DashBoardEvent()
    data class onSubjectCardColorChange(val colors: List<Color>) : DashBoardEvent()
    data class onSubjectNameChange(val name: String): DashBoardEvent()
    data class onGoalStudyHoursChange(val hours: String): DashBoardEvent()

    data class onTaskIsCompleteChange(val task: Task): DashBoardEvent()
    data class onDeleteSessionButtonClick(val session: Session): DashBoardEvent()
    data object DeleteSession: DashBoardEvent()
}
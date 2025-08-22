package com.example.mathapp.ui.goal.insert_goal_screen

import androidx.compose.material3.DatePickerState

data class AddGoalScreenState(
    val title: String = "",
    val description: String = "",
    val supabaseSavableDate: String = "",
    val isDatePickerOpen: Boolean = false
)

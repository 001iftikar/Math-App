package com.example.mathapp.presentation.goal.shared_goals.addsharedgoal_screen

data class AddSharedGoalScreenState(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val isDatePickerOpen: Boolean = false
)
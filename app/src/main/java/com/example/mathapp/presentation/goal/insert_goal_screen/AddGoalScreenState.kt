package com.example.mathapp.presentation.goal.insert_goal_screen

data class AddGoalScreenState(
    val title: String = "",
    val description: String = "",
    val supabaseSavableDate: String = "",
    val isDatePickerOpen: Boolean = false,
    val isDialogOpen: Boolean = false,
    val goalAddMessage: String = ""
)

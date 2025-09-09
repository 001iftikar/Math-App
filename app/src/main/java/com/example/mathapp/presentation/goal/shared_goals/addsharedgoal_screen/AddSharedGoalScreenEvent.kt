package com.example.mathapp.presentation.goal.shared_goals.addsharedgoal_screen

sealed interface AddSharedGoalScreenEvent {
    data object Idle : AddSharedGoalScreenEvent
    data class OnTitleChange(val title: String) : AddSharedGoalScreenEvent
    data class OnDescriptionChange(val description: String) : AddSharedGoalScreenEvent
    data class DatePickerStateChange(val isOpen: Boolean) : AddSharedGoalScreenEvent
    data class OnDateSelectConfirm(val date: String) : AddSharedGoalScreenEvent
    data class OnCreateGoal(val groupId: String) : AddSharedGoalScreenEvent
    data object OnSuccess : AddSharedGoalScreenEvent
}
package com.example.mathapp.ui.goal.ongoing_goals_screen

sealed interface OngoingGoalsScreenEvent {
    data object Idle : OngoingGoalsScreenEvent
    data object Refresh : OngoingGoalsScreenEvent
    data class NavigateToSpecificGoal(val goalId: String) : OngoingGoalsScreenEvent
    data object NavigateBack : OngoingGoalsScreenEvent
    data class SortByEvent(val sortBy: SortBy) : OngoingGoalsScreenEvent
}
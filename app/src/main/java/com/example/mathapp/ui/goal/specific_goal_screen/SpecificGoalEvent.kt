package com.example.mathapp.ui.goal.specific_goal_screen

sealed interface SpecificGoalEvent {
    object Idle : SpecificGoalEvent
    data class GetSpecificGoal(val goalId: String) : SpecificGoalEvent
    object NavigateBack : SpecificGoalEvent
}